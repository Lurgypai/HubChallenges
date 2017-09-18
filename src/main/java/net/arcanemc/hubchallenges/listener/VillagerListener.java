package net.arcanemc.hubchallenges.listener;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import net.arcanemc.hubchallenges.HubChallenges;
import net.arcanemc.hubchallenges.NMS.CustomVillager;
import net.md_5.bungee.api.ChatColor;

public class VillagerListener implements Listener {
	
	private HubChallenges plugin;
	
	public VillagerListener(HubChallenges plugin_) {
		this.plugin = plugin_;
	}
	
	@EventHandler
	public void villagerHit(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && ((CraftEntity)e.getEntity()).getHandle() instanceof CustomVillager) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void clickVillager(PlayerInteractEntityEvent e) {
		if (((CraftEntity)e.getRightClicked()).getHandle() instanceof CustomVillager) {
			//Villager villager = (Villager)e.getRightClicked();
			if (plugin.getUserFileManager().getFile(e.getPlayer().getUniqueId()) != null) {
			plugin.getParkourManager().getGui().openGui(e.getPlayer().getUniqueId());
			} else {
				e.getPlayer().sendMessage(HubChallenges.formatTextForHub("Sorry, we're still loading your " + ChatColor.GOLD + "Parkour " + ChatColor.RESET + "info."));
			}
			e.setCancelled(true);
		}
	}
}
