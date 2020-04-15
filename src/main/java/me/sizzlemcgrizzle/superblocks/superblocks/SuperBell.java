package me.sizzlemcgrizzle.superblocks.superblocks;

import de.craftlancer.clclans.CLClans;
import lombok.NonNull;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SuperBell extends SuperBlock {
	private CLClans clans = (CLClans) Bukkit.getPluginManager().getPlugin("CLClans");


	private String name = ChatColor.GOLD + "" + ChatColor.BOLD + "Player Radar";
	private List<String> lore = Arrays.asList(ChatColor.GRAY + "Place this radar down", ChatColor.GRAY + "and right click to", ChatColor.GRAY + "scan for players.");
	private HashMap<Player, Long> cooldownMap = new HashMap<>();

	public SuperBell() {
	}

	/*
	 *** Make a new item and apply name and lore to it, then return it.
	 */
	@Override
	public ItemStack getItem() {
		ItemStack item = new ItemStack(CompMaterial.BELL.getMaterial());
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);

		return item;
	}

	/*
	 *** Validate that this item is a super bell.
	 */
	@Override
	public boolean isSuperBlock(@NonNull ItemStack item) {
		return (item.getItemMeta().getDisplayName().equals(name) && Objects.equals(item.getItemMeta().getLore(), lore));
	}

	@Override
	public void doFunction(Player player, Location location) {
		if (cooldownMap.containsKey(player)) {
			if (cooldownMap.get(player) + 60000L >= System.currentTimeMillis()) {
				Common.tell(player, "&f[&4Craft&fCitizen]&e You must wait &c" + (cooldownMap.get(player) + 60000L - System.currentTimeMillis()) / 1000L + " seconds&e to use this again.");
				return;
			}

			this.cooldownMap.remove(player);
		}

		player.playSound(location, Sound.BLOCK_BELL_RESONATE, 1.0F, 1.0F);
		Common.tell(player, "&aScanning...");
		cooldownMap.put(player, System.currentTimeMillis());
		Runnable runnable = () -> {
			int clan = 0;
			int neutral = 0;
			int rival = 0;

			for (Player p : Bukkit.getOnlinePlayers()) {
				if (!p.equals(player) && location.distance(p.getLocation()) <= 200.0D) {
					if (clans.getClan(p) == null || clans.getClan(Bukkit.getOfflinePlayer(player.getUniqueId())) == null) {
						++neutral;
					} else if (clans.getClan(Bukkit.getOfflinePlayer(p.getUniqueId())).hasRival(clans.getClan(Bukkit.getOfflinePlayer(player.getUniqueId())))) {
						++rival;
					} else if (clans.getClan(p).equals(clans.getClan(player))) {
						++clan;
					} else {
						++neutral;
					}
				}
			}

			if (clan == 0 && neutral == 0 && rival == 0) {
				Common.tell(player, "&aThere are no players nearby.");
			} else {
				Common.tell(player, "&f[&4Craft&fCitizen] &eThere are &2" + clan + " clan members&e, &4" + rival + " rivals&e, and &f" + neutral + " players &enearby.");
			}

		};
		Common.runLater(60, runnable);

	}

	public void serializeSettings(Location bell, Location placedOn) throws IOException, InvalidConfigurationException {
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/bells.yml");
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists())
			file.createNewFile();

		config.load(file);

		String serializeBell = (int) bell.getX() + "&-&" + (int) bell.getY() + "&-&" + (int) bell.getZ() + "&-&" + bell.getWorld().getName();
		String serializePlacedOn = (int) placedOn.getX() + "&-&" + (int) placedOn.getY() + "&-&" + (int) placedOn.getZ() + "&-&" + placedOn.getWorld().getName();

		config.createSection(serializeBell);
		config.getConfigurationSection(serializeBell).set("placedOn", serializePlacedOn);
		config.save(file);
	}

	public void removeSetting(Location bell) throws IOException, InvalidConfigurationException {
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/bells.yml");
		YamlConfiguration config = new YamlConfiguration();

		if (!file.exists())
			file.createNewFile();

		config.load(file);

		String serializedLocation = (int) bell.getX() + "&-&" + (int) bell.getY() + "&-&" + (int) bell.getZ() + "&-&" + bell.getWorld().getName();

		config.set(serializedLocation, null);
		config.save(file);
	}
}
