package net.arcanemc.hubchallenges.challenge;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.arcanemc.hubchallenges.HubChallenges;

public class ChallengeListener implements Listener {
	
	private HubChallenges plugin;
	
	public ChallengeListener(HubChallenges plugin_) {
		this.plugin = plugin_;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		new LoadFileRunnable("loadFile", plugin, e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		new SaveFileRunnable("saveFile", plugin, e.getPlayer().getUniqueId());
	}
	
	//before
	@EventHandler
	public void onCrouch(PlayerToggleSneakEvent e) {
		if (plugin.getParkourManager().getMapPlaying(e.getPlayer().getUniqueId()) != null) {
			plugin.getUserFileManager().getFile(e.getPlayer().getUniqueId()).getData("noCrouch").setCompleteableThisMap(false);
			plugin.getUserFileManager().getFile(e.getPlayer().getUniqueId()).getData("underThirtyNoCrouch").setCompleteableThisMap(false);
		}
	}
}
