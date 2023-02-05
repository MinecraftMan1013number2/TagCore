package com.minecraftman.tagcore.queue;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuit implements Listener {
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (QueueManager.onlineManager.isInQueue(event.getPlayer())) {
			Player player = event.getPlayer();
			int pos = QueueManager.onlineManager.getQueuePosition(player);
//			QueueManager.offlineManager.addOfflinePlayer(player, pos);
		}
	}
	/*
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (QueueManager.offlineManager.isInOfflineQueue(event.getPlayer())) {
			Player player = event.getPlayer();
			int pos = QueueManager.offlineManager.getPreviousPosition(player);
			
			QueueManager.offlineManager.removeOfflinePlayer(player);
			
			QueueManager.onlineManager.insertPlayer(player, pos);
		}
	}
	 */
}
