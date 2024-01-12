package com.minecraftman.tagcore.gameplay;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import com.minecraftman.tagcore.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class TagPlayer {
	// TagPlayer object management
	private static final HashMap<UUID, TagPlayer> players = new HashMap<>();
	public static TagPlayer getTagPlayer(UUID uuid) {
		return players.get(uuid);
	}
	public static void removeTagPlayer(UUID uuid) {
		Bukkit.getPlayer(uuid).setScoreboard(null);
		players.remove(uuid);
	}
	
	
	private final TagCore main;
	// TagPlayer Data
	private final UUID uuid;
	private int tokens;
	private Inventory savedInventory;
	
	public TagPlayer(TagCore main, UUID uuid) throws SQLException {
		this.uuid = uuid;
		players.put(uuid, this);
		this.main = main;

		PreparedStatement tokensStatement = main.getDatabaseManager().getConnection().prepareStatement("SELECT Tokens,Inventory FROM tag_playerdata WHERE UUID = ?;");
		tokensStatement.setString(1, uuid.toString());
		ResultSet resultSet = tokensStatement.executeQuery();
		if (resultSet.next()) {
			tokens = resultSet.getInt("Tokens");
			savedInventory = InventoryUtils.deserializePlayerInventory(resultSet.getBytes("Inventory"));
		} else {
			// Add to database
			tokens = 0;
			PreparedStatement insert = main.getDatabaseManager().getConnection().prepareStatement("INSERT INTO tag_playerdata (ID, UUID, Tokens) VALUES (" +
					"default," +
					"?," +
					"0);"
			);
			insert.setString(1, uuid.toString());
			insert.executeUpdate();
		}
		
		main.getSidebarManager().setDynamicSidebarLine("tokens-" + Bukkit.getPlayer(uuid).getName(), 1, "&7&l» ", "&6" + tokens);
	}
	
	public void removeTagTokens(int tokens) {
		addTagTokens(tokens * -1);
	}
	
	public void addTagTokens(int tokens) {
		this.tokens += tokens;
		main.getSidebarManager().setSuffix("tokens-" + Bukkit.getPlayer(uuid).getName(), "&6" + tokens);
		try {
			PreparedStatement statement = main.getDatabaseManager().getConnection().prepareStatement("UPDATE tag_playerdata SET Tokens = " + this.tokens + " WHERE UUID = ?;");
			statement.setString(1, uuid.toString());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getTagTokens() {
		return tokens;
	}
	
	public void saveItems() {
		savedInventory = Bukkit.getPlayer(uuid).getInventory();
		
		try {
			byte[] serializedInv = InventoryUtils.serializeInventory(savedInventory);
			PreparedStatement statement = main.getDatabaseManager().getConnection().prepareStatement("UPDATE tag_playerdata SET Inventory = ? WHERE UUID = ?;");
			statement.setString(2, uuid.toString());
			statement.setBytes(1, serializedInv);
			statement.executeUpdate();
		} catch (SQLException e) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null)
				player.sendMessage(Chat.translate("&cThere was an error saving your items! Please take a SCREENSHOT of your inventory and notify an administrator!"));
			throw new RuntimeException(e);
		}
	}
	
	public void restoreItems() {
		PlayerInventory inv = Bukkit.getPlayer(uuid).getInventory();
		inv.clear();
		inv.setContents(savedInventory.getContents());
	}
}
