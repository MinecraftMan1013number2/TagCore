package com.minecraftman.tagcore;

import com.minecraftman.tagcore.core.Lobby;
import com.minecraftman.tagcore.core.events.Damage;
import com.minecraftman.tagcore.core.events.JoinQuit;
import com.minecraftman.tagcore.core.events.WorldChange;
import com.minecraftman.tagcore.core.managers.*;
import com.minecraftman.tagcore.queue.BaseCommand;
import com.minecraftman.tagcore.queue.QueueManager;
import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.plugin.java.JavaPlugin;

public final class TagCore extends JavaPlugin {
	private static GameManager gameManager;
	private static ConfigManager configManager;
	private static PreGameManager preGameManager;
	private static PlayerManager playerManager;
	private static QueueManager queueManager;
	private static DatabaseManager databaseManager;
	
	@Override
	public void onEnable() {
		configManager = new ConfigManager(this);
		playerManager = new PlayerManager(this);
		preGameManager = new PreGameManager(this);
		gameManager = new GameManager(this);
		queueManager = new QueueManager(this);
		databaseManager = new DatabaseManager(this);
		
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		final BaseCommand baseCommand = new BaseCommand(this);
		getCommand("queue").setExecutor(baseCommand);
		getCommand("queue").setTabCompleter(baseCommand);
		
		getCommand("tag").setExecutor(new TagCommand(this));
		
		getServer().getPluginManager().registerEvents(new Lobby(), this);
		getServer().getPluginManager().registerEvents(new JoinQuit(this), this);
		getServer().getPluginManager().registerEvents(new WorldChange(this), this);
		getServer().getPluginManager().registerEvents(new Damage(this), this);
		
		getLogger().info("Plugin initiated!");
		getLogger().info("Database status: " + (databaseManager.isConnected() ? Chat.translate("&aCONNECTED!") : Chat.translate("&cNOT CONNECTED :(")));
	}
	
	@Override
	public void onDisable() {
		databaseManager.disconnect();
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public ConfigManager getConfigManager() {
		return configManager;
	}
	
	public PreGameManager getGameComponents() {
		return preGameManager;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public QueueManager getQueueManager() {
		return queueManager;
	}
	
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}
}
