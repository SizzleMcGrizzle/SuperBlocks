package me.sizzlemcgrizzle.superblocks.beacon;

import me.sizzlemcgrizzle.superblocks.util.SuperBlocksUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BeaconData {
	
	private final Location location;
	private final UUID owner;
	private PotionEffect buff1;
	private PotionEffect buff2;
	private PotionEffect debuff;
	private long expireTime;
	
	public BeaconData(Location location, UUID uuid, String buff1, String buff2, String debuff, long expires) {
		this.location = location;
		this.owner = uuid;
		this.buff1 = SuperBlocksUtil.getPotionFromString(buff1);
		this.buff2 = SuperBlocksUtil.getPotionFromString(buff2);
		this.debuff = SuperBlocksUtil.getPotionFromString(debuff);
		this.expireTime = expires;
	}
	
	/**
	 * Gets the tier of a beacon depending on its platform(s)
	 *
	 * @return 0 for inactive, 1 for 3x3, 2 for 5x5, 3 for 7x7, 4 for 9x9
	 */
	public double getRange() {
		int tier = 0;
		if (new BeaconPlatformBox(location.clone().subtract(1, 1, 1), location.clone().subtract(-1, 1, -1)).isValidTier())
			tier++;
		if (new BeaconPlatformBox(location.clone().subtract(2, 2, 2), location.clone().subtract(-2, 2, -2)).isValidTier())
			tier++;
		if (new BeaconPlatformBox(location.clone().subtract(3, 3, 3), location.clone().subtract(-3, 3, -3)).isValidTier())
			tier++;
		if (new BeaconPlatformBox(location.clone().subtract(4, 4, 4), location.clone().subtract(-4, 4, -4)).isValidTier())
			tier++;
		
		return Math.pow(20 + tier * 20, 2);
	}
	
	public Location getLocation() {
		return location;
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public PotionEffect getBuff1() {
		return buff1;
	}
	
	public PotionEffect getBuff2() {
		return buff2;
	}
	
	public PotionEffect getDebuff() {
		return debuff;
	}
	
	public long getExpireTime() {
		return expireTime;
	}
	
	private class BeaconPlatformBox {
		private Location minLocation;
		private Location maxLocation;
		private World world;
		
		private BeaconPlatformBox(Location minLocation, Location maxLocation) {
			this.minLocation = minLocation;
			this.maxLocation = maxLocation;
			this.world = maxLocation.getWorld();
		}
		
		private boolean isValidTier() {
			
			List<Location> list = new ArrayList<>();
			double minX = minLocation.getX(), minY = minLocation.getY(), minZ = minLocation.getZ();
			double maxX = maxLocation.getX(), maxZ = maxLocation.getZ();
			
			for (; minX <= maxX; minX++)
				for (; minZ <= maxZ; minZ++) {
					list.add(new Location(world, minX, minY, minZ));
				}
			
			return list.stream().allMatch(location -> isBeaconActivateBlock(location.getBlock().getType()));
		}
		
		private boolean isBeaconActivateBlock(Material material) {
			switch (material) {
				case DIAMOND_BLOCK:
				case IRON_BLOCK:
				case EMERALD_BLOCK:
				case GOLD_BLOCK:
					return true;
				default:
					return false;
			}
		}
		
		
	}
	
	
}
