package com.minecraftman.tagcore.gameplay.managers;

import com.minecraftman.tagcore.TagCore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
	private Connection connection = null;
	private static TagCore main;
	private final MySQL mySQLData;
	
	public DatabaseManager(TagCore mainClass) {
		main = mainClass;
		if (main.getConfigManager().getDatabaseType().equals("mysql")) {
			mySQLData = new DatabaseManager.MySQL();
		} else {
			mySQLData = null;
		}
	}
	
	public class MySQL {
		public final String HOST;
		public final String PORT;
		public final String DATABASE;
		public final String USERNAME;
		public final String PASSWORD;
		
		public MySQL() {
			this.HOST = main.getConfigManager().getDatabaseValue("host");
			this.PORT = main.getConfigManager().getDatabaseValue("port");
			this.DATABASE = main.getConfigManager().getDatabaseValue("database");
			this.USERNAME = main.getConfigManager().getDatabaseValue("username");
			this.PASSWORD = main.getConfigManager().getDatabaseValue("password");
			
			setupDatabase();
		}
	}
	
	private void setupDatabase() {
		try {
			if (getConnection() != null) {
				if (mySQLData == null) {
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
				if (mySQLData == null) {
					// SQLite
					Class.forName("org.sqlite.JDBC");
					connection = DriverManager.getConnection("jdbc:sqlite:"
							+ main.getDataFolder().getAbsolutePath() + "/PlayerInfo.db");
				} else {
					// MySQL
					connection = DriverManager.getConnection(
							"jdbc:mysql://" + mySQLData.HOST + ":" + mySQLData.PORT + "/" + mySQLData.DATABASE + "?useSSL=false",
							mySQLData.USERNAME,
							mySQLData.PASSWORD
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
