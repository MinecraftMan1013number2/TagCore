package com.minecraftman.tagcore.core;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.queue.QueueManager;
import com.minecraftman.tagcore.utils.Chat;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class GameComponents {
	private final TagCore main;
	public GameComponents(TagCore main) {
		this.main = main;
	}
	
	private int cointdownToStart = -1;

	public void startGame(@Nullable Player player) {
		if (Bukkit.getOnlinePlayers().size() > 1) {
			if (QueueManager.getQueueLength() >= 2) {
				if (cointdownToStart != -1) return;
				
				String mesasge = Chat.translate("&aThere are enough people to start! Starting...");
				Location spawn = TagCore.getConfigManager().getTagWorld().getSpawnLocation();
				for (Player q : QueueManager.getQueue()) {
					q.sendMessage(mesasge);
// if changing code below, also change in joinQueue() -> else statement of 'if {CountdownToStart} is not set'
					q.teleport(spawn);
					q.getInventory().clear();
// end change
				}
				cointdownToStart = TagCore.getConfigManager().getStartDelay();
				int i = cointdownToStart;
				
				/*
				public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//        for (int i = 0; i < 10; i++) {
//            Bukkit.getScheduler().runTaskLater(this, (task) -> {
//                Bukkit.broadcastMessage("test");
//            }, 20L*i);
//        }
        
        new BukkitRunnable() {
            private int i = 0;
    
            @Override
            public void run() {
                if (i >= 10) {
                    cancel();
                    return;
                }
        
                Bukkit.broadcastMessage("test " + i);
                i++;
            }
        }.runTaskTimer(this, 20L, 20L);
        return true;
    }
				 */
				
				Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
					if (QueueManager.getQueueLength() > 1) {
						if (i % 10 == 0) {
							TextComponent msg = new TextComponent(String.format("§a§lThe tag game will be starting in %s seconds!" +
									"\n§a§lClick this message to join!", i));
							msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "queue join"));
							
							for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
								onlinePlayer.spigot().sendMessage(msg);
							}
						}
// need to wait 1 tick per loop, not schedule a new task every loop
//						Bukkit.getScheduler().runTaskLater(main, () -> cointdownToStart--, 20L);
					} else {
						Bukkit.broadcastMessage(Chat.translate("&cThere is only 1 player in the queue! The startup has ended!"));
						
						for (Player p : QueueManager.getQueue()) {
							leaveGame(p);
						}
						
						QueueManager.clearQueue();
//						cancel
					}
				}, 20L, 20L);
				
				/*
			delete {CountdownToStart}
			set {timer} to {@Tag Game Time}
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
