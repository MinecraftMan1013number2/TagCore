package com.minecraftman.tagcore.queue;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
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
				if (Bukkit.getOnlinePlayers().size() > 1) {
					if (TagCore.getGameManager().getTagger() == null) {
						if (!QueueManager.getQueue().contains(player)) {
							/*
							if {players::*} does not contain {_p}:
								if {CountdownToStart} is not set:
									add {_p} to {queue::*}
									send "&eYou have joined the queue!" to {_p}
									if size of {queue::*} >= 2:
										startGame()
									else:
										send formatted "<cmd:/queue>&eThere is a player in the queue! Join the queue or click me to play tag with them!" to all players
								else:
									# if chaning code below, also change in startGame() below other comment
									teleport {_p} to spawn of {TagWorld}
									clear {_p}'s inventory
							else:
								send "&cYou are already in the queue!" to {_p}
							 */
						} else {
							player.sendMessage(Chat.translate("&cYou are already in the queue!"));
						}
					} else {
						/*
						if {players::*} does not contain {_p}:
							send "&eYou joined the game!" to {_p}
							teleport {_p} to spawn of {TagWorld}
							add {_p} to {players::*}
							broadcast "&2&l%{_p}%&a has joined the tag game!" to {TagWorld}
							clear {_p}'s inventory
							wait 1 tick
							giveItems({_p})
							modifyTeam({_p}, "runner")
						else:
							send "&cYou are already in the game! If this is an error, do /spawn!" to {_p}
						 */
					}
				} else {
					player.sendMessage(Chat.translate("&cYou are the only player online! Invite someone to play with you!"));
				}
			}
		} else {
			sender.sendMessage(Chat.translate("&4Only players can execute commands!"));
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return StringUtil.copyPartialMatches(args[0], Arrays.asList("join", "leave"), new ArrayList<>());
	}
}