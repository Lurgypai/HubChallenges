package net.arcanemc.hubchallenges.gui;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.arcanemc.hubchallenges.HubChallenges;
import net.arcanemc.hubchallenges.challenge.ChallengeManager;
import net.arcanemc.hubchallenges.parkour.ParkourMap;
import net.arcanemc.hubchallenges.user.UserChallengeData;
import net.arcanemc.hubchallenges.user.UserFileManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class ParkourInventory {

	private final int INV_SIZE = 27;
	private HubChallenges plugin;
	
	public enum GuiAction {
		NOTHING,
		START,
		STOP;
	}
	
	public ParkourInventory(HubChallenges plugin_) {
		this.plugin = plugin_;
	}
	
	public void openGui(UUID player) {
		Inventory gui = Bukkit.createInventory(null, 27, "Parkour");
		
		ItemStack start = createGuiIcon(Material.WOOL, 1, (byte)5, "Start", GuiAction.START);
		ItemStack stop = createGuiIcon(Material.WOOL, 1, (byte)14, "Stop", GuiAction.STOP);
		ItemStack blank = createGuiIcon(Material.STAINED_GLASS_PANE, 1, (byte)0, "", GuiAction.NOTHING);
		
		gui.setItem(9, start);
		gui.setItem(11, stop);
		
		populateChallengeIcons(gui, player, plugin.getChallengeManager(), plugin.getUserFileManager());
		
		for (int slot = 0; slot != INV_SIZE; slot++) {
			if(gui.getItem(slot) == null || gui.getItem(slot).getType() == Material.AIR) {
				gui.setItem(slot, blank);
			}
		}
		
		Bukkit.getPlayer(player).openInventory(gui);
	}
	
	public GuiAction getAction(ItemStack item) {
		//possible settypeid
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound nbttc = nmsItem.getTag();
		
			if (nbttc.hasKey("gui")) {
				return GuiAction.values()[CraftItemStack.asNMSCopy(item).getTag().getInt("gui")];
			}
		return null;
	}
	
	private ItemStack createGuiIcon(Material type, int number, byte dataValue, String name, GuiAction action) {
		ItemStack item = new ItemStack(type, number, dataValue);
		NBTTagCompound nbttc = new NBTTagCompound();
		nbttc.setInt("gui", action.ordinal());
		net.minecraft.server.v1_8_R3.ItemStack nmsItem= CraftItemStack.asNMSCopy(item);
		nmsItem.setTag(nbttc);
		item = CraftItemStack.asBukkitCopy(nmsItem);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	private void populateChallengeIcons(Inventory gui, UUID uuid, ChallengeManager chManager, UserFileManager userManager) {
		final int LENGTH = 4;
		final int HEIGHT = 1;
		final int R_OFFSET = 1;
		final int C_OFFSET = 4;
		final int R_WIDTH = 9;
		int row = 0;
		int column = 0;
		for (UserChallengeData chData : userManager.getFile(uuid).getData()) {
			gui.setItem((row + R_OFFSET) * R_WIDTH + (column + C_OFFSET), this.constructChallengeIcon(chManager, chData));
			
			column++;
			if (column == LENGTH) {
				column = 0;
				row++;
			}
			if (row == HEIGHT) {
				row = 0;
			}
		}
	}
	
	private ItemStack constructChallengeIcon(ChallengeManager chManager, UserChallengeData chData) {
		ItemStack item = this.createGuiIcon(Material.IRON_BLOCK, 1, (byte)0, chManager.getChallenge(chData.getChallengeId()).getName(), GuiAction.NOTHING);
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(chManager.getChallenge(chData.getChallengeId()).getDescription());
		lore.add("Repetitions: " + chData.getCurrentRepetitions() + " / " + chManager.getChallenge(chData.getChallengeId()).getNeededRepetitions());
		ArrayList<String> mapNames = new ArrayList<String>();
		
		for (ParkourMap map : this.plugin.getParkourManager().getMaps()) {
			if (chData.getMapsCompletedOn().contains(map.getId())) {
				mapNames.add(map.getDisplayName());
			}
		}
		
		String mapNm = String.join(ChatColor.DARK_PURPLE + ", ", mapNames);
		lore.add("Completed on: " + mapNm);
		
		
		ItemMeta loreMeta = item.getItemMeta();
		loreMeta.setLore(lore);
		item.setItemMeta(loreMeta);
		return item;
	}
}
