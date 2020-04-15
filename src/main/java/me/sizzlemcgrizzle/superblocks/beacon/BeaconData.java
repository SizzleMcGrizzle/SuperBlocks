package me.sizzlemcgrizzle.superblocks.beacon;

import me.sizzlemcgrizzle.superblocks.util.SuperBlocksUtil;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;

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


}
