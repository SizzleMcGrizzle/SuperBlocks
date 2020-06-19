package me.sizzlemcgrizzle.superblocks.beacon;

import de.craftlancer.clclans.CLClans;
import me.sizzlemcgrizzle.superblocks.SuperBeacon;
import me.sizzlemcgrizzle.superblocks.SuperBlock;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.stream.Collectors;

public class BeaconEffect extends BukkitRunnable {
    private SuperBlocksPlugin plugin;
    private CLClans clans = (CLClans) Bukkit.getPluginManager().getPlugin("CLClans");
    
    private SuperBeacon superBeacon;
    
    public BeaconEffect(SuperBlocksPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        for (SuperBlock block : plugin.getSuperBlocks().stream().filter(block -> block instanceof SuperBeacon).filter(block -> ((SuperBeacon) block).isActive()).collect(Collectors.toList())) {
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
        }
    }
    
    private boolean isClanMember(UUID uuid, UUID playerUUID) {
        if (uuid.equals(playerUUID))
            return true;
        if (clans.getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || clans.getClan(Bukkit.getOfflinePlayer(uuid)) == null)
            return false;
        return clans.getClan(Bukkit.getOfflinePlayer(uuid)).equals(clans.getClan(Bukkit.getOfflinePlayer(playerUUID)));
    }
    
    private boolean isEnemy(UUID uuid, UUID playerUUID) {
        if (clans.getClan(Bukkit.getOfflinePlayer(playerUUID)) == null || clans.getClan(Bukkit.getOfflinePlayer(uuid)) == null)
            return false;
        return clans.getClan(Bukkit.getOfflinePlayer(playerUUID)).hasRival(clans.getClan(Bukkit.getOfflinePlayer(uuid)));
    }
    
    private double getHorizontalDistanceSquared(Location loc1, Location loc2) {
        return Math.pow(Math.abs(loc1.getX() - loc2.getX()), 2) + Math.pow(Math.abs(loc1.getZ() - loc2.getZ()), 2);
    }
}
