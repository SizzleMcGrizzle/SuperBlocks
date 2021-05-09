package me.sizzlemcgrizzle.superblocks;

import de.craftlancer.clclans.CLClans;
import de.craftlancer.core.LambdaRunnable;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SuperBell extends SuperBlock {
    
    private CLClans clans = (CLClans) Bukkit.getPluginManager().getPlugin("CLClans");
    
    private HashMap<Player, Long> cooldownMap = new HashMap<>();
    
    public SuperBell(List<Location> structure, UUID owner) {
        super(structure, owner);
    }
    
    public SuperBell(Map<String, Object> map) {
        super(map);
    }
    
    @Override
    public @Nonnull
    Map<String, Object> serialize() {
        return super.serialize();
    }
    
    @Override
    public void doFunction(Player player, Location location, PlayerInteractEvent event) {
        if (cooldownMap.containsKey(player)) {
            if (cooldownMap.get(player) + 60000L >= System.currentTimeMillis()) {
                MessageUtil.sendMessage(SuperBlocksPlugin.instance, player, MessageLevel.INFO, "You must wait §c" + (cooldownMap.get(player) + 60000L - System.currentTimeMillis()) / 1000L + " seconds§e to use this again.");
                return;
            }
            
            this.cooldownMap.remove(player);
        }
        
        event.setCancelled(true);
        player.playSound(location, Sound.BLOCK_BELL_RESONATE, 1.0F, 1.0F);
        player.sendMessage("§aScanning...");
        cooldownMap.put(player, System.currentTimeMillis());
        new LambdaRunnable(() -> {
            int clan = 0;
            int neutral = 0;
            int rival = 0;
            
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getLocation().getWorld().equals(location.getWorld()))
                    continue;
                if (!p.equals(player) && location.distance(p.getLocation()) <= 200.0D) {
                    if (clans.getClan(p) == null || clans.getClan(Bukkit.getOfflinePlayer(player.getUniqueId())) == null) {
                        ++neutral;
                    } else if (clans.getClan(Bukkit.getOfflinePlayer(p.getUniqueId())).hasRival(clans.getClan(Bukkit.getOfflinePlayer(player.getUniqueId())))) {
                        ++rival;
                    } else if (clans.getClan(p).equals(clans.getClan(player))) {
                        ++clan;
                    } else {
                        ++neutral;
                    }
                }
            }
            
            if (clan == 0 && neutral == 0 && rival == 0) {
                MessageUtil.sendMessage(SuperBlocksPlugin.instance, player, MessageLevel.INFO, "There are no players nearby.");
            } else {
                MessageUtil.sendMessage(SuperBlocksPlugin.instance, player, MessageLevel.INFO, "§eThere are §" + clan + " clan members§e, §4" + rival + " rivals§e, and §f" + neutral + " players §enearby.");
            }
            
        }).runTaskLater(SuperBlocksPlugin.instance, 60);
        
    }
    
    public static ItemStack getItem() {
        ItemStack item = new ItemStack(Material.BELL);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Player Radar");
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Place this radar down", ChatColor.GRAY + "and right click to", ChatColor.GRAY + "scan for players."));
        
        item.setItemMeta(meta);
        return item;
    }
}
