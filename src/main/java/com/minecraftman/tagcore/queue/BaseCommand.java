package com.minecraftman.tagcore.queue;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
				if (TagCore.getQueueManager().removePlayer(player)) {
					player.sendMessage(Chat.translate("&eYou have left the queue."));
				} else {
					player.sendMessage(Chat.translate("&cYou are not in the queue!"));
				}
			} else {
				if (Bukkit.getOnlinePlayers().size() > 1) {
					if (TagCore.getPlayerManager().getTagger() == null) {
						QueueManager queueManager = TagCore.getQueueManager();
						if (!queueManager.getQueue().contains(player)) {
							if (!TagCore.getGameComponents().countdownStarted()) {
								queueManager.addPlayer(player);
								player.sendMessage(Chat.translate("&eYou have joined the queue!"));
								if (queueManager.getQueueLength() >= 2) {
									TagCore.getGameComponents().initiateStartCountdown();
								} else {
									BaseComponent component = new TextComponent(Chat.translate("&eThere is a player in the queue! Join the queue or click me to play tag with them!"));
									component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "queue"));
									Bukkit.getOnlinePlayers().forEach(p ->
										p.spigot().sendMessage(component)
									);
								}
							} else {
								TagCore.getPlayerManager().joinGame(player, true);
							}
						} else {
							player.sendMessage(Chat.translate("&cYou are already in the queue!"));
						}
					} else {
						if (!TagCore.getPlayerManager().isPlaying(player)) {
							player.sendMessage(Chat.translate("&eYou joined the game!"));
							TagCore.getPlayerManager().joinGame(player, true);
						} else {
							player.sendMessage(Chat.translate("&cYou are already in the game! If this is an error, do /spawn!"));
						}
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