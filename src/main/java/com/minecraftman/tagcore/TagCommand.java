package com.minecraftman.tagcore;

import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TagCommand implements CommandExecutor {
	private final TagCore main;
	public TagCommand(TagCore main) {
		this.main = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			if (args[0].equals("reload")) {
				sender.sendMessage(Chat.translate("&eReloading..."));
				TagCore.getConfigManager().reloadConfig();
				sender.sendMessage(Chat.translate("&6Reloaded!"));
				return true;
			}
		}
		sender.sendMessage(Chat.translate("&6This server is running on &b&l&nTagCore v" + main.getDescription().getVersion() + " - By MinecraftMan1013"));
		return true;
	}
}
