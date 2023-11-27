package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseManager {
	private Connection connection;
	private final TagCore main;
	
	public DatabaseManager(TagCore main) {
		this.main = main;
		setupDatabase();
	}
	
	private void setupDatabase() {
		try {
			getConnection().prepareStatement("""
                CREATE TABLE IF NOT EXISTS tag_playerdata (
                ID INT AUTO_INCREMENT UNIQUE,
                UUID VARCHAR(36) NOT NULL UNIQUE,
                Tokens INT(32) NOT NULL,
                Inventory BLOB,
                PRIMARY KEY(ID)
                )
                """
			).execute();
			
			main.getLogger().info("Databases created!");
		} catch (SQLException e) {
			main.getLogger().log(Level.SEVERE, "The database could not be created!");
			throw new RuntimeException(e);
		}
	}
	
	public Connection getConnection() {
		if (connection == null) {
			try {
//				Class.forName("org.sqlite.JDBC");
//				connection = DriverManager.getConnection("jdbc:sqlite:"
//						+ main.getDataFolder().getAbsolutePath() + "/PlayerInfo.db");
				final String HOST = "localhost";
				final int PORT = 3306;
				final String DATABASE = "tagcore";
				final String USERNAME = "root";
				final String PASSWORD = "";
				
				connection = DriverManager.getConnection(
						"jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false",
						USERNAME,
						PASSWORD
				);
			} catch (SQLException e) {
				main.getLogger().log(Level.SEVERE, "The database could not be connected to!");
				throw new RuntimeException(e);
			}
		}
		return connection;
	}
	
	public boolean isConnected() {
		return connection != null;
	}
	
	public void disconnect() {
		if (isConnected()) {
			try {
				connection.close();
				main.getLogger().info("Database disconnected!");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			main.getLogger().severe("Database was unable to be disconnected from! This means that the connection may still be open, or the connection was closed early!");
		}
	}
}
