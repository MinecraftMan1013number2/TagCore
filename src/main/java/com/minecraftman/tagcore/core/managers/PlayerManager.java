package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.core.Lobby;
import com.minecraftman.tagcore.utils.Chat;
import com.minecraftman.tagcore.utils.TagArmor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {
	private final TagCore main;
	private final List<Player> players;
	private Player tagger;
	private final TeamManager teamManager;
	
	public PlayerManager(TagCore main) {
		this.main = main;
		this.players = new ArrayList<>();
		this.teamManager = new TeamManager();
	}
	
	public TeamManager getTeamManager() {
		return teamManager;
	}
	
	public void leaveGame(Player player) {
		leaveGame(player, true);
	}
	public void leaveGame(Player player, boolean sendMsg) {
		if (players.contains(player)) {
			players.remove(player);
//			saveItems({_p})
			teamManager.removeTeam(player);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
				Bukkit.getServer().dispatchCommand(player, "spawn");
				Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
					player.getInventory().clear();
					Lobby.setLobbyInventory(player);
					player.sendMessage(Chat.translate("&aYou have left the game."));
				}, 1L);
			}, 1L);
		} else {
			if (sendMsg) player.sendMessage(Chat.translate("&cYou aren't in the game!"));
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
		teamManager.setTeam(player, TeamManager.TagTeam.RUNNER);
		/*
		wait 1 tick
		giveItems({_p})
		 */
		
	}
	
	public void transferPlayers() {
		if (players.isEmpty()) {
			players.addAll(TagCore.getQueueManager().getQueue());
			TagCore.getQueueManager().clearQueue();
		}
	}
	
	public Player getTagger() {
		return tagger;
	}
	
	public void setTagger(Player player, Player oldTagger) {
		
		if (oldTagger != null) {
			oldTagger.getInventory().setArmorContents(null);
		}
		
		tagger = player;
		if (tagger != null) {
			tagger.sendTitle(Chat.translate("&4&lYou are the tagger!"), Chat.translate("&cTag other people!"), 10, 40, 20);
			ItemStack[] armor = new ItemStack[4];
			armor[3] = TagArmor.getTaggerSkull();
			armor[2] = TagArmor.getTaggerChestplate();
			armor[1] = TagArmor.getTaggerLeggings();
			armor[0] = TagArmor.getTaggerBoots();
			tagger.getInventory().setArmorContents(armor);
		}
		TagCore.getPlayerManager().getTeamManager().setTeam(tagger, TeamManager.TagTeam.TAGGER);
	}
	
	public boolean isPlaying(Player player) {
		return players.contains(player);
	}
	
//	public List<Player> getPlayers() {
//		return players;
//	}
	
	public void endGame() {
		players.forEach(this::leaveGame);
		setTagger(null, tagger);
	}
}
