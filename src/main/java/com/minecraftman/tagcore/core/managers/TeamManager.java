package com.minecraftman.tagcore.core.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {
	
	public enum TagTeam {
		TAGGER(sb.getTeam("0tagger")),
		RUNNER(sb.getTeam("1runner"));
		
		private final Team team;
		
		TagTeam(Team team) {
			this.team = team;
		}
		
		public Team getScoreboardTeam() {
			return team;
		}
	}
	
	private static Scoreboard sb;
	
	public TeamManager() {
		sb = Bukkit.getScoreboardManager().getNewScoreboard();
		
		Team taggerTeam = sb.registerNewTeam("0tagger");
		Team runnerTeam = sb.registerNewTeam("1runner");
		taggerTeam.setColor(ChatColor.DARK_RED);
		runnerTeam.setColor(ChatColor.AQUA);
	}
	
	public void setTeam(Player player, TagTeam team) {
		Team taggerTeam = TagTeam.TAGGER.getScoreboardTeam();
		Team runnerTeam = TagTeam.RUNNER.getScoreboardTeam();
		
		if (team == TagTeam.RUNNER) {
			taggerTeam.removeEntry(player.getName());
			runnerTeam.addEntry(player.getName());
		} else {
			runnerTeam.removeEntry(player.getName());
			taggerTeam.addEntry(player.getName());
		}
	}
	
	public void removeTeam(Player player) {
		TagTeam.RUNNER.getScoreboardTeam().removeEntry(player.getName());
		TagTeam.TAGGER.getScoreboardTeam().removeEntry(player.getName());
	}
}