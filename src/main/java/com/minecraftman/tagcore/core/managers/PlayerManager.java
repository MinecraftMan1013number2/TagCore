package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.queue.QueueManager;
import com.minecraftman.tagcore.utils.Chat;
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
		players.remove(player);
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
			players.addAll(QueueManager.getQueue());
			QueueManager.clearQueue();
		}
	}
	
	public Player getTagger() {
		return tagger;
	}
	
	public void setTagger(Player player) {
		tagger = player;
		/*
		send title "&4&lYou are the tagger!" with subtitle "&cTag other people!" to {Tagger} for 2 seconds
		modifyTeam({tagger}, "tagger")
		 */
	}
	
	public boolean isPlaying(Player player) {
		return players.contains(player);
	}
}
