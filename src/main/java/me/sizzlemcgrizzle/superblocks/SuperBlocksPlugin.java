package me.sizzlemcgrizzle.superblocks;


import de.craftlancer.clclans.CLClans;
import me.sizzlemcgrizzle.superblocks.commands.SuperBlocksCommandHandler;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SuperBlocksPlugin extends JavaPlugin {
    public static SuperBlocksPlugin instance;
    private static Economy econ = null;
    
    private File superBlocksFile;
    private List<SuperBlock> superBlocks = new ArrayList<>();
    
    @Override
    public void onEnable() {
        
        ConfigurationSerialization.registerClass(SuperBeacon.class);
        ConfigurationSerialization.registerClass(SuperBell.class);
        
        instance = this;
        
        Settings.load(this);
        
        superBlocksFile = new File(this.getDataFolder(), "superBlocks.yml");
        
        Bukkit.getPluginManager().registerEvents(new SuperBlockListener(this), this);
        
        getCommand("superblocks").setExecutor(new SuperBlocksCommandHandler(this));
        
        registerSuperBlocks();
    }
    
    @Override
    public void onDisable() {
        serializeSuperBlocks();
    }
    
    private void registerSuperBlocks() {
        
        if (!superBlocksFile.exists())
            saveResource(superBlocksFile.getName(), false);
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(superBlocksFile);
        
        superBlocks.addAll((List<SuperBeacon>) config.get("beacons"));
        superBlocks.addAll((List<SuperBell>) config.get("bells"));
    }
    
    private void serializeSuperBlocks() {
        
        if (!superBlocksFile.exists())
            saveResource(superBlocksFile.getName(), false);
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(superBlocksFile);
        
        config.set("beacons", superBlocks.stream().filter(block -> block instanceof SuperBeacon).collect(Collectors.toList()));
        config.set("bells", superBlocks.stream().filter(block -> block instanceof SuperBell).collect(Collectors.toList()));
        
        try {
            config.save(superBlocksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<SuperBlock> getSuperBlocks() {
        return superBlocks;
    }
    
    public void removeSuperBlock(SuperBlock block) {
        superBlocks.remove(block);
    }
    
    public void addSuperBlock(SuperBlock block) {
        superBlocks.add(block);
    }
    
    
    public static Economy getEconomy() {
        return econ;
    }
    
    public boolean isClanMember(UUID uuid, UUID playerUUID) {
        if (uuid.equals(playerUUID))
            return true;
        if (CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(uuid)) == null)
            return false;
        return CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(uuid)).equals(CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(playerUUID)));
    }
    
    public boolean isEnemy(UUID uuid, UUID playerUUID) {
        if (CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(uuid)) == null)
            return false;
        return CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(uuid)).hasRival(CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(playerUUID)));
    }
    
    public double getHorizontalDistanceSquared(Location loc1, Location loc2) {
        return Math.pow(Math.abs(loc1.getX() - loc2.getX()), 2) + Math.pow(Math.abs(loc1.getZ() - loc2.getZ()), 2);
    }
}