package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

public class ConfigManager {
	private final TagCore main;
	public ConfigManager(TagCore main) {
		this.main = main;
	}
	
	public void reloadConfig() {
		main.reloadConfig();
	}
	
	public World getTagWorld() {
		return Bukkit.getWorld(getTagWorldName());
	}
	
	public String getTagWorldName() {
		return main.getConfig().getString("Tag.TagWorld");
	}
	
	public int getStartDelay() {
		return main.getConfig().getInt("Tag.StartDelay", 60);
	}
	
	public List<Integer> getGameLength() {
		return main.getConfig().getIntegerList("Tag.GameLength");
	}
}
