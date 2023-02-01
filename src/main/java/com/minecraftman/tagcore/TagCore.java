package com.minecraftman.tagcore;

import com.minecraftman.tagcore.core.ConfigManager;
import com.minecraftman.tagcore.queue.BaseCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class TagCore extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		final BaseCommand baseCommand = new BaseCommand();
		getCommand("queue").setExecutor(baseCommand);
		getCommand("queue").setTabCompleter(baseCommand);
		
		ConfigManager.setMain(this);
	}
	
	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
