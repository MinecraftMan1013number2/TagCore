package com.minecraftman.tagcore.gameplay.managers.scoreboard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class TeamManager extends ScoreboardManager {
	private static Team taggerTeam;
	private static Team runnerTeam;
	
	public enum TagTeam {
		TAGGER(),
		RUNNER()
	}
	
	
	public TeamManager() {
		taggerTeam = createTeam("0tagger");
		runnerTeam = createTeam("1runner");
		
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