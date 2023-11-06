package com.minecraftman.tagcore.core.managers;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.core.Lobby;
import com.minecraftman.tagcore.utils.Chat;
import com.minecraftman.tagcore.utils.TagArmor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerManager {
	private final List<Player> players;
	private Player tagger;
	private final TeamManager teamManager;
	
	private final TagCore main;
	public PlayerManager(TagCore main) {
		this.main = main;
		this.players = new ArrayList<>();
		this.teamManager = new TeamManager();
	}
	
	public TeamManager getTeamManager() {
		return teamManager;
	}
	
	public void joinGame(Player player, boolean broadcastJoin) {
		players.add(player);
		if (broadcastJoin) {
			players.forEach((p) ->
					p.sendMessage(Chat.translate("&2&l" + player.getName() + "&a has joined the tag game!"))
			);
		}
		player.teleport(main.getConfigManager().getTagWorld().getSpawnLocation());
		player.getInventory().clear();
		teamManager.setTeam(player, TeamManager.TagTeam.RUNNER);
		/*
		wait 1 tick
		giveItems({_p})
		 */
		
	}
	
	public void leaveGame(boolean sendMsg, Player player) {
		if (players.contains(player)) {
			players.remove(player);
			removeActions(player);
		} else {
			if (sendMsg) player.sendMessage(Chat.translate("&cYou aren't in the game!"));
		}
	}
	
	public void flushPlayers() {
		setTagger(null, tagger);
		Iterator<Player> iterator = players.iterator();
		while (iterator.hasNext()) {
			Player player = iterator.next();
			iterator.remove();
			removeActions(player);
		}
	}
	
	private void removeActions(Player player) {
//		saveItems({_p})
		teamManager.removeTeam(player);
		
		Bukkit.getServer().dispatchCommand(player, "spawn");
		// wait 1 tick?
		player.getInventory().clear();
		Lobby.setLobbyInventory(player);
		player.sendMessage(Chat.translate("&aYou have left the game."));
	}
	
	public void transferPlayers() {
		if (players.isEmpty()) {
			players.addAll(main.getQueueManager().getQueue());
			main.getQueueManager().clearQueue();
		}
	}
	
	public Player getTagger() {
		return tagger;
	}
	
	// Set first param to null to only remove armor
	public void setTagger(Player player, Player oldTagger) {
		
		if (oldTagger != null) {
			oldTagger.getInventory().setArmorContents(null);
		}
		
		tagger = player;
		if (tagger != null) {
			tagger.sendTitle(Chat.translate("&4&lYou are the tagger!"), Chat.translate("&cTag other people!"), 10, main.getConfigManager().getTitleTicks(), 20);
			ItemStack[] armor = new ItemStack[4];
			armor[3] = TagArmor.getTaggerSkull();
			armor[2] = TagArmor.getTaggerChestplate();
			armor[1] = TagArmor.getTaggerLeggings();
			armor[0] = TagArmor.getTaggerBoots();
			tagger.getInventory().setArmorContents(armor);
			main.getPlayerManager().getTeamManager().setTeam(tagger, TeamManager.TagTeam.TAGGER);
		}
	}
	
	public Player setRandomTagger(boolean removeArmor) {
		Player newTagger = players.get((int)(Math.random() * players.size()));
		setTagger(newTagger, (removeArmor ? tagger : null));
		return newTagger;
	}
	
	public boolean isPlaying(Player player) {
		return players.contains(player);
	}
	
	public void broadcastGame(String... msg) {
		players.forEach((p) ->
			p.sendMessage(msg)
		);
	}
}