package com.minecraftman.tagcore.gameplay.events;

import com.minecraftman.tagcore.TagCore;
import com.minecraftman.tagcore.gameplay.managers.PlayerManager;
import com.minecraftman.tagcore.gameplay.managers.scoreboard.TeamManager;
import com.minecraftman.tagcore.gameplay.TagPlayer;
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
				PlayerManager pm = main.getPlayerManager();
				if (attacker.equals(pm.getTagger()) && !attacker.equals(victim)) {
					if (attacker.hasMetadata("GottenTaggedRecently")) {
						event.setCancelled(true);
						attacker.sendMessage(Chat.translate("&cYou need to wait a bit before tagging someone!"));
					} else {
						pm.setTagger(victim, pm.getTagger());
						pm.getTeamManager().setTeam(victim, TeamManager.TagTeam.TAGGER);
						pm.getTeamManager().setTeam(attacker, TeamManager.TagTeam.RUNNER);
						
						victim.setMetadata("GottenTaggedRecently", new FixedMetadataValue(main, true));
						pm.broadcastGame(Chat.translate("&c" + attacker.getName() + " has tagged " + victim.getName() + "!"));
						Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
							victim.setHealth(victim.getHealth() + event.getFinalDamage());
							
							TagPlayer tagVictim = TagPlayer.getTagPlayer(victim.getUniqueId());
							TagPlayer tagAttacker = TagPlayer.getTagPlayer(attacker.getUniqueId());
							int tokensForVictim = main.getConfigManager().getTokensForVictim();
							int tokensForAttacker = main.getConfigManager().getTokensForAttacker();
							if (tagVictim.getTagTokens() > 0) tagVictim.removeTagTokens(tokensForVictim);
							tagAttacker.addTagTokens(tokensForAttacker);
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
									victim.removeMetadata("GottenTaggedRecently", main),
									main.getConfigManager().getTagCooldown());
						}, 1L);
						
					}
				} else {
					event.setCancelled(true);
				}
			}
		}
	}
}
