package com.minecraftman.tagcore.core;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ConfigManager {
	static TagCore main;
	public static void setMain(TagCore main) {
		ConfigManager.main = main;
	}
	public static World getTagWorld() {
		return Bukkit.getWorld(main.getConfig().getString("tag.TagWorld"));
	}
}
