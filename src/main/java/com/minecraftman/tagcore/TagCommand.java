package com.minecraftman.tagcore;

import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagCommand implements CommandExecutor, TabCompleter {
	private final TagCore main;
	public TagCommand(TagCore main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			if (sender instanceof Player player) {
				if (args[0].startsWith("join")) {
					main.getQueueManager().addPlayer(player);
					return true;
				} else if (args[0].equals("leave")) {
					main.getPlayerManager().leaveGame(true, player);
					return true;
				}
			} else {
				sender.sendMessage(Chat.translate("&cOnly players can use this command!"));
				return true;
			}
			if (sender.hasPermission("tag.*")) {
				switch (args[0]) {
					case "stop":
					case "end":
						main.getGameManager().endGame(sender);
						break;
					case "start":
						main.getGameManager().startGame();
						break;
					case "reload":
						sender.sendMessage(Chat.translate("&eReloading..."));
						main.getGameManager().endGame(sender);
						main.getConfigManager().reloadConfig();
						sender.sendMessage(Chat.translate("&6Reloaded!"));
						break;
					default:
						sender.sendMessage(Chat.translate("&cIncorrect usage"));
						break;
				}
				return true;
			}
		}
		sender.sendMessage(Chat.translate("&6This server is running on &b&l&nTagCore v" + main.getDescription().getVersion() + " - By MinecraftMan1013"));
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return StringUtil.copyPartialMatches(args[0], Arrays.asList("stop", "end", "start", "reload"), new ArrayList<>());
	}
}
