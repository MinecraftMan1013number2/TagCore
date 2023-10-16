package com.minecraftman.tagcore;

import com.minecraftman.tagcore.core.managers.*;
import com.minecraftman.tagcore.queue.BaseCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class TagCore extends JavaPlugin {
	private static GameManager gameManager;
	private static ConfigManager configManager;
	private static PreGameManager preGameManager;
	private static PlayerManager playerManager;
	
	@Override
	public void onEnable() {
		configManager = new ConfigManager(this);
		playerManager = new PlayerManager(this);
		preGameManager = new PreGameManager(this);
		gameManager = new GameManager(this);
		
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		final BaseCommand baseCommand = new BaseCommand();
		getCommand("queue").setExecutor(baseCommand);
		getCommand("queue").setTabCompleter(baseCommand);
		
		getCommand("tag").setExecutor(new TagCommand(this));
		
		gameManager.init();
	}
	
	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
	
	public static GameManager getGameManager() {
		return gameManager;
	}
	
	public static ConfigManager getConfigManager() {
		return configManager;
	}
	
	public static PreGameManager getGameComponents() {
		return preGameManager;
	}
	
	public static PlayerManager getPlayerManager() {
		return playerManager;
	}
}
