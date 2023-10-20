package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
	private final TagCore main;
	private final List<Player> players;
	private Player tagger;
	
	public PlayerManager(TagCore main) {
		this.main = main;
		this.players = new ArrayList<>();
	}
	public void leaveGame(Player player) {
		if (players.contains(player)) {
			players.remove(player);
			/*
			saveItems({_p})
			remove {_p} from team entries of (team named "tagger")
			remove {_p} from team entries of (team named "runner")
			 */
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
				Bukkit.getServer().dispatchCommand(player, "spawn");
				Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
					player.getInventory().clear();
//					lobbyItems({_p})
					player.sendMessage(Chat.translate("&aYou have left the game."));
				}, 20L);
			}, 20L);
		} else {
			player.sendMessage(Chat.translate("&cYou aren't in the game!"));
		}
	}
	
	public void joinGame(Player player, boolean broadcastJoin) {
		players.add(player);
		if (broadcastJoin) {
			players.forEach((p) ->
					p.sendMessage(Chat.translate("&2&l" + player.getName() + "&a has joined the tag game!"))
			);
		}
		player.teleport(TagCore.getConfigManager().getTagWorld().getSpawnLocation());
		player.getInventory().clear();
		/*
		modifyTeam(loop-value, "runner")
		wait 1 tick
		giveItems({_p})
		 */
		
	}
	
	public void transferPlayers() {
		if (!players.isEmpty()) {
			players.addAll(TagCore.getQueueManager().getQueue());
			TagCore.getQueueManager().clearQueue();
		}
	}
	
	public Player getTagger() {
		return tagger;
	}
	
	public void setTagger(Player player) {
		tagger = player;
		if (tagger != null) {
			tagger.sendTitle(Chat.translate("&4&lYou are the tagger!"), Chat.translate("&cTag other people!"), 10, 40, 20);
		}
//		modifyTeam({tagger}, "tagger")
	}
	
	public boolean isPlaying(Player player) {
		return players.contains(player);
	}
	
//	public List<Player> getPlayers() {
//		return players;
//	}
	
	public void endGame() {
		players.forEach(this::leaveGame);
		setTagger(null);
//		removeArmor({tagger})
	}
}
