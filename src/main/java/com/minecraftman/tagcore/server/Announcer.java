package com.minecraftman.tagcore.server;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Announcer implements CommandExecutor {
	// create config for announcer
	
	private boolean enabled;
	
	public Announcer(TagCore main) {
		enabled = true;
		Bukkit.broadcast(Chat.translate(prefix + " Announcer has been enabled!"));
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
			if (enabled) {
				Bukkit.getOnlinePlayers().forEach((player ->
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100f, 1f)));
				Bukkit.broadcastMessage("\n" + msg + "\n");
			}
		}, 20L, 20L);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			switch (args[0]) {
				case "on" -> {
					enabled = true;
					sender.sendMessage(Chat.translate(prefix + " Announcer has been enabled!"));
					return true;
				}
				case "off" -> {
					enabled = false;
					sender.sendMessage(Chat.translate(prefix + " Announcer has been disabled!"));
					return true;
				}
				case "reload" -> {
					// reload config
					sender.sendMessage(Chat.translate(prefix + " Announcer has been reloaded!"));
					return true;
				}
			}
		}
		sender.sendMessage(Chat.translate("&3Announcer status: &3&l" + (enabled ? "&cENABLED" : "&cDISABLED")));
		return true;
	}
}
