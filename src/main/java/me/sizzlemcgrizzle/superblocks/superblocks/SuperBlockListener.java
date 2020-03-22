package me.sizzlemcgrizzle.superblocks.superblocks;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.IOException;
import java.util.Iterator;

public class SuperBlockListener implements Listener {
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
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) throws IOException, InvalidConfigurationException {
		Material material = event.getBlock().getType();
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();

		if (superBell.isBellOnLocation(location)) {
			event.setCancelled(true);
			return;
		}

		if (SuperBlock.isLocationSuperBlock(location)) {
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
		}

	}

	@EventHandler(ignoreCancelled = true)
	public void onInteractEvent(PlayerInteractEvent event) throws IOException, InvalidConfigurationException {
		if (event.getClickedBlock() == null || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		Material material = event.getClickedBlock().getType();
		Player player = event.getPlayer();
		Location location = event.getClickedBlock().getLocation();

		if (SuperBlock.isLocationSuperBlock(location)) {
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
	public void onExplosion(EntityExplodeEvent event) throws IOException, InvalidConfigurationException {
		Iterator<Block> iter = event.blockList().iterator();
		while (iter.hasNext()) {
			Block block = iter.next();
			if (superBell.isBellOnLocation(block.getLocation()) || SuperBlock.isLocationSuperBlock(block.getLocation()))
				iter.remove();
		}


		/*List<Integer> removeList = new ArrayList<>();
		for (int i = 0; i < event.blockList().size(); i++)
			if (SuperBlock.isLocationSuperBlock(event.blockList().get(i).getLocation()) || superBell.isBellOnLocation(event.blockList().get(i).getLocation()))
				removeList.add(i);
		Common.log(removeList.toString());
		Common.log(String.valueOf(event.blockList().size()));
		for (Integer integer : removeList)
			event.blockList().remove(integer.intValue());*/
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPistonExtend(BlockPistonExtendEvent event) throws IOException, InvalidConfigurationException {
		for (Block block : event.getBlocks())
			if (SuperBlock.isLocationSuperBlock(block.getLocation()) || superBell.isBellOnLocation(block.getLocation()))
				event.setCancelled(true);

	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPistonRetract(BlockPistonRetractEvent event) throws IOException, InvalidConfigurationException {
		for (Block block : event.getBlocks())
			if (SuperBlock.isLocationSuperBlock(block.getLocation()) || superBell.isBellOnLocation(block.getLocation()))
				event.setCancelled(true);

	}

	@EventHandler
	public void onBlockShoot(ProjectileHitEvent event) throws IOException, InvalidConfigurationException {
		if (event.getHitBlock() == null || !(event.getEntity().getShooter() instanceof Player))
			return;
		if (SuperBlock.isLocationSuperBlock(event.getHitBlock().getLocation())
				&& event.getHitBlock().getType().equals(Material.BELL))
			superBell.doFunction((Player) event.getEntity().getShooter(), event.getHitBlock().getLocation());
	}

}
