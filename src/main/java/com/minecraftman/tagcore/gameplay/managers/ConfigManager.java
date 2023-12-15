package com.minecraftman.tagcore.gameplay.managers;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class ConfigManager {
	private final TagCore main;
	public ConfigManager(TagCore main) {
		this.main = main;
	}
	
	/* GENERAL OPTIONS */
	private FileConfiguration mainConfig() {
		return main.getConfig();
	}
	
	public void reloadConfig() {
		main.reloadConfig();
	}
	
	public World getTagWorld() {
		return Bukkit.getWorld(getTagWorldName());
	}
	
	public String getTagWorldName() {
		return mainConfig().getString("general.tag-world");
	}
	
	public int getStartDelay() {
		return mainConfig().getInt("general.start-delay", 60);
	}
	
	public int[] getGameLength() {
		return new int[]{mainConfig().getInt("general.game-length.minutes"), mainConfig().getInt("general.game-length.seconds")};
	}
	
	/* GAME OPTIONS */
	public long getTagCooldown() {
		return mainConfig().getLong("game.tagger-cooldown", 20L);
	}
	
	public int getTitleTicks() {
		return mainConfig().getInt("game.tagger-title", 40);
	}
	
	/* TAG TOKEN OPTIONS */
	public int getTokensForAttacker() {
		return mainConfig().getInt("tokens.on-tag");
	}
	
	public int getTokensForVictim() {
		return mainConfig().getInt("tokens.on-tagged");
	}
	
	public int getPlaytimeReward() {
		return mainConfig().getInt("tokens.playtime-reward");
	}
	
	
	
	/* ANNOUNCER */
	private YamlConfiguration announcerYaml() {
		return main.getAnnouncerYaml();
	}
	
	public List<String> getAnnouncerMessages() {
		return announcerYaml().getStringList("messages");
	}
	
	public String getAnnouncerPrefix() {
		return announcerYaml().getString("prefix");
	}
	
	public long getAnnouncerFrequency() {
		return announcerYaml().getLong("frequency");
	}
	
	public boolean announcerPadding() {
		return announcerYaml().getBoolean("padding", true);
	}
}
