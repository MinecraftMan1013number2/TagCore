package com.minecraftman.tagcore.core;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class TagPlayer {
	private final TagCore main;
	private static final HashMap<UUID, TagPlayer> players = new HashMap<>();
	
	public static TagPlayer getTagPlayer(UUID uuid) {
		return players.get(uuid);
	}
	public static void removeTagPlayer(UUID uuid) {
		players.remove(uuid);
	}
	
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
	}
	
	public void removeTagTokens(int tokens) {
		addTagTokens(tokens * -1);
	}
	
	public void addTagTokens(int tokens) {
		this.tokens += tokens;
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
			throw new RuntimeException(e);
		}
	}
	
	public Inventory getSavedInventory() {
		return savedInventory;
	}
}
