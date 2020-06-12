package me.sizzlemcgrizzle.superblocks;


import me.sizzlemcgrizzle.superblocks.beacon.BeaconData;
import me.sizzlemcgrizzle.superblocks.beacon.BeaconEffect;
import me.sizzlemcgrizzle.superblocks.commands.SuperBlocksCommandGroup;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBeacon;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBell;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBlockListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SuperBlocksPlugin extends SimplePlugin {
    private static Economy econ = null;
    
    private List<BeaconData> beacons = new ArrayList<>();
    private List<Location> superBlockLocations = new ArrayList<>();
    private List<Location> superBellOnlocations = new ArrayList<>();
    private File folder = new File(this.getDataFolder(), "Data");
    
    private SuperBell superBell;
    private SuperBeacon superBeacon;
    
    @Override
    public void onPluginStart() {
        
        if (Settings.USE_ECONOMY)
            setupEconomy();
        
        if (!folder.exists()) {
            folder.mkdir();
        }
        
        superBell = new SuperBell(ChatColor.GOLD + "" + ChatColor.BOLD + "Player Radar",
                Arrays.asList(ChatColor.GRAY + "Place this radar down", ChatColor.GRAY + "and right click to", ChatColor.GRAY + "scan for players."),
                Material.BELL);
        superBeacon = new SuperBeacon(ChatColor.AQUA + "" + ChatColor.ITALIC + "Amplified Beacon",
                Arrays.asList(ChatColor.GRAY + "Place this beacon down", ChatColor.GRAY + "and right click to", ChatColor.GRAY + "edit properties"),
                Material.BEACON);
        
        registerEvents(new SuperBlockListener(this));
        
        registerCommands("superblocks", new SuperBlocksCommandGroup());
        
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
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    private void activateBeaconTimer() {
        new BeaconEffect(this).runTaskTimer(this, 0, 160);
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
    
    public SuperBeacon getSuperBeacon() {
        return superBeacon;
    }
    
    public SuperBell getSuperBell() {
        return superBell;
    }
    
    public static Economy getEconomy() {
        return econ;
    }
}