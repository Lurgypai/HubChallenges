package net.arcanemc.hubchallenges.parkour;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import net.arcanemc.hubchallenges.HubChallenges;
import net.arcanemc.hubchallenges.challenge.ChallengeManager;
import net.arcanemc.hubchallenges.challenge.ChallengeTimer;
import net.arcanemc.hubchallenges.region.Region;
import net.arcanemc.hubchallenges.user.UserFileManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class ParkourMap {
	private ArrayList<UUID> currentPlayers = new ArrayList<>();
	private Location startBlock;
	private Region end;
	private	ArrayList<UUID> currentWinners = new ArrayList<UUID>();
	private HashMap<UUID, Long> playerStartTimes = new HashMap<UUID, Long>();
	private String displayName;
	private String id;
	private ChallengeTimer timer;
	
	public ParkourMap(Location start, Region end_, String id_, String displayName_, HubChallenges plugin) {
		this.startBlock = start;
		this.end = end_;
		this.displayName = displayName_; 
		this.id = id_;
		this.timer = new ChallengeTimer(plugin);
		this.timer.startTimer();
	}
	
	public void update(ArrayList<UUID> activePlayers, ParkourManager manager, UserFileManager userManager, ChallengeManager chManager) {
		for (UUID uuid : activePlayers) {
			if (Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).getLocation().getBlock().equals(startBlock.getBlock())) {
				if (manager.isPlayingMap(uuid)) {
					if(manager.getMapPlaying(uuid) != null && !manager.getMapPlaying(uuid).equals(this)) {
						Bukkit.getPlayer(uuid).sendMessage(HubChallenges.formatTextForHub("Left " + manager.getMapPlaying(uuid).getDisplayName() + "!"));
						manager.getMapPlaying(uuid).removePlayer(uuid);
						userManager.getFile(uuid).setAllCompleteable(false);
						playerStartTimes.remove(uuid);
					}
				}
				
				if (!this.currentPlayers.contains(uuid)) {
					this.currentPlayers.add(uuid);
					userManager.getFile(uuid).setAllCompleteable(true);
					Bukkit.getPlayer(uuid).sendMessage(HubChallenges.formatTextForHub("Started " + getDisplayName() + "!"));
					playerStartTimes.put(uuid, timer.getSeconds());
				}
			}	
		}

		for (UUID player : this.currentPlayers) {
			if(!Bukkit.getPlayer(player).isSprinting()) {
				userManager.getFile(player).getData("noStop").setCompleteableThisMap(false);
			}
		}
		
		ArrayList<UUID> toRemove = new ArrayList<>();
		for (UUID uuid : this.currentPlayers) {
			if (end.isAbove(Bukkit.getPlayer(uuid).getLocation()) && !currentWinners.contains(uuid)) {
				win(uuid, userManager, chManager);
				currentWinners.add(uuid);
			}
			if (!end.isAbove(Bukkit.getPlayer(uuid).getLocation()) && currentWinners.contains(uuid)) {
				currentWinners.remove(uuid);
				toRemove.add(uuid);
			}
		}
		currentPlayers.removeAll(toRemove);
	}
	
	private void win(UUID uuid, UserFileManager manager, ChallengeManager chManager) {
		IChatBaseComponent chatTitle = ChatSerializer.a(
				"{\"text\": \"You Won!\", \"color\": \"gold\"}");
		PacketPlayOutTitle title = new PacketPlayOutTitle(
				EnumTitleAction.TITLE, chatTitle, 5, 15, 5);
		IChatBaseComponent chatSubtitle = ChatSerializer.a(
				"{\"text\": \"" + this.getDisplayName() + "\"}");
		PacketPlayOutTitle sub = new PacketPlayOutTitle(
				EnumTitleAction.SUBTITLE, chatSubtitle, 5, 15, 5);
		CraftPlayer craftPlayer = (CraftPlayer)Bukkit.getPlayer(uuid);
		craftPlayer.getHandle().playerConnection.sendPacket(title);
		craftPlayer.getHandle().playerConnection.sendPacket(sub);
		
		if (this.timer.getSeconds() - this.playerStartTimes.get(uuid) > 30) {
			manager.getFile(uuid).getData("underThrity").setCompleteableThisMap(false);
			manager.getFile(uuid).getData("underThirtyNoCrouch").setCompleteableThisMap(false);
		}
		
		manager.getFile(uuid).checkChallenges(this, chManager);
	}
	
	public boolean isPlaying(UUID uuid) {
		return this.currentPlayers.contains(uuid);
	}
	
	public void removePlayer(UUID uuid) {
		if (this.currentPlayers.contains(uuid)) {
			this.currentPlayers.remove(uuid);
		}
		this.currentWinners.remove(uuid);
	}
	
	public String getDisplayName() {
		return ChatColor.translateAlternateColorCodes('&',this.displayName);
	}

	public String getId() {
		return this.id;
	}
}
