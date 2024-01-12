package com.minecraftman.tagcore.gameplay.managers.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {
	private static Scoreboard scoreboard;
	
	public ScoreboardManager() {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	public Team createTeam(String name) {
		return scoreboard.registerNewTeam(name);
	}
	
	public void setSuffix(String team, String suffix) {
		scoreboard.getTeam(team).setSuffix(suffix);
	}
	
	public Objective registerNewObjective(String name, String criteria, String title) {
		return scoreboard.registerNewObjective(name, criteria, title);
	}
	
	public void setCustomScoreboard(Player player) {
		player.setScoreboard(scoreboard);
	}
}