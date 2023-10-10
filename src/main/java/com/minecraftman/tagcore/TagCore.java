package com.minecraftman.tagcore;

import com.minecraftman.tagcore.core.managers.ConfigManager;
import com.minecraftman.tagcore.core.GameComponents;
import com.minecraftman.tagcore.core.managers.GameManager;
import com.minecraftman.tagcore.queue.BaseCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class TagCore extends JavaPlugin {
	private static GameManager gameManager;
	private static ConfigManager configManager;
	private static GameComponents gameComponents;
	
	@Override
	public void onEnable() {
		gameManager = new GameManager();
		configManager = new ConfigManager(this);
		gameComponents = new GameComponents(this);
		
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		final BaseCommand baseCommand = new BaseCommand();
		getCommand("queue").setExecutor(baseCommand);
		getCommand("queue").setTabCompleter(baseCommand);
		
//		getServer().getPluginManager().registerEvents(new JoinQuit(), this);
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
	
	public static GameComponents getGameComponents() {
		return gameComponents;
	}
}
