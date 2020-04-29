package me.sizzlemcgrizzle.superblocks.superblocks;

import lombok.NonNull;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public abstract class SuperBlock {
	
	public SuperBlock() {
	}
	
	public abstract ItemStack getItem();
	
	public abstract boolean isSuperBlock(@NonNull ItemStack item);
	
	public abstract void doFunction(Player player, Location location);
	
	
	public void serialize(Location location, String type) {
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/superblocks.yml");
		YamlConfiguration config = new YamlConfiguration();
		
		try {
			if (!file.exists())
				file.createNewFile();
			
			config.load(file);
			
			String serializedLocation = (int) location.getX() + "&-&" + (int) location.getY() + "&-&" + (int) location.getZ() + "&-&" + location.getWorld().getName();
			
			config.set(serializedLocation, type);
			
			config.save(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	
	public void removeFromFile(Location location) {
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/superblocks.yml");
		YamlConfiguration config = new YamlConfiguration();
		
		try {
			if (!file.exists())
				file.createNewFile();
			
			config.load(file);
			
			String serializedLocation = (int) location.getX() + "&-&" + (int) location.getY() + "&-&" + (int) location.getZ() + "&-&" + location.getWorld().getName();
			
			config.set(serializedLocation, null);
			
			config.save(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	
}
