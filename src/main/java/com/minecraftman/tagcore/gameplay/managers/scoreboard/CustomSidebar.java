package com.minecraftman.tagcore.gameplay.managers.scoreboard;

import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomSidebar {
	private static final HashMap<UUID, CustomSidebar> scoreboards = new HashMap<>();
	public static void removePlayer(UUID uuid) {
		scoreboards.remove(uuid);
		updatePlayerCount();
	}
	private static final HashMap<String, String> LINE_DATA = new HashMap<>(Map.of(
			"tagger", "&7&l» &7Tagger: ",
			"timer", "&7&l» &7Timer: ",
			"playercount", "&0&7&l» ",
			"tokens", "&7&l» "
	));
	
	private final Scoreboard scoreboard;
	private final Objective objective;
	
	
	@SuppressWarnings("ConstantValue")
	public CustomSidebar(UUID owner) {
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		
		String title = "&9&lTag!";
		
		if (title.length() > 32) throw new StringIndexOutOfBoundsException("The title of a sidebar has a 32 character limit!");
		scoreboard.registerNewTeam(owner.toString());
		objective = scoreboard.registerNewObjective("tag", "dummy", Chat.translate(title));
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		setStaticSidebarLine(10, "&0&9&l&m                    ");
		setStaticSidebarLine(9 ,"&9&lCurrent Game:");
		setDynamicSidebarLine(8, LINE_DATA.get("tagger"), "&7None");
		setDynamicSidebarLine(7, LINE_DATA.get("timer"), "&7None");
		setStaticSidebarLine(6, "&l&r");
		setStaticSidebarLine(5, "&9&lPlayers online:");
		setDynamicSidebarLine(4, LINE_DATA.get("playercount"), "&70/10");
		setStaticSidebarLine(3, "&e");
		setStaticSidebarLine(2, "&9&lTag Tokens:");
		setDynamicSidebarLine(1, LINE_DATA.get("tokens"), "&60");
		setStaticSidebarLine(0, "&9&l&m                    ");
		
		scoreboards.put(owner, this);
		Bukkit.getPlayer(owner).setScoreboard(scoreboard);
		updatePlayerCount();
		
	}
	
	private void setStaticSidebarLine(int score, String line) {
		objective.getScore(Chat.translate(line)).setScore(score);
	}
	
	private void setDynamicSidebarLine(int score, String line, String suffix) {
		line = Chat.translate(line);
		Team team = scoreboard.registerNewTeam(line);
		team.addEntry(line);
		team.setSuffix(Chat.translate(suffix));
		
		objective.getScore(line).setScore(score);
	}
	
	public void setSuffix(String lineID, String suffix) {
		String id = LINE_DATA.get(lineID);
		if (id == null) throw new NullPointerException("They key '" + lineID + "' does not exist!");
		scoreboard.getTeam(Chat.translate(id)).setSuffix(Chat.translate(suffix));
	}
	
	private static void updatePlayerCount() {
		Bukkit.getOnlinePlayers().forEach((player) ->
				scoreboards.get(player.getUniqueId()).setSuffix("playercount", "&7" + Bukkit.getOnlinePlayers().size() + "/10"));
	}
	
	public static void updateTimer(String newText) {
		Bukkit.getOnlinePlayers().forEach((player) ->
				scoreboards.get(player.getUniqueId()).setSuffix("timer", "&7" + newText));
	}
	
	public static void updateTagger(String newTagger) {
		Bukkit.getOnlinePlayers().forEach((player) ->
				scoreboards.get(player.getUniqueId()).setSuffix("tagger", "&7" + newTagger));
	}
}
