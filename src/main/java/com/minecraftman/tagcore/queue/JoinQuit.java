package com.minecraftman.tagcore.queue;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuit implements Listener {
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (QueueManager.isInQueue(event.getPlayer())) {
			QueueManager.removePlayer(event.getPlayer());
		}
	}
}
