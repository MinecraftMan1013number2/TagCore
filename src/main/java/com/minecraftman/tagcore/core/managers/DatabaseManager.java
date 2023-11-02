package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
	private final String TABLENAME = "tag";
	
	private static Connection connection;
	private final TagCore main;
	
	public DatabaseManager(TagCore main) {
		this.main = main;
		setupDatabase();
	}
	
	private void setupDatabase() {
		try {
//			getConnection().prepareStatement("""
//                CREATE TABLE IF NOT EXISTS DATA(
//                ID INT(36) NOT NULL,
//                SOMETHING VARCHAR(32) NOT NULL,
//                PRIMARY KEY(ID)
//                )
//            """);
			
			getConnection().prepareStatement("""
                CREATE TABLE IF NOT EXISTS PlayerData(
                Player TINYTEXT NOT NULL PRIMARY,
                Tokens MEDIUMINT(255) NOT NULL,
                
                
                
                ID INT(36) NOT NULL,
                SOMETHING VARCHAR(32) NOT NULL,
                PRIMARY KEY(ID)
                )
            """);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("org.sqlite.JDBC");
				connection = DriverManager.getConnection("jdbc:sqlite:"
						+ main.getDataFolder().getAbsolutePath() + "/PlayerInfo.db");
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	public void setValue(String record, Object value) throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement("INSERT INTO " + TABLENAME + "");
	}
}
