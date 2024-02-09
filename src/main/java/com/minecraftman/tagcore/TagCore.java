package com.minecraftman.tagcore;

import com.minecraftman.tagcore.gameplay.Lobby;
import com.minecraftman.tagcore.gameplay.events.Damage;
import com.minecraftman.tagcore.gameplay.events.JoinQuit;
import com.minecraftman.tagcore.gameplay.events.WorldChange;
import com.minecraftman.tagcore.gameplay.managers.*;
import com.minecraftman.tagcore.queue.BaseCommand;
import com.minecraftman.tagcore.queue.QueueManager;
import com.minecraftman.tagcore.server.Announcer;
import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TagCore extends JavaPlugin {
	private YamlConfiguration announcerYaml;
	private File announcerFile;
	private Announcer announcer;
	
	
	private static GameManager gameManager;
	private static ConfigManager configManager;
	private static PreGameManager preGameManager;
	private static PlayerManager playerManager;
	private static QueueManager queueManager;
	private static DatabaseManager databaseManager;
	
	@Override
	public void onEnable() {
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		configManager = new ConfigManager(this);
		playerManager = new PlayerManager(this);
		preGameManager = new PreGameManager(this);
		gameManager = new GameManager(this);
		queueManager = new QueueManager(this);
		databaseManager = new DatabaseManager(this);
		
		announcerFile = new File(getDataFolder(), "announcer.yml");
		if (!announcerFile.exists()) {
			announcerFile.getParentFile().mkdir();
			saveResource("announcer.yml", false);
		}
		announcerYaml = YamlConfiguration.loadConfiguration(announcerFile);
		
		final BaseCommand baseCommand = new BaseCommand(this);
		final TagCommand tagCommand = new TagCommand(this);
		announcer = new Announcer(this);
		getCommand("queue").setExecutor(baseCommand);
		getCommand("queue").setTabCompleter(baseCommand);
		getCommand("tag").setExecutor(tagCommand);
		getCommand("tag").setTabCompleter(tagCommand);
		getCommand("announcer").setExecutor(announcer);
		getCommand("announcer").setTabCompleter(announcer);
		
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
	
	public YamlConfiguration getAnnouncerYaml() {
		return announcerYaml;
	}
	
	
	
	public void reloadAnnouncer() {
		announcerYaml = YamlConfiguration.loadConfiguration(announcerFile);
		announcer = new Announcer(this);
	}
}
