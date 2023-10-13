package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import com.minecraftman.tagcore.utils.Timer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class GameManager {
	private final TagCore main;
	private static boolean gameRunning;
	private static List<Player> players;
	private static int taskID = -1;
	Timer timer;
	
	public GameManager(TagCore main) {
		this.main = main;
		this.timer = null;
	}
	
	public void init() {
		if (taskID != -1) return;
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
			if (gameRunning) {
				BaseComponent[] component = TextComponent.fromLegacyText(Chat.translate("&b&lTime Remaining: &b" + timer.getFormattedTimeRemaining()));
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
				}
				if (timer.isFinished()) {
					endGame();
					return;
				}
				// rules
			}
			/*
			if {tagger} is set:
				remove 1 from {timer}
				if {timer} <= 0:
					endGame()
					stop
				set {_notify} to true if {@Game Time Notification Rule 1}
				set {_notify} to true if {@Game Time Notification Rule 2}
				set {_notify} to true if {@Game Time Notification Rule 3}
				set {_notify} to true if {@Game Time Notification Rule 4}
				set {_notify} to true if {@Game Time Notification Rule 5}
				if {_notify} is set:
					set {_timeLeft} to "%{timer}% seconds" parsed as a timespan
					set {_grammar} to "is" if "%{_timeLeft}%" starts with "1 min" or "1 sec", else "are"
					broadcast "" to {TagWorld}
					broadcast " &eThere %{_grammar}% &6%{_timeLeft}%&e left!" to {TagWorld}
					broadcast "" to {TagWorld}
			else:
				send action bar "&bThere is no game running!" to all players
			 */
		}, 20L, 20L);
	}
	
	public void startGame(Timer timer) {
		//
		this.timer = timer;
	}
	
}
