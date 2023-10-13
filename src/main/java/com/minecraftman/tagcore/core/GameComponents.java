package com.minecraftman.tagcore.core;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.queue.QueueManager;
import com.minecraftman.tagcore.utils.Chat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

public class GameComponents {
	private final TagCore main;
	public GameComponents(TagCore main) {
		this.main = main;
	}
	
	private int cointdownToStart = -1;

	public void initiateStartCountdown(@Nullable Player player) {
		if (Bukkit.getOnlinePlayers().size() > 1) {
			if (QueueManager.getQueueLength() >= 2) {
				if (cointdownToStart != -1) return;
				
				cointdownToStart = TagCore.getConfigManager().getStartDelay();
		        new BukkitRunnable() {
		            private int i = cointdownToStart;
		    
		            @Override
		            public void run() {
		                if (i >= 10) {
			                cointdownToStart = -1;
							
			                TagCore.getGameManager().startGame();
							
		                    cancel();
		                    return;
		                }
				
						if (QueueManager.getQueueLength() > 1) {
							if (i % 10 == 0) {
								TextComponent msg = new TextComponent(String.format("§a§lThe tag game will be starting in %s seconds!" +
										"\n§a§lClick this message to join!", i));
								msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "queue join"));
								
								Bukkit.getOnlinePlayers().forEach(player ->
									player.spigot().sendMessage(msg)
								);
							}
						} else {
							Bukkit.broadcastMessage(Chat.translate("&cThere is only 1 player in the queue! The startup has ended!"));
							
							QueueManager.getQueue().forEach(player ->
								leaveGame(player)
							);
							
							QueueManager.clearQueue();
							cancel();
						}
			            
			            i++;
		            }
		        }.runTaskTimer(main, 20L, 20L);
				
				/*
				set {players::*} to all players where [world of input is {TagWorld}]
				set {tagger} to a random element out of {players::*}
				delete {queue::*}
				setTagger({tagger})
				send title "&4&lYou are the tagger!" with subtitle "&cTag other people!" to {Tagger} for 2 seconds
				modifyTeam({tagger}, "tagger")
				giveItems({tagger})
				loop {players::*} where [input != {tagger}]:
					modifyTeam(loop-value, "runner")
					giveItems(loop-value)
				broadcast "&aThe tagger has been chosen!" to {TagWorld}
				broadcast "&aThe tagger is &a&l%{tagger}%&a!" to {TagWorld}
				send formatted "<cmd:/queue>&aThe tag game has started! Join the queue with ""/queue"" or click me to join!" to all players where [input's world is not {TagWorld}]
				 */
			} else {
				if (player != null) player.sendMessage(Chat.translate("&dYou are the only one in the queue! Invite someone else to join you!"));
			}
		} else {
			if (player != null) player.sendMessage(Chat.translate(""));
		}
	}
	
	public void leaveGame(Player player) {
	
	}
}
