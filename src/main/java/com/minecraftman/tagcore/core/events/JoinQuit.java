package com.minecraftman.tagcore.core.events;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.core.Lobby;
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
		TagCore.getQueueManager().removePlayer(player);
		TagCore.getPlayerManager().leaveGame(true, player);
		
		/*
		if {Tagger} is player:
			set helmet of player to air
			set chestplate of player to air
			set leggings of player to air
			set boots of player to air
			broadcast "&a&l%player% was the tagger and left!" to {TagWorld}
			wait 1 tick
			if number of online players > 1:
				delete {tagger}
				delete {players::*}
				wait 1 tick
				set {players::*} to all players where [world of input is {TagWorld}]
				set {Tagger} to a random element out of {players::*}
				broadcast "&aThe new tagger is &a&l%{tagger}%&a!" to {TagWorld}
				setTagger({tagger})
				modifyTeam({tagger}, "tagger")
				leaveTeam(player, "tagger")
			else:
				leaveTeam(player, "tagger")
				broadcast "&aThere aren't enough players to continue the game!"
				endGame()
				
		if {players::*} contains player:
			saveItems(player)
		 */
	}
	
	public static void joinMsg(Player player) {
		Bukkit.broadcastMessage(Chat.translate("&8[&a+&8]&7 " + player.getName()));
	}
	public static void quitMsg(Player player) {
		Bukkit.broadcastMessage(Chat.translate("&8[&c-&8]&7 " + player.getName()));
	}
}
