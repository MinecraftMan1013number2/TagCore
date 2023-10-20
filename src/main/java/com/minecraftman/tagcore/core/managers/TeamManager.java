package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TeamManager {
	
	public enum TagTeam {
		TAGGER(),
		RUNNER();
	}
	private final TagCore main;
	private Scoreboard sb;
	public TeamManager(TagCore main) {
		this.main = main;
		this.sb = Bukkit.getScoreboardManager().getNewScoreboard();
		
		Team taggerTeam = sb.registerNewTeam("0tagger");
		Team runnerTeam = sb.registerNewTeam("1runner");
		taggerTeam.setColor(ChatColor.DARK_RED);
		runnerTeam.setColor(ChatColor.AQUA);
	}
	
	public void setTeam(Player player, TagTeam team) {
		Team taggerTeam = sb.getTeam("0tagger");
		Team runnerTeam = sb.getTeam("1runner");
		
		if (team == TagTeam.RUNNER) {
			taggerTeam.removeEntry(player.getName());
			runnerTeam.addEntry(player.getName());
		} else {
			runnerTeam.removeEntry(player.getName());
			taggerTeam.addEntry(player.getName());
		}
	}

/*
function leaveTeam(p: player, team: text):
	if {_team} is "runner":
		set {_runnerTeam} to team named "1runner"
		remove {_p} from team entries of {_taggerTeam}
	else if {_team} is "tagger":
		set {_taggerTeam} to team named "0tagger"
		remove {_p} from team entries of {_runnerTeam}
*/
}