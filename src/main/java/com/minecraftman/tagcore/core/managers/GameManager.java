package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.queue.QueueManager;
import com.minecraftman.tagcore.utils.Chat;
import com.minecraftman.tagcore.utils.Timer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
	private final TagCore main;
	private static boolean gameRunning;
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
					endGame(null);
					return;
				}
				int secsLeft = (int)Math.round(timer.getSecondsLeft());
				if (secsLeft % 60 == 0 || secsLeft == 30 || secsLeft == 15 || secsLeft == 10 || secsLeft == 5) {
					String timeLeft = secsLeft + " seconds";
					String grammar = (timeLeft.startsWith("1 m") || timeLeft.startsWith("1 s") ? "is" : "are");
					additionalMsg = new String[]{"", Chat.translate(" &eThere " + grammar + " &6" + timeLeft + "&e left!"), ""};
				}
			} else {
				component = TextComponent.fromLegacyText(Chat.translate("&bThere is no game running!"));
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
		
		PlayerManager playerManager = TagCore.getPlayerManager();
		
		List<Integer> time = TagCore.getConfigManager().getGameLength();
		Timer timer = new Timer(time.get(0), time.get(1));
		QueueManager.getQueue().forEach(q -> {
			q.sendMessage(Chat.translate("&aThere are enough people to start! Starting..."));
			playerManager.joinGame(q, false);
		});
		
		gameRunning = true;
		this.timer = timer;
		
		ArrayList<Player> players = QueueManager.getQueue();
		playerManager.transferPlayers();
		playerManager.setTagger(players.get((int)(Math.random() * players.size())));
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
			BaseComponent playersComponent = new TextComponent(Chat.translate("&aThe tagger has been chosen!") + "\n" + Chat.translate("&aThe tagger is &a&l" + TagCore.getPlayerManager().getTagger().getName() +"&a!"));
			BaseComponent publicComponent = new TextComponent(Chat.translate("&aThe tag game has started! Join the queue with \"/queue\" or click me to join!"));
			publicComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "queue"));
			
			Bukkit.getOnlinePlayers().forEach((player -> {
				if (playerManager.isPlaying(player)) {
					player.spigot().sendMessage(playersComponent);
				} else {
					player.spigot().sendMessage(publicComponent);
				}
			}));
		}, 20L*2);
	}
	
	public void endGame(@Nullable Player player) {
		if (timer != null) {
			gameRunning = false;
			timer = null;
			QueueManager.clearQueue();
			TagCore.getPlayerManager().endGame();
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(Chat.translate("  &eThe tag game has ended!"));
			Bukkit.broadcastMessage("");
			if (player != null) {
				player.sendMessage(Chat.translate("&eYou have ended the game!"));
			}
		} else {
			assert player != null;
			player.sendMessage(Chat.translate("&cThere is no tag game running!"));
		}
		
	}
}
