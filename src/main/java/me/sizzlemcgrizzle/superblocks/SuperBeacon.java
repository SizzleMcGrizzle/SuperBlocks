package me.sizzlemcgrizzle.superblocks;

import me.sizzlemcgrizzle.superblocks.settings.Settings;
import me.sizzlemcgrizzle.superblocks.util.SuperBlocksUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.mineacademy.fo.Common;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SuperBeacon extends SuperBlock {
    
    private PotionEffect buff1;
    private PotionEffect buff2;
    private PotionEffect debuff;
    private long expireTime;
    
    public SuperBeacon(List<Location> structure, UUID owner) {
        super(structure, owner);
        
        this.buff1 = null;
        this.buff2 = null;
        this.debuff = null;
        this.expireTime = 0;
    }
    
    public SuperBeacon(Map<String, Object> map) {
        super(map);
        
        buff1 = SuperBlocksUtil.getPotionFromString((String) map.get("buff1"));
        buff2 = SuperBlocksUtil.getPotionFromString((String) map.get("buff2"));
        debuff = SuperBlocksUtil.getPotionFromString((String) map.get("debuff"));
        expireTime = Long.parseLong((String) map.get("expire"));
    }
    
    @Override
    public @Nonnull
    Map<String, Object> serialize() {
        Map<String, Object> map = super.serialize();
        
        map.put("buff1", SuperBlocksUtil.getStringFromPotion(buff1));
        map.put("buff2", SuperBlocksUtil.getStringFromPotion(buff2));
        map.put("debuff", SuperBlocksUtil.getStringFromPotion(debuff));
        map.put("expire", String.valueOf(expireTime));
        
        return map;
    }
    
    @Override
    public void doFunction(Player player, Location location) {
        if (isActive())
            new BeaconPreferencesMenu(this).displayTo(player);
        else {
            Common.tell(player, Settings.PREFIX + "&cYou must activate this beacon! &e&o(There must be a beam and a 3x3 grid of blocks under like a normal beacon)");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 0.5F);
            Runnable runnable = player::closeInventory;
            Common.runLater(1, runnable);
        }
    }
    
    /*
     *   Is the beacon chunk loaded?
     *   Are there any non-transparent blocks above it?
     *   Does it have a 3x3 of iron/diamond/emerald/gold blocks under it?
     */
    public boolean isActive() {
        Optional<Location> optional = getStructure().stream().findFirst();
        
        if (!optional.isPresent())
            return false;
        
        Location beaconLocation = optional.get();
        
        double x = beaconLocation.getX();
        double y = beaconLocation.getY();
        double z = beaconLocation.getZ();
        World world = beaconLocation.getWorld();
        if (world == null || !world.isChunkLoaded(beaconLocation.getBlockX() >> 4, beaconLocation.getBlockZ() >> 4))
            return false;
        
        if (!isBeaconActivateBlock(new Location(world, x, y - 1, z).getBlock().getType())) return false;
        if (!isBeaconActivateBlock(new Location(world, x, y - 1, z - 1).getBlock().getType())) return false;
        if (!isBeaconActivateBlock(new Location(world, x - 1, y - 1, z - 1).getBlock().getType())) return false;
        if (!isBeaconActivateBlock(new Location(world, x + 1, y - 1, z - 1).getBlock().getType())) return false;
        if (!isBeaconActivateBlock(new Location(world, x - 1, y - 1, z + 1).getBlock().getType())) return false;
        if (!isBeaconActivateBlock(new Location(world, x + 1, y - 1, z + 1).getBlock().getType())) return false;
        if (!isBeaconActivateBlock(new Location(world, x + 1, y - 1, z).getBlock().getType())) return false;
        if (!isBeaconActivateBlock(new Location(world, x - 1, y - 1, z).getBlock().getType())) return false;
        if (!isBeaconActivateBlock(new Location(world, x, y - 1, z + 1).getBlock().getType())) return false;
        
        for (int i = (int) y + 1; i <= 255; i++) {
            Material material = (new Location(world, x, i, z).getBlock().getType());
            if (material.isAir())
                continue;
            if (occludingPassThrough(material).equals("true"))
                continue;
            if (occludingPassThrough(material).equals("false"))
                return false;
            if (material.isOccluding())
                return false;
            
        }
        return true;
    }
    
    private boolean isBeaconActivateBlock(Material material) {
        switch (material) {
            case DIAMOND_BLOCK:
            case IRON_BLOCK:
            case EMERALD_BLOCK:
            case GOLD_BLOCK:
                return true;
            default:
                return false;
        }
    }
    
    private String occludingPassThrough(Material material) {
        if (Settings.PASSTHROUGH_BLOCKS_MATERIAL.contains(material))
            return "true";
        switch (material) {
            case PISTON:
            case GLOWSTONE:
            case REDSTONE_LAMP:
            case OBSERVER:
                return "false";
            default:
                return "null";
        }
    }
    
    public double getRange() {
        Optional<Location> optional = getStructure().stream().filter(location -> location.getBlock().getType() == Material.BEACON).findFirst();
        
        if (!optional.isPresent())
            return 0;
        
        Location location = optional.get();
        
        int tier = 0;
        if (new BeaconPlatformBox(location.clone().subtract(1, 1, 1), location.clone().subtract(-1, 1, -1)).isValidTier())
            tier++;
        if (new BeaconPlatformBox(location.clone().subtract(2, 2, 2), location.clone().subtract(-2, 2, -2)).isValidTier())
            tier++;
        if (new BeaconPlatformBox(location.clone().subtract(3, 3, 3), location.clone().subtract(-3, 3, -3)).isValidTier())
            tier++;
        if (new BeaconPlatformBox(location.clone().subtract(4, 4, 4), location.clone().subtract(-4, 4, -4)).isValidTier())
            tier++;
        
        return Math.pow(20 + tier * 20, 2);
    }
    
    private static class BeaconPlatformBox {
        private Location minLocation;
        private Location maxLocation;
        private World world;
        
        private BeaconPlatformBox(Location minLocation, Location maxLocation) {
            this.minLocation = minLocation;
            this.maxLocation = maxLocation;
            this.world = maxLocation.getWorld();
        }
        
        private boolean isValidTier() {
            
            List<Location> list = new ArrayList<>();
            double minX = minLocation.getX(), minY = minLocation.getY(), minZ = minLocation.getZ();
            double maxX = maxLocation.getX(), maxZ = maxLocation.getZ();
            
            for (; minX <= maxX; minX++)
                for (; minZ <= maxZ; minZ++) {
                    list.add(new Location(world, minX, minY, minZ));
                }
            
            return list.stream().allMatch(location -> isBeaconActivateBlock(location.getBlock().getType()));
        }
        
        private boolean isBeaconActivateBlock(Material material) {
            switch (material) {
                case DIAMOND_BLOCK:
                case IRON_BLOCK:
                case EMERALD_BLOCK:
                case GOLD_BLOCK:
                    return true;
                default:
                    return false;
            }
        }
    }
    
    public PotionEffect getBuff1() {
        return buff1;
    }
    
    public PotionEffect getBuff2() {
        return buff2;
    }
    
    public PotionEffect getDebuff() {
        return debuff;
    }
    
    public void setBuff1(PotionEffect buff1) {
        this.buff1 = buff1;
    }
    
    public void setBuff2(PotionEffect buff2) {
        this.buff2 = buff2;
    }
    
    public void setDebuff(PotionEffect debuff) {
        this.debuff = debuff;
    }
    
    public long getExpireTime() {
        return expireTime;
    }
    
    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
    
    public static ItemStack getItem() {
        ItemStack item = new ItemStack(Material.BEACON);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.ITALIC + "Amplified Beacon");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Place this beacon down", ChatColor.GRAY + "and right click to", ChatColor.GRAY + "edit properties"));
        
        item.setItemMeta(meta);
        return item;
    }
}
