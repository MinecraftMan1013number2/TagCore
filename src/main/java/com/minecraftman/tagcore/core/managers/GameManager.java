package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.queue.QueueManager;
import com.minecraftman.tagcore.utils.Chat;
import com.minecraftman.tagcore.utils.Timer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
			BaseComponent[] component;
			String[] additionalMsg = null;
			if (gameRunning) {
				component = TextComponent.fromLegacyText(Chat.translate("&b&lTime Remaining: &b" + timer.getFormattedTimeRemaining()));
				if (timer.isFinished()) {
					endGame();
					return;
				}
				int secsLeft = (int)Math.round(timer.getSecondsLeft());
				if (secsLeft % 60 == 0 || secsLeft == 30 || secsLeft == 15 || secsLeft == 10 || secsLeft == 5) {
					String timeLeft = secsLeft + " seconds";
					String grammar = (timeLeft.startsWith("1 m") || timeLeft.startsWith("1 s") ? "is" : "are");
					additionalMsg = new String[]{"", Chat.translate(" &eThere " + grammar + " &6" + timeLeft + "&e left!"), ""};
				}
			} else {
				component = TextComponent.fromLegacyText(Chat.translate("&b&lTime Remaining: &b" + timer.getFormattedTimeRemaining()));
			}
			String[] finalAdditionalMsg = additionalMsg;
			Bukkit.getOnlinePlayers().forEach(player -> {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
				if (finalAdditionalMsg != null) {
					player.sendMessage(finalAdditionalMsg);
				}
			});
		}, 20L, 20L);
	}
	
	public void startGame() {
		if (gameRunning) return;
		
		List<Integer> time = TagCore.getConfigManager().getGameLength();
		Timer timer = new Timer(time.get(0), time.get(1));
		
		String mesasge = Chat.translate("&aThere are enough people to start! Starting...");
		Location spawn = TagCore.getConfigManager().getTagWorld().getSpawnLocation();
		QueueManager.getQueue().forEach(q -> {
			q.sendMessage(mesasge);
// if changing code below, also change in joinQueue() -> else statement of 'if {CountdownToStart} is not set'
			q.teleport(spawn);
			q.getInventory().clear();
// end change
		});
		
		gameRunning = true;
		this.timer = timer;
		players = QueueManager.getQueue();
		QueueManager.clearQueue();
	}
	
	public void endGame() {
		gameRunning = false;
		timer = null;
		
	}
	
}
