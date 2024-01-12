package com.minecraftman.tagcore.gameplay.events;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.gameplay.Lobby;
import com.minecraftman.tagcore.gameplay.TagPlayer;
import com.minecraftman.tagcore.gameplay.managers.PlayerManager;
import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class JoinQuit implements Listener {
	TagCore main;
	public JoinQuit(TagCore main) {
		this.main = main;
	}
	
	@EventHandler
	public void onConnect(PlayerLoginEvent event) {
		try {
			new TagPlayer(main, event.getPlayer().getUniqueId());
		} catch (SQLException e) {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Your data could not be loaded! Tell an administrator to check console!");
			throw new RuntimeException(e);
		}
	}
	
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
		
		main.getSidebarManager().setCustomScoreboard(player);
		main.getSidebarManager().setSuffix("playercount", "&7" + Bukkit.getOnlinePlayers().size() + "/10");
	}
	
	@EventHandler
	public void onQuit(@NotNull PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		main.getSidebarManager().setSuffix("playercount", "&7" + Bukkit.getOnlinePlayers().size() + "/10");
		
		TagPlayer.removeTagPlayer(player.getUniqueId());
		
		event.setQuitMessage(Chat.translate("&8[&c-&8]&7 " + player.getName()));
		main.getPlayerManager().leaveGame(true, player);
		
		main.getQueueManager().removePlayer(event.getPlayer());
		if (main.getQueueManager().getQueueLength() == 1 && Bukkit.getOnlinePlayers().size() == 1) {
			main.getQueueManager().clearQueue();
			Bukkit.broadcastMessage(Chat.translate("&cThere is only 1 person online! Invite someone else to play"));
			return;
		}
		
		PlayerManager pm = main.getPlayerManager();
		if (pm.getTagger() != null && pm.getTagger().equals(player)) {
			pm.broadcastGame(Chat.translate("&a&l" + player.getName() + " was the tagger and left!"));
			if (Bukkit.getOnlinePlayers().size() > 1) {
				Player newTagger = pm.setRandomTagger(true);
				pm.broadcastGame(Chat.translate("&aThe new tagger is &a&l" + newTagger.getName() + "&a!"));
			} else {
				Bukkit.broadcastMessage(Chat.translate("&aThere aren't enough players to continue the game!"));
				main.getGameManager().endGame(null);
			}
		}
		
		if (main.getPlayerManager().isPlaying(player))
			TagPlayer.getTagPlayer(player.getUniqueId()).saveItems();
	}
	
	public static void joinMsg(Player player) {
		Bukkit.broadcastMessage(Chat.translate("&8[&a+&8]&7 " + player.getName()));
	}
	public static void quitMsg(Player player) {
		Bukkit.broadcastMessage(Chat.translate("&8[&c-&8]&7 " + player.getName()));
	}
}
