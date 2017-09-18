package net.arcanemc.hubchallenges.region;

import org.bukkit.Location;
import org.bukkit.World;

public class Region {

	private Location loc1;
	private Location loc2;
	private Location minLoc;
	private Location maxLoc;
	private Location centerLoc;
	
	public Region(Location loc1_, Location loc2_) {
		this.loc1 = loc1_;
		this.loc2 = loc2_;
		
		this.minMaxCenter();
	}
	
	private void minMaxCenter() {
		double minX = Math.min(loc1.getX(), loc2.getX());
		double minY = Math.min(loc1.getY(), loc2.getY());
		double minZ = Math.min(loc1.getZ(), loc2.getZ());
		

		double maxX = Math.max(loc1.getX(), loc2.getX());
		double maxY = Math.max(loc1.getY(), loc2.getY());
		double maxZ = Math.max(loc1.getZ(), loc2.getZ());
		
		this.minLoc = new Location(loc1.getWorld(), minX, minY, minZ);
		this.maxLoc = new Location(loc1.getWorld(), maxX, maxY, maxZ);
		
		double centerX = minX + ((maxX - minX)/2);
		double centerY = minY + ((maxY - minY)/2);
		double centerZ = minZ + ((maxZ - minZ)/2);
		
		this.centerLoc = new Location(loc1.getWorld(), centerX, centerY, centerZ);
	}
	
	public Location getloc1() {
		return this.loc1;
	}
	
	public Location getLoc2() {
		return this.loc2;
	}
	
	public Location getMinLoc() {
		return this.minLoc;
	}
	
	public Location getMaxLoc() {
		return this.maxLoc;
	}
	
	public Location getCenter() {
		return this.centerLoc;
	}
	
	public World getWorld() {
		return this.centerLoc.getWorld();
	}
	
	public boolean isInside(Location loc) {
		return (loc.getX() >= minLoc.getX() &&
				loc.getY() >= minLoc.getY() &&
				loc.getZ() >= minLoc.getZ() &&
				
				loc.getX() <= maxLoc.getX() + 1 &&
				loc.getY() <= maxLoc.getY() + 1 &&
				loc.getZ() <= maxLoc.getZ() + 1);
	}
	
	public boolean isAbove(Location loc) {
		return (loc.getX() >= minLoc.getX() &&
				loc.getY() >= minLoc.getY() &&
				loc.getZ() >= minLoc.getZ() &&
				
				loc.getX() <= maxLoc.getX() + 1 && 
				loc.getZ() <= maxLoc.getZ() + 1);
	}
	
	public boolean isBelow(Location loc) {
		return (loc.getX() >= minLoc.getX() &&
				loc.getZ() >= minLoc.getZ() &&
				
				loc.getX() <= maxLoc.getX() + 1 && 
				loc.getY() <= maxLoc.getY() + 1 &&
				loc.getZ() <= maxLoc.getZ() + 1);
	}
}
