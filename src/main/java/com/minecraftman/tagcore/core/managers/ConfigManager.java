package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

public class ConfigManager {
	private final TagCore main;
	
	/* GENERAL OPTIONS */
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
		return main.getConfig().getString("General.TagWorld");
	}
	
	public int getStartDelay() {
		return main.getConfig().getInt("General.StartDelay", 60);
	}
	
	public List<Integer> getGameLength() {
		return main.getConfig().getIntegerList("General.GameLength");
	}
	
	/* GAME OPTIONS */
	public long getTagCooldown() {
		return main.getConfig().getLong("Game.TaggerCooldown", 20L);
	}
	
	public int getTitleTicks() {
		return main.getConfig().getInt("Game.TaggerTitle", 40);
	}
}
