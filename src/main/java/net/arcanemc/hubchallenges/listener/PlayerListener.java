package net.arcanemc.hubchallenges.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import net.arcanemc.hubchallenges.gui.ParkourInventory.GuiAction;
import net.arcanemc.hubchallenges.parkour.ParkourManager;

public class PlayerListener implements Listener {
	
	private ParkourManager manager;
	
	public PlayerListener(ParkourManager manager_) {
		this.manager = manager_;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		manager.removeActivePlayer(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().getName().equals("Parkour")) {
			ItemStack item = e.getCurrentItem();
			if (item != null) {
			Player player = (Player)e.getWhoClicked();
			GuiAction action = manager.getGui().getAction(item);
				if (action != null) {
					switch(action) {
						case NOTHING:
							break;
						case START:
							manager.addActivePlayer(player.getUniqueId());
							break;
						case STOP:
							manager.removeActivePlayer(player.getUniqueId());
							break;
					}
					e.setCancelled(true);
				}
			}
		}
	}
}
