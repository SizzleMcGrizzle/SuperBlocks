package me.sizzlemcgrizzle.superblocks.beacon;

import de.craftlancer.clclans.CLClans;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBeacon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.Common;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BeaconEffects {
	private int repeat;
	private int delay;
	private File file;
	private YamlConfiguration config;
	private CLClans clans = (CLClans) Bukkit.getPluginManager().getPlugin("CLClans");
	private SuperBeacon superBeacon;

	public BeaconEffects(int repeat, int delay) {
		this.repeat = repeat;
		this.delay = delay;
		this.superBeacon = new SuperBeacon();

		runTimer();
	}

	private void runTimer() {
		Runnable runnable = () -> {
			try {
				this.file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/beaconsettings.yml");
				this.config = new YamlConfiguration();
				if (!file.exists())
					file.createNewFile();
				config.load(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}


			for (Player player : Bukkit.getOnlinePlayers()) {
				for (String beacon : config.getKeys(false)) {
					if (!superBeacon.isBeaconActive(deserializeLocation(beacon)))
						continue;
					if (System.currentTimeMillis() > config.getConfigurationSection(beacon).getLong("expires"))
						continue;
					if (player.getLocation().distance(deserializeLocation(beacon)) <= 50)
						applyEffects(player, beacon);
				}
			}
		};
		Common.runTimer(delay, Math.toIntExact(repeat), runnable);
	}

	private void applyEffects(Player player, String key) {
		String buff1 = config.getConfigurationSection(key).getString("buff1");
		String buff2 = config.getConfigurationSection(key).getString("buff2");
		String debuff = config.getConfigurationSection(key).getString("debuff");
		UUID uuid = UUID.fromString(config.getConfigurationSection(key).getString("playerUUID"));
		UUID playerUUID = player.getUniqueId();
		List<String> list = Arrays.asList(buff1, buff2, debuff);
		for (String string : list)
			switch (string) {
				case "regen":
					if (!isClanMember(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, repeat, 0));
					break;
				case "strength":
					if (!isClanMember(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, repeat, 1));
					break;
				case "speed":
					if (!isClanMember(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, repeat, 1));
					break;
				case "haste":
					if (!isClanMember(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, repeat, 1));
					break;
				case "resistance":
					if (!isClanMember(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, repeat, 1));
					break;
				case "jumpboost":
					if (!isClanMember(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, repeat, 1));
					break;
				case "fireres":
					if (!isClanMember(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, repeat, 0));
					break;
				case "waterbreath":
					if (!isClanMember(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, repeat, 0));
					break;
				case "minefatigue":
					if (!isEnemy(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, repeat, 0));
					break;
				case "slowness":
					if (!isEnemy(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, repeat, 0));
					break;
				case "weakness":
					if (!isEnemy(uuid, playerUUID)) break;
					player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, repeat, 0));
					break;
			}


	}

	private Location deserializeLocation(String serializedLocation) {
		String[] array = serializedLocation.split("&-&");
		return new Location(Bukkit.getServer().getWorld(array[3]), Double.parseDouble(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]));
	}

	private boolean isClanMember(UUID uuid, UUID playerUUID) {
		if (uuid.equals(playerUUID))
			return true;
		if (clans.getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || clans.getClan(Bukkit.getOfflinePlayer(uuid)) == null)
			return false;
		return clans.getClan(Bukkit.getOfflinePlayer(uuid)).hasMember(Bukkit.getOfflinePlayer(playerUUID));
	}

	private boolean isEnemy(UUID uuid, UUID playerUUID) {
		if (clans.getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || clans.getClan(Bukkit.getOfflinePlayer(uuid)) == null)
			return false;
		return clans.getClan(Bukkit.getOfflinePlayer(playerUUID)).hasRival(clans.getClan(Bukkit.getOfflinePlayer(uuid)));
	}

}
