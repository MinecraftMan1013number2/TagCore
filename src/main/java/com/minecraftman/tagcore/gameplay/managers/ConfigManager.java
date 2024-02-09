package com.minecraftman.tagcore.gameplay.managers;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
import java.util.Set;

public class ConfigManager {
	private final TagCore main;
	public ConfigManager(TagCore main) {
		this.main = main;
		
		Set<String> currentKeys = mainConfig().getKeys(true);
		for (String defaultKey : mainConfig().getDefaults().getKeys(true)) {
			if (!currentKeys.contains(defaultKey)) {
				main.getLogger().warning("Adding path '" + defaultKey + "' to the config");
				mainConfig().set(defaultKey, mainConfig().getDefaults().get(defaultKey));
			}
		}
		main.saveConfig();
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
		return mainConfig().getInt("general.start-delay"/*, 60*/);
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
	
	public boolean announcerSoundEnabled() {
		return !announcerYaml().getString("sound", "none").equalsIgnoreCase("none");
	}
	
	public Sound getAnnouncerSound() {
		Sound sound;
		try {
			sound = Sound.valueOf(announcerYaml().getString("sound"));
		} catch (IllegalArgumentException e) {
			main.getLogger().warning("Sound '" + announcerYaml().getString("sound") + "' is invalid! Refer to the list of valid sounds here https://helpch.at/docs/1.18/org/bukkit/Sound.html");
			main.getLogger().warning("Defaulting to 'ENTITY_EXPERIENCE_ORB_PICKUP'");
			sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
		}
		return sound;
	}
	
	/* DATABASE */
	public String getDatabaseType() {
		String type = mainConfig().getString("database.type");
		if (type == null || (!type.equalsIgnoreCase("mysql") && !type.equalsIgnoreCase("sqlite"))) {
			return "sqlite";
		}
		return type;
	}
	
	public String getDatabaseValue(String subpath) {
		return mainConfig().getString("database." + subpath);
	}
}
