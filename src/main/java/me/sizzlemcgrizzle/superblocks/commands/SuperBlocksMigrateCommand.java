package me.sizzlemcgrizzle.superblocks.commands;

import me.sizzlemcgrizzle.superblocks.SuperBeacon;
import me.sizzlemcgrizzle.superblocks.SuperBell;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import me.sizzlemcgrizzle.superblocks.util.SuperBlocksUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class SuperBlocksMigrateCommand extends SimpleSubCommand {
    protected SuperBlocksMigrateCommand(SimpleCommandGroup parent) {
        super(parent, "migrate");
        setPermission("superblocks.migrate");
    }
    
    @Override
    protected void onCommand() {
        checkConsole();
        
        tell(Settings.PREFIX + "&eMigrating old files to new... please wait...");
        getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, 0.8F);
        
        migrateBeacons();
        migrateBells();
        
        Runnable runnable = () -> {
            tell(Settings.PREFIX + "&aMigration successful! Please confirm locations are valid before deleting any files.");
            getPlayer().playSound(getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, 1.8F);
        };
        
        Common.runLater(60, runnable);
    }
    
    private void migrateBeacons() {
        File file = new File(SuperBlocksPlugin.instance.getDataFolder() + File.separator + "/Data" + File.separator + "/beacons.yml");
        
        if (!file.exists())
            return;
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        for (String serializedLocation : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(serializedLocation);
            String[] array = serializedLocation.split("&-&");
            Location location = new Location(Bukkit.getServer().getWorld(array[3]), Double.parseDouble(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]));
            
            SuperBeacon beacon = new SuperBeacon(Collections.singletonList(location), UUID.fromString(section.getString("playerUUID")));
            
            beacon.setBuff1(SuperBlocksUtil.getPotionFromString(section.getString("buff1")));
            beacon.setBuff2(SuperBlocksUtil.getPotionFromString(section.getString("buff2")));
            beacon.setDebuff(SuperBlocksUtil.getPotionFromString(section.getString("debuff")));
            beacon.setExpireTime(section.getLong("expires"));
            
            SuperBlocksPlugin.instance.addSuperBlock(beacon);
        }
        
    }
    
    private void migrateBells() {
        File file = new File(SuperBlocksPlugin.instance.getDataFolder() + File.separator + "/Data" + File.separator + "/bells.yml");
        
        if (!file.exists())
            return;
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        for (String serializedLocation : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(serializedLocation);
            
            String[] array = serializedLocation.split("&-&");
            String[] array2 = section.getString("placedOn").split("&-&");
            
            Location bellLocation = new Location(Bukkit.getServer().getWorld(array[3]), Double.parseDouble(array[0]), Double.parseDouble(array[1]), Double.parseDouble(array[2]));
            Location placedOnLocation = new Location(Bukkit.getServer().getWorld(array2[3]), Double.parseDouble(array2[0]), Double.parseDouble(array2[1]), Double.parseDouble(array2[2]));
            
            SuperBell bell = new SuperBell(Arrays.asList(bellLocation, placedOnLocation), UUID.randomUUID());
            
            SuperBlocksPlugin.instance.addSuperBlock(bell);
        }
    }
}
