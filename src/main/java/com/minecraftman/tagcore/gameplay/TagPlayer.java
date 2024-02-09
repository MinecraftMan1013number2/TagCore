package com.minecraftman.tagcore.gameplay;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import com.minecraftman.tagcore.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.IOException;
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
	private ItemStack savedOffHand;
	
	public TagPlayer(TagCore main, UUID uuid) throws SQLException, IOException, ClassNotFoundException {
		this.uuid = uuid;
		players.put(uuid, this);
		this.main = main;

		PreparedStatement tokensStatement = main.getDatabaseManager().getConnection().prepareStatement("SELECT Tokens,Inventory FROM tag_playerdata WHERE UUID = ?;");
		tokensStatement.setString(1, uuid.toString());
		ResultSet resultSet = tokensStatement.executeQuery();
		if (resultSet.next()) {
			tokens = resultSet.getInt("Tokens");
			savedInventory = InventoryUtils.deserializeInventory(resultSet.getString("Inventory"));
			savedOffHand = InventoryUtils.deserializeItem(resultSet.getString("OffHand"))[0];
		} else {
			// Add to database
			tokens = 0;
			savedInventory = null;
			savedOffHand = null;
			PreparedStatement insert = main.getDatabaseManager().getConnection().prepareStatement("INSERT INTO tag_playerdata (UUID, Tokens) VALUES (" +
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
		savedOffHand = Bukkit.getPlayer(uuid).getInventory().getItemInOffHand();
		
		try {
			String serializedInv = InventoryUtils.serializeInventory(savedInventory);
			String serializedOffHand = InventoryUtils.serializeItem(new ItemStack[]{savedOffHand});
			if (serializedInv.length() > 25000 || serializedOffHand.length() > 2000) {
				Player player = Bukkit.getPlayer(uuid);
				if (player != null)
					player.sendMessage(Chat.translate("&cYou have too many items in your inventory!"));
				throw new SQLException();
			}
			PreparedStatement statement = main.getDatabaseManager().getConnection().prepareStatement("UPDATE tag_playerdata SET Inventory = ?, OffHand = ? WHERE UUID = ?;");
			statement.setString(3, uuid.toString());
			statement.setString(2, serializedOffHand);
			statement.setString(1, serializedInv);
			statement.executeUpdate();
		} catch (SQLException e) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null)
				player.sendMessage(Chat.translate("&cThere was an error saving your items! Please take a SCREENSHOT of your inventory and notify an administrator!"));
			throw new RuntimeException(e);
		}
	}
	
	public void restoreItems() {
		if (savedInventory == null) return;
		PlayerInventory inv = Bukkit.getPlayer(uuid).getInventory();
		inv.clear();
		inv.setContents(savedInventory.getContents());
		inv.setItemInOffHand(savedOffHand);
	}
}
