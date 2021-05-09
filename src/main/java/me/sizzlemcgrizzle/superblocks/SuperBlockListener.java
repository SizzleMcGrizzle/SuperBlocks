package me.sizzlemcgrizzle.superblocks;

import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.events.CraftDetectEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
                MessageUtil.sendMessage(plugin, player, MessageLevel.WARNING, "You cannot place this here!");
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
        
        superBlock.doFunction(player, location, event);
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
    
    @EventHandler
    public void onShipPilot(CraftDetectEvent event) {
        Craft craft = event.getCraft();
        Player pilot = craft.getNotificationPlayer();
        
        if (pilot == null)
            return;
        
        if (plugin.getSuperBlocks().stream().noneMatch(block -> block.getStructure().stream()
                .anyMatch(location -> craft.getHitBox().contains((int) location.getX(), (int) location.getY(), (int) location.getZ()))))
            return;
        
        MessageUtil.sendMessage(plugin, pilot, MessageLevel.WARNING, "Cannot pilot ship: there is an amplified beacon or a player radar on ship!");
        pilot.playSound(pilot.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.25F, 1F);
        event.setCancelled(true);
    }
    
    
}
