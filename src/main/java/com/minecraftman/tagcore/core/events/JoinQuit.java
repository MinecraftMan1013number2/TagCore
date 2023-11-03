package com.minecraftman.tagcore.core.events;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.core.Lobby;
import com.minecraftman.tagcore.core.managers.PlayerManager;
import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuit implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPlayedBefore()) {
			event.setJoinMessage(Chat.translate("&8[&a+&8]&c&l NEW &7" + player.getName()));
		} else {
			event.setJoinMessage(Chat.translate("&8[&a+&8]&7 " + player.getName()));
		}
		
		if (!player.isOp()) player.getInventory().clear();
		
		Lobby.setLobbyInventory(player);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		event.setQuitMessage(Chat.translate("&8[&c-&8]&7 " + player.getName()));
		TagCore.getPlayerManager().leaveGame(true, player);
		
		TagCore.getQueueManager().removePlayer(event.getPlayer());
		if (TagCore.getQueueManager().getQueueLength() == 1 && Bukkit.getOnlinePlayers().size() == 1) {
			TagCore.getQueueManager().clearQueue();
			Bukkit.broadcastMessage(Chat.translate("&cThere is only 1 person online! Invite someone else to play"));
			return;
		}
		
		PlayerManager pm = TagCore.getPlayerManager();
		if (pm.getTagger().equals(player)) {
			pm.broadcastGame(Chat.translate("&a&l" + player.getName() + " was the tagger and left!"));
			if (Bukkit.getOnlinePlayers().size() > 1) {
				Player newTagger = pm.setRandomTagger(true);
				pm.broadcastGame(Chat.translate("&aThe new tagger is &a&l" + newTagger.getName() + "&a!"));
			} else {
				Bukkit.broadcastMessage(Chat.translate("&aThere aren't enough players to continue the game!"));
				TagCore.getGameManager().endGame(null);
			}
		}
		
		// if {players::*} contains player:
		//   saveItems(player)
	}
	
	public static void joinMsg(Player player) {
		Bukkit.broadcastMessage(Chat.translate("&8[&a+&8]&7 " + player.getName()));
	}
	public static void quitMsg(Player player) {
		Bukkit.broadcastMessage(Chat.translate("&8[&c-&8]&7 " + player.getName()));
	}
}
