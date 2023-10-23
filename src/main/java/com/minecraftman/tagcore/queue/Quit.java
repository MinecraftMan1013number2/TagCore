package com.minecraftman.tagcore.queue;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		TagCore.getQueueManager().removePlayer(event.getPlayer());
		if (TagCore.getQueueManager().getQueueLength() == 1 && Bukkit.getOnlinePlayers().size() == 1) {
			TagCore.getQueueManager().clearQueue();
			Bukkit.broadcastMessage(Chat.translate("&cThere is only 1 person online! Invite someone else to play"));
		}
	}
}
