package me.sizzlemcgrizzle.superblocks.beacon;

import de.craftlancer.clclans.CLClans;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBeacon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class BeaconEffect extends BukkitRunnable {
	SuperBlocksPlugin superBlocks = (SuperBlocksPlugin) Bukkit.getPluginManager().getPlugin("SuperBlocks");
	CLClans clans = (CLClans) Bukkit.getPluginManager().getPlugin("CLClans");
	SuperBeacon superBeacon = new SuperBeacon();

	@Override
	public void run() {
		List<BeaconData> beacons = superBlocks.getBeacons();

		for (Player player : Bukkit.getOnlinePlayers())
			for (BeaconData beacon : beacons) {
				if (!superBeacon.isBeaconActive(beacon.getLocation()))
					continue;
				if (System.currentTimeMillis() > beacon.getExpireTime())
					continue;
				if (player.getLocation().distance(beacon.getLocation()) >= 50 || !player.getLocation().getWorld().equals(beacon.getLocation().getWorld()))
					continue;

				if (isClanMember(beacon.getOwner(), player.getUniqueId())) {
					if (beacon.getBuff1() != null)
						player.addPotionEffect(beacon.getBuff1());
					if (beacon.getBuff2() != null)
						player.addPotionEffect(beacon.getBuff2());
				}
				if (isEnemy(beacon.getOwner(), player.getUniqueId())) {
					if (beacon.getDebuff() != null)
						player.addPotionEffect(beacon.getDebuff());
				}

			}
	}

	private boolean isClanMember(UUID uuid, UUID playerUUID) {
		if (uuid.equals(playerUUID))
			return true;
		if (clans.getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || clans.getClan(Bukkit.getOfflinePlayer(uuid)) == null)
			return false;
		return clans.getClan(Bukkit.getOfflinePlayer(uuid)).equals(clans.getClan(Bukkit.getOfflinePlayer(playerUUID)));
	}

	private boolean isEnemy(UUID uuid, UUID playerUUID) {
		if (clans.getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || clans.getClan(Bukkit.getOfflinePlayer(uuid)) == null)
			return false;
		return clans.getClan(Bukkit.getOfflinePlayer(playerUUID)).hasRival(clans.getClan(Bukkit.getOfflinePlayer(uuid)));
	}
}
