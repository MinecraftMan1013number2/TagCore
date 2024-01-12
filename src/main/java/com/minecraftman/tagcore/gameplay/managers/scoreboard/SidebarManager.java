package com.minecraftman.tagcore.gameplay.managers.scoreboard;

import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

public class SidebarManager extends ScoreboardManager {
	private Objective objective;
	
	@SuppressWarnings("UnusedReturnValue")
	public Objective createSidebar(String name, String title) {
		if (title.length() > 32) throw new StringIndexOutOfBoundsException("The title of a sidebar has a 32 character limit!");
		objective = registerNewObjective(Chat.translate(name), "dummy", Chat.translate(title));
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		return objective;
	}
	
	public void setStaticSidebarLine(String line, int score) {
		objective.getScore(line).setScore(score);
	}
	
	public void setDynamicSidebarLine(String id, int score, String prefix, String suffix) {
		Team team = createTeam(id);
		team.addEntry(id);
		team.setPrefix(Chat.translate(prefix));
		team.setSuffix(Chat.translate(suffix));
		
		objective.getScore(id).setScore(score);
	}
}
