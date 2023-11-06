package com.minecraftman.tagcore.core;

import com.minecraftman.tagcore.TagCore;

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
	
	public TagPlayer(TagCore main, UUID uuid) throws SQLException {
		this.uuid = uuid;
		players.put(uuid, this);
		this.main = main;

		PreparedStatement p = main.getDatabaseManager().getConnection().prepareStatement("SELECT Tokens FROM tag_playerdata WHERE UUID = ?;");
		p.setString(1, uuid.toString());
		ResultSet results = p.executeQuery();
		if (results.next()) {
			tokens = results.getInt("Tokens");
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
}
