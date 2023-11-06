package com.minecraftman.tagcore.core.events;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.core.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class WorldChange implements Listener {
	private final TagCore main;
	public WorldChange(TagCore main) {
		this.main = main;
	}
	
	@EventHandler
	public void playerWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		if (event.getFrom().getName().equals(main.getConfigManager().getTagWorldName()) && main.getPlayerManager().isPlaying(player)) {
			main.getPlayerManager().leaveGame(true, player);
		}
	}
	
	@EventHandler
	public void onSpawnCommand(PlayerCommandPreprocessEvent event) {
		Bukkit.getScheduler().runTaskLater(main, () -> {
			if (event.getMessage().equalsIgnoreCase("/spawn") && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
				Lobby.setLobbyInventory(event.getPlayer());
			}
		}, 1L);
	}
}
