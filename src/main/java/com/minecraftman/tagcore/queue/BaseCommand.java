package com.minecraftman.tagcore.queue;

import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BaseCommand implements CommandExecutor, TabCompleter {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length >= 1 && args[0].equals("leave")) {
				if (QueueManager.removePlayer(player)) {
					player.sendMessage(Chat.translate("&eYou have left the queue."));
				} else {
					player.sendMessage(Chat.translate("&cYou are not in the queue!"));
				}
			} else {
				if (QueueManager.addPlayer(player)) {
					player.sendMessage(Chat.translate("&eYou have joined the queue!"));
				} else {
					player.sendMessage(Chat.translate("&cYou are already in the queue!"));
				}
			}
		} else {
			sender.sendMessage(Chat.translate("&4Only players can execute commands!"));
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();

//		completions.add("tabCompletion");
//		return StringUtil.copyPartialMatches(args[0], Arrays.asList("option1", "option2"), new ArrayList<>());
		
		return completions;
	}
}