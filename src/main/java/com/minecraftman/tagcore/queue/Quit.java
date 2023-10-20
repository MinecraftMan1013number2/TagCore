package com.minecraftman.tagcore.queue;

import com.minecraftman.tagcore.TagCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		TagCore.getQueueManager().removePlayer(event.getPlayer());
	}
}
