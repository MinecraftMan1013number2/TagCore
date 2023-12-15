package com.minecraftman.tagcore.server;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.utils.Chat;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Announcer implements CommandExecutor {
	private boolean enabled;
	private final TagCore main;
	
	private final int taskID;
	
	public Announcer(TagCore main) {
		this.main = main;
		enabled = true;
		Bukkit.broadcastMessage(Chat.translate(main.getConfigManager().getAnnouncerPrefix() + " Announcer has been loaded!"));
		
		long broadcastFrequency = main.getConfigManager().getAnnouncerFrequency() * 20L;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
			if (enabled) {
				List<String> msgs = main.getConfigManager().getAnnouncerMessages();
				String msg = Chat.translate(msgs.get((int)(Math.random() * msgs.size())));
				Matcher matcher = Pattern.compile("<(.*?)>").matcher(msg);
				TextComponent component = new TextComponent();
				while (matcher.find()) {
					msg = msg.replace(matcher.group(), "");
					String[] data = matcher.group(1).split(":");
					switch (data[0]) {
						case "link", "url" ->
								component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, data[1]));
						case "command", "cmd" ->
								// todo: cmd doesnt work; debug it
							component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, data[1]));
						case "suggest", "sgt" ->
							component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, data[1]));
						case "tooltip", "ttp" -> {
							List<BaseComponent> baseComponents = new ArrayList<>();
							for (String s : data[1].split("\\n")) {
								baseComponents.add(new TextComponent(s));
							}
							component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, baseComponents.toArray(new BaseComponent[0])));
						}
						default ->
							main.getLogger().warning("Action '" + data[0] + "' is invalid! Check announcer.yml for valid actions!");
					}
				}
				if (main.getConfigManager().announcerPadding()) {
					component.setText("\n" + msg + "\n");
				} else {
					component.setText(msg);
				}
				Bukkit.getOnlinePlayers().forEach(player -> {
					player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10f, 1f);
					player.spigot().sendMessage(component);
				});
				Bukkit.getConsoleSender().sendMessage(component.getText());
			}
		}, broadcastFrequency, broadcastFrequency);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			String prefix = main.getConfigManager().getAnnouncerPrefix();
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
					sender.sendMessage(Chat.translate(prefix + " Announcer has been disabled!"));
					Bukkit.getScheduler().cancelTask(taskID);
					main.reloadAnnouncer();
					return true;
				}
			}
		}
		sender.sendMessage(Chat.translate("&3Announcer status: &3&l" + (enabled ? "&cENABLED" : "&cDISABLED")));
		return true;
	}
}
