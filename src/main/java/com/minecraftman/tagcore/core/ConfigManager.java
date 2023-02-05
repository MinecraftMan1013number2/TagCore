package com.minecraftman.tagcore.core;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ConfigManager {
	private static TagCore main;
	public static void setMain(TagCore main) {
		ConfigManager.main = main;
	}
	public static World getTagWorld() {
		return Bukkit.getWorld(main.getConfig().getString("tag.TagWorld"));
	}
	
	public static int getStartDelay() {
		return main.getConfig().getInt("tag.StartDelay");
	}
}
