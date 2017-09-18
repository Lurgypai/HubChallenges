package net.arcanemc.hubchallenges.challenge;

import java.util.UUID;

import org.bukkit.Bukkit;

import net.arcanemc.hubchallenges.HubChallenges;

public class SaveFileRunnable implements Runnable {

	private Thread runner;
	private HubChallenges plugin;
	private UUID player;
	
	public SaveFileRunnable(String name, HubChallenges plugin_, UUID player_) {
		this.plugin = plugin_;
		this.player = player_;
		this.runner = new Thread(this, name);
		this.runner.start();
	}
	
	@Override
	public void run() {
		plugin.getUserFileManager().serializeAndDelete(player);
		Bukkit.getLogger().info("[HubChallenges] Saved file for UUID: " + player.toString());
	}
}
