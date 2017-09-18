package net.arcanemc.hubchallenges.challenge;

import java.util.UUID;

import org.bukkit.Bukkit;

import net.arcanemc.hubchallenges.HubChallenges;
import net.arcanemc.hubchallenges.user.UserFile;

public class LoadFileRunnable implements Runnable {

	private Thread runner;
	private HubChallenges plugin;
	private UUID player;
	
	public LoadFileRunnable(String name, HubChallenges plugin_, UUID player_) {
		this.plugin = plugin_;
		this.player = player_;
		this.runner = new Thread(this, name);
		this.runner.start();
	}
	
	@Override
	public void run() {
		plugin.getUserFileManager().addFile(new UserFile(player, plugin.getChallengeManager()));
		Bukkit.getLogger().info("[HubChallenges] Retrieved file for UUID: " + player.toString());
	}
}
