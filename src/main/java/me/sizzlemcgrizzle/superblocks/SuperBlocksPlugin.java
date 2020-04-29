package me.sizzlemcgrizzle.superblocks;


import me.sizzlemcgrizzle.superblocks.beacon.BeaconData;
import me.sizzlemcgrizzle.superblocks.beacon.BeaconEffect;
import me.sizzlemcgrizzle.superblocks.commands.SuperBlocksCommandGroup;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBlockListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SuperBlocksPlugin extends SimplePlugin {
    private List<BeaconData> beacons = new ArrayList<>();
    private List<Location> superBlockLocations = new ArrayList<>();
    private List<Location> superBellOnlocations = new ArrayList<>();
    private File folder = new File(this.getDataFolder(), "Data");
    
    @Override
    public void onPluginStart() {
        registerEvents(new SuperBlockListener());
        
        registerCommands("superblocks", new SuperBlocksCommandGroup());
        
        if (!folder.exists()) {
            folder.mkdir();
        }
        cacheSuperBlockLocations();
        activateBeaconTimer();
    }
    
    @Override
    protected void onPluginReload() {
        if (!folder.exists()) {
            folder.mkdir();
        }
        cacheSuperBlockLocations();
        activateBeaconTimer();
    }
    
    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Settings.class);
    }
    
    private void activateBeaconTimer() {
        new BeaconEffect().runTaskTimer(this, 0, 160);
    }
    
    
    /*
     *   Beacon utility methods
     */
    public void cacheBeacons() {
        File file = new File(this.getDataFolder() + File.separator + "/Data/beacons.yml");
        YamlConfiguration config = new YamlConfiguration();
        
        beacons.clear();
        
        try {
            if (!file.exists())
                return;
            config.load(file);
            
            for (String key : config.getKeys(false)) {
                Location location = deserializeLocation(key);
                String uuid = config.getConfigurationSection(key).getString("playerUUID");
                String buff1 = config.getConfigurationSection(key).getString("buff1");
                String buff2 = config.getConfigurationSection(key).getString("buff2");
                String debuff = config.getConfigurationSection(key).getString("debuff");
                long expires = config.getConfigurationSection(key).getLong("expires");
                
                if (uuid == null || debuff == null || buff1 == null || buff2 == null)
                    continue;
                beacons.add(new BeaconData(location, UUID.fromString(uuid), buff1, buff2, debuff, expires));
            }
            
            cacheSuperBellOnBlocks();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void cacheSuperBlockLocations() {
        File file = new File(this.getDataFolder() + File.separator + "/Data/superblocks.yml");
        YamlConfiguration config = new YamlConfiguration();
        try {
            superBlockLocations.clear();
            if (!file.exists())
                file.createNewFile();
            config.load(file);
            
            for (String key : config.getKeys(false))
                superBlockLocations.add(deserializeLocation(key));
            
            cacheBeacons();
            
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void cacheSuperBellOnBlocks() {
        File bellFile = new File(this.getDataFolder(), "/Data/bells.yml");
        YamlConfiguration config = new YamlConfiguration();
        
        superBellOnlocations.clear();
        
        try {
            if (!bellFile.exists())
                bellFile.createNewFile();
            config.load(bellFile);
            
            for (String key : config.getKeys(false))
                superBellOnlocations.add(deserializeLocation(config.getConfigurationSection(key).getString("placedOn")));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    private Location deserializeLocation(String serializedLocation) {
        String[] array = serializedLocation.split("&-&");
        return new Location(Bukkit.getServer().getWorld(array[3]), Double.parseDouble(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]));
    }
    
    public List<Location> getSuperBellOnLocations() {
        return superBellOnlocations;
    }
    
    public List<Location> getSuperBlockLocations() {
        return superBlockLocations;
    }
    
    public List<BeaconData> getBeacons() {
        return beacons;
    }
    
    
}