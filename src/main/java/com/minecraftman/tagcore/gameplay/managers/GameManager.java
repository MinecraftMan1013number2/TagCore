package com.minecraftman.tagcore.gameplay.managers;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import com.minecraftman.tagcore.utils.Timer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class GameManager {
	private boolean gameRunning = false;
	private Timer timer = null;
	
	private final TagCore main;
	
	public GameManager(TagCore main) {
		this.main = main;
		
		// Initiate periodical for action bar + timer
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
			BaseComponent[] component;
			String[] additionalMsg = null;
			if (gameRunning) {
				component = TextComponent.fromLegacyText(Chat.translate("&b&lTime Remaining: &b" + timer.getFormattedTimeRemaining()));
				if (timer.isFinished()) {
					endGame(null);
					return;
				}
				long secs = timer.getSecondsLeft()%60;
				long mins = timer.getMinutesLeft();
				if (mins+secs != 0 && secs == 0 || (mins == 0 && (secs == 30 || secs == 15 || secs == 10 || secs <= 5))) {
					String minsLeft = (mins != 0) ? ((mins + " minute") + ((mins == 1) ? "" : "s")) : "";
					String secsLeft = (secs != 0) ? ((secs + " second") + ((secs == 1) ? "" : "s")) : "";
					String timeLeft = minsLeft + secsLeft;
					String grammar = (timeLeft.startsWith("1 ") ? "is" : "are");
					additionalMsg = new String[]{"", Chat.translate(" &eThere " + grammar + " &6" + timeLeft + "&e left!"), ""};
				}
				
				main.getSidebarManager().setSuffix("timer", "&7" + timer.getFormattedTimeRemaining());
			} else {
				component = TextComponent.fromLegacyText(Chat.translate("&bThere is no game running!"));
				main.getSidebarManager().setSuffix("timer", "&7None");
			}
			String[] finalAdditionalMsg = additionalMsg;
			Bukkit.getOnlinePlayers().forEach(player -> {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
				if (finalAdditionalMsg != null) {
					player.sendMessage(finalAdditionalMsg);
				}
			});
		}, 0L, 20L);
	}
	
	public void startGame() {
		if (gameRunning) return;
		
		PlayerManager playerManager = main.getPlayerManager();
		
		int[] time = main.getConfigManager().getGameLength();
		Timer timer = new Timer(time[0], time[1]);
		main.getQueueManager().getQueue().forEach(q -> {
			q.sendMessage(Chat.translate("&aThere are enough people to start! Starting..."));
			playerManager.joinGame(q, false);
		});
		
		gameRunning = true;
		this.timer = timer;
		
		playerManager.transferPlayers();
		playerManager.setRandomTagger(false);
		BaseComponent playersComponent = new TextComponent(Chat.translate("&aThe tagger has been chosen!") + "\n" + Chat.translate("&aThe tagger is &a&l" + main.getPlayerManager().getTagger().getName() +"&a!"));
		BaseComponent publicComponent = new TextComponent(Chat.translate("&aThe tag game has started! Join the queue with \"/queue\" or click me to join!"));
		publicComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/queue"));
		
		Bukkit.getOnlinePlayers().forEach((player -> {
			if (playerManager.isPlaying(player)) {
				player.spigot().sendMessage(playersComponent);
			} else {
				player.spigot().sendMessage(publicComponent);
			}
			player.sendMessage(Chat.translate("&eThe game will last for &6" + timer.getMinutesLeft() + " minutes and " + timer.getSecondsLeft()%60 + " seconds&e."));
		}));
	}
	
	public void endGame(@Nullable CommandSender sender) {
		if (timer != null) {
			gameRunning = false;
			timer = null;
			main.getQueueManager().clearQueue();
			main.getPlayerManager().flushPlayers();
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(Chat.translate(" &eThe tag game has ended!"));
			Bukkit.broadcastMessage("");
			if (sender != null) {
				sender.sendMessage(Chat.translate("&eYou have ended the game!"));
			}
		} else {
			assert sender != null;
			sender.sendMessage(Chat.translate("&cThere is no tag game running!"));
		}
	}
}
