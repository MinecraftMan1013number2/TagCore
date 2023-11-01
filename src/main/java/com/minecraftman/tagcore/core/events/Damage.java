package com.minecraftman.tagcore.core.events;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.core.managers.PlayerManager;
import com.minecraftman.tagcore.core.managers.TeamManager;
import com.minecraftman.tagcore.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Damage implements Listener {
	TagCore main;
	public Damage(TagCore main) {
		this.main = main;
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player attacker) {
			if (event.getEntity() instanceof Player victim) {
				PlayerManager pm = TagCore.getPlayerManager();
				if (attacker.equals(pm.getTagger()) && !attacker.equals(victim)) {
					if (attacker.hasMetadata("GottenTaggedRecently")) {
						event.setCancelled(true);
						attacker.sendMessage(Chat.translate("&cYou need to wait a bit before tagging someone!"));
					} else {
						pm.setTagger(victim, pm.getTagger());
						pm.getTeamManager().setTeam(victim, TeamManager.TagTeam.TAGGER);
						pm.getTeamManager().setTeam(attacker, TeamManager.TagTeam.RUNNER);
						
						victim.setMetadata("GottenTaggedRecently", new FixedMetadataValue(main, true));
						pm.broadcastGame(Chat.translate("&c" + attacker + " has tagged " + victim+ "!"));
						Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
							victim.setHealth(victim.getHealth() + event.getFinalDamage());
							
							// remove 1 from {tokens::%victim's uuid%} if {tokens::%victim's uuid%} > 0
							// add 3 to {tokens::%attacker's uuid%}
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
									victim.removeMetadata("GottenTaggedRecently", main),
									TagCore.getConfigManager().getTagCooldown());
						}, 1L);
						
					}
				} else {
					event.setCancelled(true);
				}
			}
		}
	}
}
