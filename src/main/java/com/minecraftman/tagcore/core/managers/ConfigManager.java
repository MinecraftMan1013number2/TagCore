package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ConfigManager {
	private final TagCore main;
	public ConfigManager(TagCore main) {
		this.main = main;
	}
	public World getTagWorld() {
		return Bukkit.getWorld(main.getConfig().getString("tag.TagWorld"));
	}
	
	public int getStartDelay() {
		return main.getConfig().getInt("tag.StartDelay");
	}
}
