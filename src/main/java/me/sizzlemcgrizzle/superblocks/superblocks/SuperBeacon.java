package me.sizzlemcgrizzle.superblocks.superblocks;

import lombok.NonNull;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.beacon.BeaconPreferencesMenu;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SuperBeacon extends SuperBlock {
	private SuperBlocksPlugin superBlocks = (SuperBlocksPlugin) Bukkit.getPluginManager().getPlugin("SuperBlocks");
	private String name = ChatColor.AQUA + "" + ChatColor.ITALIC + "Amplified Beacon";
	private List<String> lore = Arrays.asList(ChatColor.GRAY + "Place this beacon down", ChatColor.GRAY + "and right click to", ChatColor.GRAY + "edit properties");

	public SuperBeacon() {
	}

	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(CompMaterial.BEACON.getMaterial());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public boolean isSuperBlock(@NonNull ItemStack item) {
		return (item.getItemMeta().getDisplayName().equals(name) && Objects.equals(item.getItemMeta().getLore(), lore));
	}

	@Override
	public void doFunction(Player player, Location location) throws IOException, InvalidConfigurationException {
		if (isBeaconActive(location))
			new BeaconPreferencesMenu(location).displayTo(player);
		else {
			Common.tell(player, Settings.PREFIX + "&cYou must activate this beacon! &e&o(There must be a beam and a 3x3 grid of blocks under like a normal beacon)");
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 0.5F);
			Runnable runnable = player::closeInventory;
			Common.runLater(1, runnable);
		}


	}

	public void serializeSettings(Location location, Player player) throws IOException, InvalidConfigurationException {
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/beacons.yml");
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists())
			file.createNewFile();

		config.load(file);

		String serializedLocation = (int) location.getX() + "&-&" + (int) location.getY() + "&-&" + (int) location.getZ() + "&-&" + location.getWorld().getName();

		config.createSection(serializedLocation);
		config.getConfigurationSection(serializedLocation).set("buff1", "none");
		config.getConfigurationSection(serializedLocation).set("buff2", "none");
		config.getConfigurationSection(serializedLocation).set("debuff", "none");
		config.getConfigurationSection(serializedLocation).set("expires", 0);
		config.getConfigurationSection(serializedLocation).set("playerUUID", player.getUniqueId().toString());
		config.save(file);

	}

	public void removeSetting(Location location) throws IOException, InvalidConfigurationException {
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/beacons.yml");
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists())
			file.createNewFile();

		config.load(file);

		String serializedLocation = (int) location.getX() + "&-&" + (int) location.getY() + "&-&" + (int) location.getZ() + "&-&" + location.getWorld().getName();

		config.set(serializedLocation, null);
		config.save(file);
	}

	/*
	 *   Is the beacon chunk loaded?
	 *   Are there any non-transparent blocks above it?
	 *   Does it have a 3x3 of iron/diamond/emerald/gold blocks under it?
	 */
	public boolean isBeaconActive(Location location) {
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		World world = location.getWorld();

		if (!world.isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4))
			return false;

		if (!isBeaconActivateBlock(new Location(world, x, y - 1, z).getBlock().getType())) return false;
		if (!isBeaconActivateBlock(new Location(world, x, y - 1, z - 1).getBlock().getType())) return false;
		if (!isBeaconActivateBlock(new Location(world, x - 1, y - 1, z - 1).getBlock().getType())) return false;
		if (!isBeaconActivateBlock(new Location(world, x + 1, y - 1, z - 1).getBlock().getType())) return false;
		if (!isBeaconActivateBlock(new Location(world, x - 1, y - 1, z + 1).getBlock().getType())) return false;
		if (!isBeaconActivateBlock(new Location(world, x + 1, y - 1, z + 1).getBlock().getType())) return false;
		if (!isBeaconActivateBlock(new Location(world, x + 1, y - 1, z).getBlock().getType())) return false;
		if (!isBeaconActivateBlock(new Location(world, x - 1, y - 1, z).getBlock().getType())) return false;
		if (!isBeaconActivateBlock(new Location(world, x, y - 1, z + 1).getBlock().getType())) return false;


		for (int i = (int) y + 1; i <= 255; i++) {
			Material material = (new Location(world, x, i, z).getBlock().getType());
			if (material.isAir())
				continue;
			if (occludingPassThrough(material).equals("true"))
				continue;
			if (occludingPassThrough(material).equals("false"))
				return false;
			if (material.isOccluding())
				return false;

		}
		return true;
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

	private String occludingPassThrough(Material material) {
		if (Settings.PASSTHROUGH_BLOCKS_MATERIAL.contains(material))
			return "true";
		switch (material) {
			case PISTON:
			case GLOWSTONE:
			case REDSTONE_LAMP:
			case OBSERVER:
				return "false";
			default:
				return "null";
		}
	}


}
