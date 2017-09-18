package net.arcanemc.hubchallenges.parkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;

import net.arcanemc.hubchallenges.HubChallenges;
import net.arcanemc.hubchallenges.gui.ParkourInventory;
import net.md_5.bungee.api.ChatColor;

public class ParkourManager {
	//stop players from joining multiple
	
	private HashMap<String, ParkourMap> maps = new HashMap<String, ParkourMap>();
	private ArrayList<UUID> activePlayers = new ArrayList<UUID>();
	private HubChallenges plugin;
	private int taskId;
	private ParkourInventory gui;
	
	public ParkourManager(HubChallenges plugin_) {
		this.plugin = plugin_;
		this.gui = new ParkourInventory(plugin);
		}
	
	public ArrayList<ParkourMap> getMaps() {
		return new ArrayList<ParkourMap>(this.maps.values());
	}
	
	public void addMap(String id, ParkourMap map) {
		this.maps.put(id, map);
	}
	
	public ArrayList<UUID> getActivePlayers() {
		return this.activePlayers;
	}
	
	public void setActivePlayers(ArrayList<UUID> activePlayers_) {
		this.activePlayers = activePlayers_;
	}
	
	public void addActivePlayer(UUID uuid) {
		if (!this.activePlayers.contains(uuid)) {
		this.activePlayers.add(uuid);
		Bukkit.getPlayer(uuid).sendMessage(HubChallenges.formatTextForHub("You started " + ChatColor.GOLD + "Parkour" + ChatColor.RESET + "!"));
		}
	}
	
	public void removeActivePlayer(UUID uuid) {
		if (this.activePlayers.contains(uuid)) {
		this.activePlayers.remove(uuid);
		for (ParkourMap map : new ArrayList<ParkourMap>(this.maps.values())) {
			if(map.isPlaying(uuid)) {
				map.removePlayer(uuid);
			}
		}
		Bukkit.getPlayer(uuid).sendMessage(HubChallenges.formatTextForHub("You left " + ChatColor.GOLD + "Parkour" + ChatColor.RESET + "!"));
		}
	}
	
	private void update() {
		for (ParkourMap map : new ArrayList<ParkourMap>(this.maps.values())) {
			map.update(this.activePlayers, this, plugin.getUserFileManager(), plugin.getChallengeManager());
		}
	}
	
	public void startUpdating() {
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new ParkourUpdater(this), 1, 1);
	}
	
	public void stopUpdating() {
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
	public boolean isPlayingMap(UUID uuid) {
		if (!this.activePlayers.contains(uuid)) {
			return false;
		}
		for (ParkourMap map : new ArrayList<ParkourMap>(this.maps.values())) {
				if(map.isPlaying(uuid)) {
					return true;
				}
		}
		return false;
	}
	
	public ParkourMap getMapPlaying(UUID uuid) {
		for (ParkourMap map : new ArrayList<ParkourMap>(this.maps.values())) {
			if (map.isPlaying(uuid)) {
				return map;
			}
		}
		return null;
	}
	
	public ParkourInventory getGui() {
		return gui;
	}

	private class ParkourUpdater implements Runnable {

		private ParkourManager manager;
		
		public ParkourUpdater(ParkourManager manager_) {
			this.manager = manager_;
		}
		
		@Override
		public void run() {
			manager.update();
		}
		
	}
}
