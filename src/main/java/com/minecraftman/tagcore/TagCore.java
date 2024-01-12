package com.minecraftman.tagcore;

import com.minecraftman.tagcore.gameplay.Lobby;
import com.minecraftman.tagcore.gameplay.events.Damage;
import com.minecraftman.tagcore.gameplay.events.JoinQuit;
import com.minecraftman.tagcore.gameplay.events.WorldChange;
import com.minecraftman.tagcore.gameplay.managers.*;
import com.minecraftman.tagcore.gameplay.managers.scoreboard.SidebarManager;
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
	private static SidebarManager sidebarManager;
	
	@Override
	public void onEnable() {
		configManager = new ConfigManager(this);
		playerManager = new PlayerManager(this);
		preGameManager = new PreGameManager(this);
		gameManager = new GameManager(this);
		queueManager = new QueueManager(this);
		databaseManager = new DatabaseManager(this);
		sidebarManager = new SidebarManager();
		
		setupSidebar();
		
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		announcerFile = new File(getDataFolder(), "announcer.yml");
		if (!announcerFile.exists()) {
			announcerFile.getParentFile().mkdir();
			saveResource("announcer.yml", false);
		}
		announcerYaml = YamlConfiguration.loadConfiguration(announcerFile);
		
		final BaseCommand baseCommand = new BaseCommand(this);
		announcer = new Announcer(this);
		getCommand("queue").setExecutor(baseCommand);
		getCommand("queue").setTabCompleter(baseCommand);
		getCommand("tag").setExecutor(new TagCommand(this));
		// todo: tab completions for tag command
		getCommand("announcer").setExecutor(announcer);
		// todo: tab completions for announcer command
		
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
	
	public SidebarManager getSidebarManager() {
		return sidebarManager;
	}
	
	
	
	public void reloadAnnouncer() {
		announcerYaml = YamlConfiguration.loadConfiguration(announcerFile);
		announcer = new Announcer(this);
	}
	
	/**
	 * Sets up the sidebar for the server. Dynamic lines are set up in {@link com.minecraftman.tagcore.gameplay.TagPlayer}
	 */
	private void setupSidebar() {
		sidebarManager.createSidebar("tag", Chat.translate("&9&lTag!"));
		sidebarManager.setStaticSidebarLine("&9&l&m                    ", 10);
		sidebarManager.setStaticSidebarLine("&9&lCurrent Game:", 9);
		sidebarManager.setDynamicSidebarLine("tagger", 8, "&7&l» &7Tagger: ", "&7None");
		sidebarManager.setDynamicSidebarLine("timer", 7, "&7&l» &7Timer: ", "&7None");
		sidebarManager.setStaticSidebarLine("&l&r", 6);
		sidebarManager.setStaticSidebarLine("&9&lPlayers online:", 5);
		sidebarManager.setDynamicSidebarLine("playercount", 4, "&7&l» ", "&70/10");
		sidebarManager.setStaticSidebarLine("&e", 3);
		sidebarManager.setStaticSidebarLine("&9&lTag Tokens:", 2);
		// Line 1 is a dynamic per-player line, so it is registered in TagPlayer
		sidebarManager.setStaticSidebarLine("&9&l&m                    ", 0);
	}
}
