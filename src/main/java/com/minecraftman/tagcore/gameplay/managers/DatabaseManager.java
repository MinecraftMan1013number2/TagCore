package com.minecraftman.tagcore.gameplay.managers;

import com.minecraftman.tagcore.TagCore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	private Connection connection = null;
	private final TagCore main;
	private final String databaseType;
	
	
	private final String HOST;
	private final String PORT;
	private final String DATABASE;
	private final String USERNAME;
	private final String PASSWORD;
	
	public DatabaseManager(TagCore main) {
		this.main = main;
		this.databaseType = main.getConfigManager().getDatabaseType();
		this.HOST = main.getConfigManager().getDatabaseValue("host");
		this.PORT = main.getConfigManager().getDatabaseValue("port");
		this.DATABASE = main.getConfigManager().getDatabaseValue("database");
		this.USERNAME = main.getConfigManager().getDatabaseValue("username");
		this.PASSWORD = main.getConfigManager().getDatabaseValue("password");
		
		setupDatabase();
	}
	
	private void setupDatabase() {
		try {
			if (getConnection() != null) {
				if (databaseType.equals("sqlite")) {
					// SQLite
					getConnection().prepareStatement("""
							CREATE TABLE IF NOT EXISTS 'tag_playerdata' (
							UUID VARCHAR(36) NOT NULL PRIMARY KEY UNIQUE,
							Tokens INT(32) NOT NULL,
							Inventory MEDIUMTEXT,
							OffHand VARCHAR(2000)
							)
							"""
					).execute();
				} else {
					// MySQL
					getConnection().prepareStatement("""
							CREATE TABLE IF NOT EXISTS tag_playerdata (
							UUID VARCHAR(36) NOT NULL UNIQUE,
							Tokens INT(32) NOT NULL,
							Inventory MEDIUMTEXT,
							OffHand VARCHAR(2000),
							PRIMARY KEY(UUID)
							)
							"""
					).execute();
				}
				
				main.getLogger().info("Database table created!");
			}
		} catch (SQLException e) {
			main.getLogger().severe("The table could not be created!");
			throw new RuntimeException(e);
		}
	}
	
	public Connection getConnection() {
		if (connection == null) {
			try {
				if (databaseType.equals("sqlite")) {
					// SQLite
					Class.forName("org.sqlite.JDBC");
					connection = DriverManager.getConnection("jdbc:sqlite:"
							+ main.getDataFolder().getAbsolutePath() + "\\PlayerInfo.db");
				} else {
					// MySQL
					connection = DriverManager.getConnection(
							"jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false",
							USERNAME,
							PASSWORD
					);
				}
			} catch (SQLException | ClassNotFoundException e) {
				main.getLogger().severe("The database could not be connected to!");
				e.printStackTrace();
				return null;
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
				main.getLogger().info("Database disconnected successfully!");
			} catch (SQLException e) {
				main.getLogger().severe("The database was unable to be disconnected from!");
				throw new RuntimeException(e);
			}
		} else {
			main.getLogger().severe("Database was unable to be disconnected from! This means that the connection may still be open, or the connection was closed early!");
		}
	}
}
