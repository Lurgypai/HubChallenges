package net.arcanemc.hubchallenges;

import java.sql.Connection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Villager;

import net.arcanemc.core.api.database.Database;
import net.arcanemc.core.api.plugins.Plugin;
import net.arcanemc.hubchallenges.NMS.CustomEntity;
import net.arcanemc.hubchallenges.NMS.CustomVillager;
import net.arcanemc.hubchallenges.challenge.ChallengeListener;
import net.arcanemc.hubchallenges.challenge.ChallengeManager;
import net.arcanemc.hubchallenges.listener.PlayerListener;
import net.arcanemc.hubchallenges.listener.VillagerListener;
import net.arcanemc.hubchallenges.parkour.ParkourManager;
import net.arcanemc.hubchallenges.parkour.ParkourMap;
import net.arcanemc.hubchallenges.region.Region;
import net.arcanemc.hubchallenges.user.UserFileManager;
import net.md_5.bungee.api.ChatColor;

public class HubChallenges extends Plugin {

	public static final String HUBWORLD = "world";
	private static Connection conn;
	private ParkourManager manager = new ParkourManager(this);
	private UserFileManager userManager = new UserFileManager();
	private ChallengeManager challengeManager;
	public ParkourManager getParkourManager() {
		return this.manager;
	}
	
	public UserFileManager getUserFileManager() {
		return this.userManager;
	}
	
	public ChallengeManager getChallengeManager() {
		return this.challengeManager;
	}
	
	public static Connection getConnection() {
		return conn;
	}
	
	@Override
	public void onStart() {
		Database db = new Database(Database.DatabaseTemplate.PROD, "HubChallenges", 3);
		conn = db.getIdleConnection();
		challengeManager = new ChallengeManager();

		this.challengeManager.createTables();
		
		
		CustomEntity.registerEntites();
		prepareNPC();
		
		ParkourMap red = loadParkourMap("&cRed&r", "red");
		ParkourMap blue = loadParkourMap("&9Blue&r", "blue");
		ParkourMap green = loadParkourMap("&aGreen&r", "green");
		ParkourMap yellow = loadParkourMap("&eYellow&r", "yellow");
		manager.addMap("red", red);
		manager.addMap("blue", blue);
		manager.addMap("green", green);
		manager.addMap("yellow", yellow);
		manager.startUpdating();
		
		//fix green
		//only one map
		
		Bukkit.getServer().getPluginManager().registerEvents(new VillagerListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(manager), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ChallengeListener(this), this);
		
		this.saveDefaultConfig();
	}
	
	@Override
	public void onStop() {
		CustomEntity.clearCustomEntities("world");
		manager.stopUpdating();
	}
	
	private void prepareNPC() {
		FileConfiguration config = this.getConfig();
		Location spawnLocation = new Location(Bukkit.getWorld(HUBWORLD),
												config.getDouble("npc.x"),
												config.getDouble("npc.y"),
												config.getDouble("npc.z"));
		
		CustomVillager customVillager = (CustomVillager)CustomEntity.spawnEntity(new CustomVillager(((CraftWorld)Bukkit.getWorld(HUBWORLD)).getHandle()), spawnLocation);
		Villager villager = (Villager)customVillager.getBukkitEntity();
		villager.setCustomName(ChatColor.GOLD + "Parkour Guide");
	}
	
	private ParkourMap loadParkourMap(String displayName, String id) {
		FileConfiguration config = this.getConfig();
		Location startBlock = new Location(Bukkit.getWorld(HUBWORLD), config.getDouble("maps." + id + ".start.x"),
																		config.getDouble("maps." + id + ".start.y"),
																		config.getDouble("maps." + id + ".start.z"));
		
		Region end = new Region(new Location(Bukkit.getWorld(HUBWORLD), config.getDouble("maps." + id + ".loc1.x"),
				config.getDouble("maps." + id + ".loc1.y"),
				config.getDouble("maps." + id + ".loc1.z")),
				new Location(Bukkit.getWorld(HUBWORLD), config.getDouble("maps." + id + ".loc2.x"),
						config.getDouble("maps." + id + ".loc2.y"),
						config.getDouble("maps." + id + ".loc2.z")));
		
		return new ParkourMap(startBlock, end, id, displayName, this);
	}
	
	public static String formatTextForHub(String toFormat) {
		toFormat = ChatColor.DARK_GRAY + "[" +  ChatColor.BLUE + "Hub" + ChatColor.DARK_GRAY + "] > " + ChatColor.RESET + toFormat;
		return toFormat;
	}
}
/*TODO:
 * -gui
 * --automatically add more for each one, rows down.
 * --into "challeng mode"
 * -challenge api
 * --challenge class
 * ---flags
 * --challenge manager
 * ---register challnges
 * ---add them to the gui
 * ---
 */
