package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.queue.QueueManager;
import com.minecraftman.tagcore.utils.Chat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

public class PreGameManager {
	private final TagCore main;
	public PreGameManager(TagCore main) {
		this.main = main;
	}
	
	private int cointdownToStart = -1;
	
	public boolean countdownStarted() {
		return (cointdownToStart > -1);
	}
	
	public void initiateStartCountdown(@Nullable Player player) {
		if (Bukkit.getOnlinePlayers().size() > 1) {
			if (TagCore.getQueueManager().getQueueLength() >= 2) {
				if (cointdownToStart != -1) return;
				
				cointdownToStart = TagCore.getConfigManager().getStartDelay();
		        new BukkitRunnable() {
		            @Override
		            public void run() {
		                if (cointdownToStart <= 0) {
			                cointdownToStart = -1;
							
			                TagCore.getGameManager().startGame();
							
		                    cancel();
		                    return;
		                }
						
						QueueManager queueManager = TagCore.getQueueManager();
						if (queueManager.getQueueLength() > 1) {
							if (cointdownToStart % 10 == 0) {
								TextComponent msg = new TextComponent(String.format("§a§lThe tag game will be starting in %s seconds!" +
										"\n§a§lClick this message to join!", cointdownToStart));
								msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/queue"));
								
								Bukkit.getOnlinePlayers().forEach(player ->
									player.spigot().sendMessage(msg)
								);
							}
						} else {
							Bukkit.broadcastMessage(Chat.translate("&cThere is only 1 player in the queue! The startup has ended!"));
							
							cointdownToStart = -1;
							queueManager.clearQueue();
							cancel();
							return;
						}
			   
						cointdownToStart--;
		            }
		        }.runTaskTimer(main, 20L, 20L);
			} else {
				if (player != null) player.sendMessage(Chat.translate("&dYou are the only one in the queue! Invite someone else to join you!"));
			}
		} else {
			if (player != null) player.sendMessage(Chat.translate(""));
		}
	}
}
