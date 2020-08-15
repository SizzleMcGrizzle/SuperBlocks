package me.sizzlemcgrizzle.superblocks;


import de.craftlancer.clclans.CLClans;
import de.craftlancer.core.LambdaRunnable;
import me.sizzlemcgrizzle.superblocks.commands.SuperBlocksCommandGroup;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SuperBlocksPlugin extends SimplePlugin {
    public static SuperBlocksPlugin instance;
    private static Economy econ = null;
    
    private File superBlocksFile;
    private List<SuperBlock> superBlocks = new ArrayList<>();
    
    @Override
    public void onPluginStart() {
        
        ConfigurationSerialization.registerClass(SuperBeacon.class);
        ConfigurationSerialization.registerClass(SuperBell.class);
        
        instance = this;
        
        if (Settings.USE_ECONOMY)
            setupEconomy();
        
        superBlocksFile = new File(this.getDataFolder(), "superBlocks.yml");
        
        registerEvents(new SuperBlockListener(this));
        
        registerCommands("superblocks", new SuperBlocksCommandGroup());
        
        activateBeaconTimer();
        registerSuperBlocks();
    }
    
    @Override
    protected void onPluginStop() {
        serializeSuperBlocks();
    }
    
    @Override
    protected void onPluginReload() {
        activateBeaconTimer();
    }
    
    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Arrays.asList(Settings.class);
    }
    
    private void registerSuperBlocks() {
        
        if (!superBlocksFile.exists())
            FileUtil.extract("superBlocks.yml");
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(superBlocksFile);
        
        superBlocks.addAll((List<SuperBeacon>) config.get("beacons"));
        superBlocks.addAll((List<SuperBell>) config.get("bells"));
    }
    
    private void serializeSuperBlocks() {
        Common.log("Serializing super blocks for shutdown...");
        
        if (!superBlocksFile.exists())
            FileUtil.extract("superBlocks.yml");
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(superBlocksFile);
        
        config.set("beacons", superBlocks.stream().filter(block -> block instanceof SuperBeacon).collect(Collectors.toList()));
        config.set("bells", superBlocks.stream().filter(block -> block instanceof SuperBell).collect(Collectors.toList()));
        
        try {
            config.save(superBlocksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        new LambdaRunnable(() -> getSuperBlocks().stream().filter(block -> block instanceof SuperBeacon).filter(block -> ((SuperBeacon) block).isActive()).forEach(block -> {
            SuperBeacon beacon = (SuperBeacon) block;
            Location location = beacon.getStructure().get(0);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getLocation().getWorld() != location.getWorld() || getHorizontalDistanceSquared(player.getLocation(), location) >= beacon.getRange())
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
        })).runTaskTimer(this, 0, 160);
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
    
    private boolean isClanMember(UUID uuid, UUID playerUUID) {
        if (uuid.equals(playerUUID))
            return true;
        if (CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(uuid)) == null)
            return false;
        return CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(uuid)).equals(CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(playerUUID)));
    }
    
    private boolean isEnemy(UUID uuid, UUID playerUUID) {
        if (CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(uuid)) == null)
            return false;
        return CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(playerUUID)).hasRival(CLClans.getInstance().getClan(Bukkit.getOfflinePlayer(uuid)));
    }
    
    private double getHorizontalDistanceSquared(Location loc1, Location loc2) {
        return Math.pow(Math.abs(loc1.getX() - loc2.getX()), 2) + Math.pow(Math.abs(loc1.getZ() - loc2.getZ()), 2);
    }
}