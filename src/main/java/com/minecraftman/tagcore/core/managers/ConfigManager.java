package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.World;

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
		return main.getConfig().getString("general.tag-world");
	}
	
	public int getStartDelay() {
		return main.getConfig().getInt("general.start-delay", 60);
	}
	
	public int[] getGameLength() {
		return new int[]{main.getConfig().getInt("general.game-length.minutes"), main.getConfig().getInt("general.game-length.seconds")};
	}
	
	/* GAME OPTIONS */
	public long getTagCooldown() {
		return main.getConfig().getLong("game.tagger-cooldown", 20L);
	}
	
	public int getTitleTicks() {
		return main.getConfig().getInt("game.tagger-title", 40);
	}
	
	/* TAG TOKEN OPTIONS */
	public int getTokensForAttacker() {
		return main.getConfig().getInt("tokens.on-tag");
	}
	
	public int getTokensForVictim() {
		return main.getConfig().getInt("tokens.on-tagged");
	}
	
	public int getPlaytimeReward() {
		return main.getConfig().getInt("tokens.playtime-reward");
	}
}
