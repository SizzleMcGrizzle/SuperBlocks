package me.sizzlemcgrizzle.superblocks.superblocks;

import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.beacon.BeaconPreferencesMenu;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SuperBeacon extends SuperBlock {
    
    public SuperBeacon(String name, List<String> lore, Material material) {
        super(name, lore, material);
    }
    
    @Override
    public void doFunction(Player player, Location location) {
        if (isBeaconActive(location))
            new BeaconPreferencesMenu(location).displayTo(player);
        else {
            Common.tell(player, Settings.PREFIX + "&cYou must activate this beacon! &e&o(There must be a beam and a 3x3 grid of blocks under like a normal beacon)");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 0.5F);
            Runnable runnable = player::closeInventory;
            Common.runLater(1, runnable);
        }
        
        
    }
    
    public void serializeSettings(Location location, Player player) {
        File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/beacons.yml");
        YamlConfiguration config = new YamlConfiguration();
        
        try {
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
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        
    }
    
    public void removeSetting(Location location) {
        File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/beacons.yml");
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
    
    /*
     *   Is the beacon chunk loaded?
     *   Are there any non-transparent blocks above it?
     *   Does it have a 3x3 of iron/diamond/emerald/gold blocks under it?
     */
    public boolean isBeaconActive(Location beaconLocation) {
        double x = beaconLocation.getX();
        double y = beaconLocation.getY();
        double z = beaconLocation.getZ();
        World world = beaconLocation.getWorld();
        if (world == null || !world.isChunkLoaded(beaconLocation.getBlockX() >> 4, beaconLocation.getBlockZ() >> 4))
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
