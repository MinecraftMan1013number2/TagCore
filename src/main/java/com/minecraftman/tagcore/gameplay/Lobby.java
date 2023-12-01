package com.minecraftman.tagcore.gameplay;

import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Lobby implements Listener {
	public static void setLobbyInventory(Player player) {
		ItemStack play = new ItemStack(Material.CLOCK);
		ItemMeta playItemMeta = play.getItemMeta();
		playItemMeta.setDisplayName(Chat.translate("&6&lPlay"));
		play.setItemMeta(playItemMeta);
		
		ItemStack shop = new ItemStack(Material.NETHER_STAR);
		ItemMeta shopItemMeta = shop.getItemMeta();
		shopItemMeta.setDisplayName(Chat.translate("&6&lShop"));
		shop.setItemMeta(shopItemMeta);
		
		player.getInventory().setItem(2, play);
		player.getInventory().setItem(6, shop);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		ItemStack clickedItem = event.getItem();
		if (clickedItem == null) return;
		if (clickedItem.getType() == Material.CLOCK && ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("Play")) {
			Bukkit.dispatchCommand(event.getPlayer(), "queue");
		} else if (clickedItem.getType() == Material.NETHER_STAR && ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()).equals("Shop")) {
			Bukkit.dispatchCommand(event.getPlayer(), "shop");
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getCurrentItem().getType() == Material.NETHER_STAR || event.getCurrentItem().getType() == Material.CLOCK) {
			if (event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if (event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR || event.getItemDrop().getItemStack().getType() == Material.CLOCK) {
			if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
			}
		}
	}
}