package me.sizzlemcgrizzle.superblocks.superblocks;

import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.events.CraftDetectEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SuperBlockListener implements Listener {
    private SuperBlocksPlugin superBlocks = (SuperBlocksPlugin) Bukkit.getPluginManager().getPlugin("SuperBlocks");
    private SuperBell superBell = new SuperBell();
    private SuperBeacon superBeacon = new SuperBeacon();
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) throws IOException, InvalidConfigurationException {
        ItemStack item = event.getItemInHand();
        Location location = event.getBlockPlaced().getLocation();
        
        if (superBell.isSuperBlock(item) && item.getType().equals(CompMaterial.BELL.getMaterial())) {
            superBell.serialize(location, "bell");
            superBell.serializeSettings(location, event.getBlockAgainst().getLocation());
        }
        
        if (superBeacon.isSuperBlock(item) && item.getType().equals(CompMaterial.BEACON.getMaterial())) {
            superBeacon.serialize(location, "beacon");
            superBeacon.serializeSettings(location, event.getPlayer());
        }
        superBlocks.cacheSuperBlockLocations();
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) throws IOException, InvalidConfigurationException {
        Material material = event.getBlock().getType();
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();
        
        if (superBlocks.getSuperBellOnLocations().contains(location)) {
            event.setCancelled(true);
            return;
        }
        
        if (superBlocks.getSuperBlockLocations().contains(location)) {
            event.setDropItems(false);
            if (CompMaterial.fromMaterial(material).is(CompMaterial.BELL.getMaterial())) {
                if (!player.getGameMode().equals(GameMode.CREATIVE))
                    location.getWorld().dropItemNaturally(location, superBell.getItem());
                superBell.removeFromFile(location);
                superBell.removeSetting(location);
            }
            if (CompMaterial.fromMaterial(material).is(CompMaterial.BEACON.getMaterial())) {
                if (!player.getGameMode().equals(GameMode.CREATIVE))
                    location.getWorld().dropItemNaturally(location, superBeacon.getItem());
                superBeacon.removeFromFile(location);
                superBeacon.removeSetting(location);
            }
            superBlocks.cacheSuperBlockLocations();
        }
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onInteractEvent(PlayerInteractEvent event) throws IOException, InvalidConfigurationException {
        if (event.getClickedBlock() == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        Material material = event.getClickedBlock().getType();
        Player player = event.getPlayer();
        Location location = event.getClickedBlock().getLocation();
        
        if (superBlocks.getSuperBlockLocations().contains(location)) {
            if (CompMaterial.fromMaterial(material).is(CompMaterial.BELL.getMaterial()))
                superBell.doFunction(player, location);
            if (CompMaterial.fromMaterial(material).is(CompMaterial.BEACON.getMaterial())) {
                if (!(player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) && player.isSneaking()) {
                    return;
                } else if (!player.isSneaking() && (player.getInventory().getItemInMainHand().getType() == null)) {
                    event.setCancelled(true);
                    superBeacon.doFunction(player, location);
                } else {
                    event.setCancelled(true);
                    superBeacon.doFunction(player, location);
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onExplosion(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> superBlocks.getSuperBlockLocations().contains(block.getLocation()) || superBlocks.getSuperBellOnLocations().contains(block.getLocation()));
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks())
            if (superBlocks.getSuperBlockLocations().contains(block.getLocation()) || superBlocks.getSuperBellOnLocations().contains(block.getLocation()))
                event.setCancelled(true);
        
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks())
            if (superBlocks.getSuperBlockLocations().contains(block.getLocation()) || superBlocks.getSuperBellOnLocations().contains(block.getLocation()))
                event.setCancelled(true);
        
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockShoot(ProjectileHitEvent event) {
        if (event.getHitBlock() == null || !(event.getEntity().getShooter() instanceof Player))
            return;
        if (superBlocks.getSuperBlockLocations().contains(event.getHitBlock().getLocation())
                && event.getHitBlock().getType().equals(Material.BELL))
            superBell.doFunction((Player) event.getEntity().getShooter(), event.getHitBlock().getLocation());
    }
    
    @EventHandler
    public void onShipPilot(CraftDetectEvent event) {
        Craft craft = event.getCraft();
        Player pilot = craft.getNotificationPlayer();
        if (pilot == null) {
            return;
        }
        List<Location> superBlockLocations = superBlocks.getSuperBlockLocations()
                .stream()
                .filter(location -> craft.getHitBox().contains((int) location.getX(), (int) location.getY(), (int) location.getZ()))
                .collect(Collectors.toList());
        
        if (superBlockLocations.size() == 0)
            return;
        
        Common.tell(pilot, Settings.PREFIX + "&cCannot pilot ship: there is an amplified beacon or a player radar on ship!");
        CompSound.ANVIL_LAND.play(pilot.getLocation(), 0.5F, 1F);
        event.setCancelled(true);
    }
    
}
