package me.sizzlemcgrizzle.superblocks;

import me.sizzlemcgrizzle.superblocks.settings.Settings;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.events.CraftDetectEvent;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.mineacademy.fo.remain.CompSound;

import java.util.Arrays;
import java.util.Collections;

public class SuperBlockListener implements Listener {
    private SuperBlocksPlugin plugin;
    
    public SuperBlockListener(SuperBlocksPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        Location location = event.getBlockPlaced().getLocation();
        
        if (item.isSimilar(SuperBeacon.getItem()))
            plugin.addSuperBlock(new SuperBeacon(Collections.singletonList(location), player.getUniqueId()));
        if (item.isSimilar(SuperBell.getItem())) {
            if (plugin.getSuperBlocks().stream().anyMatch(block -> block.getStructure().contains(event.getBlockAgainst().getLocation()))) {
                Common.tell(player, Settings.PREFIX + "&cYou cannot place this here!");
                event.setCancelled(true);
                return;
            }
            plugin.addSuperBlock(new SuperBell(Arrays.asList(location, event.getBlockAgainst().getLocation()), player.getUniqueId()));
        }
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();
        
        if (plugin.getSuperBlocks().stream().noneMatch(block -> block.getStructure().contains(location)))
            return;
        
        event.setDropItems(false);
        
        SuperBlock superBlock = plugin.getSuperBlocks().stream().filter(block -> block.getStructure().contains(location)).findFirst().get();
        
        if (superBlock instanceof SuperBeacon)
            location.getWorld().dropItemNaturally(location, SuperBeacon.getItem());
        else if (superBlock instanceof SuperBell) {
            if (location.getBlock().getType() != Material.BELL) {
                event.setCancelled(true);
                return;
            } else
                location.getWorld().dropItemNaturally(location, SuperBell.getItem());
        }
        
        plugin.removeSuperBlock(superBlock);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        
        Player player = event.getPlayer();
        Location location = event.getClickedBlock().getLocation();
        
        if (plugin.getSuperBlocks().stream().noneMatch(block -> block.getStructure().contains(location)))
            return;
        
        SuperBlock superBlock = plugin.getSuperBlocks().stream().filter(block -> block.getStructure().contains(location)).findFirst().get();
        
        if (superBlock instanceof SuperBell && location.getBlock().getType() != Material.BELL)
            return;
        
        superBlock.doFunction(player, location);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onExplosion(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> plugin.getSuperBlocks().stream().anyMatch(b -> b.getStructure().contains(block.getLocation())));
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (plugin.getSuperBlocks().stream().anyMatch(superBlock -> superBlock.getStructure().stream().anyMatch(location -> event.getBlocks().contains(location.getBlock()))))
            event.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (plugin.getSuperBlocks().stream().anyMatch(superBlock -> superBlock.getStructure().stream().anyMatch(location -> event.getBlocks().contains(location.getBlock()))))
            event.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockShoot(ProjectileHitEvent event) {
        
        if (event.getHitBlock() == null || !(event.getEntity().getShooter() instanceof Player))
            return;
        
        Player player = (Player) event.getEntity().getShooter();
        Location hitLocation = event.getHitBlock().getLocation();
        
        if (hitLocation.getBlock().getType() == Material.BELL
                && plugin.getSuperBlocks().stream().anyMatch(block -> block.getStructure().contains(hitLocation)))
            plugin.getSuperBlocks().stream().filter(block -> block.getStructure().contains(hitLocation)).findFirst().get().doFunction(player, hitLocation);
    }
    
    @EventHandler
    public void onShipPilot(CraftDetectEvent event) {
        Craft craft = event.getCraft();
        Player pilot = craft.getNotificationPlayer();
        
        if (pilot == null) {
            return;
        }
        
        if (plugin.getSuperBlocks().stream().noneMatch(block -> block.getStructure().stream()
                .noneMatch(location -> craft.getHitBox().contains((int) location.getX(), (int) location.getY(), (int) location.getZ()))))
            return;
        
        Common.tell(pilot, Settings.PREFIX + "&cCannot pilot ship: there is an amplified beacon or a player radar on ship!");
        CompSound.ANVIL_LAND.play(pilot.getLocation(), 0.5F, 1F);
        event.setCancelled(true);
    }
    
    
}
