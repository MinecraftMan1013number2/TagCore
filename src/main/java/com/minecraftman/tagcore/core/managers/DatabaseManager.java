package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseManager {
	private static Connection connection;
	private final TagCore main;
	
	public DatabaseManager(TagCore main) {
		this.main = main;
		setupDatabase();
	}
	
	private void setupDatabase() {
		try {
			getConnection().prepareStatement("""
                CREATE TABLE IF NOT EXISTS DATA(
                ID INT(36) NOT NULL,
                SOMETHING VARCHAR(32) NOT NULL,
                PRIMARY KEY(ID)
                )
            """);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:"
						+ main.getDataFolder().getAbsolutePath() + "/PlayerInfo.db");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	// TODO: watch lecture 82 & 84
}
