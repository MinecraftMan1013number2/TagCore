package com.minecraftman.tagcore.gameplay.managers.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {
	private static final Scoreboard teams = Bukkit.getScoreboardManager().getNewScoreboard();
	private static Team taggerTeam;
	private static Team runnerTeam;
	
	public enum TagTeam {
		TAGGER(),
		RUNNER()
	}
	
	
	public TeamManager() {
		taggerTeam = teams.registerNewTeam("0tagger");
		runnerTeam = teams.registerNewTeam("1runner");
		
		taggerTeam.setColor(ChatColor.DARK_RED);
		runnerTeam.setColor(ChatColor.AQUA);
	}
	
	public void setTeam(Player player, TagTeam team) {
		if (team == TagTeam.RUNNER) {
			taggerTeam.removeEntry(player.getName());
			runnerTeam.addEntry(player.getName());
		} else {
			runnerTeam.removeEntry(player.getName());
			taggerTeam.addEntry(player.getName());
		}
	}
	
	public void removeTeam(Player player) {
		runnerTeam.removeEntry(player.getName());
		taggerTeam.removeEntry(player.getName());
	}
}