package me.sizzlemcgrizzle.superblocks.superblocks;

import lombok.NonNull;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class SuperBlock {
    
    private ItemStack item;
    
    private String name;
    private List<String> lore;
    private Material material;
    
    public SuperBlock(String itemName, List<String> itemLore, Material material) {
        this.name = itemName;
        this.lore = itemLore;
        this.material = material;
        
        setItem();
    }
    
    private void setItem() {
        item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }
    
    public boolean isSuperBlock(@NonNull ItemStack item) {
        if (!item.hasItemMeta() || item.getItemMeta() == null)
            return false;
        return (item.isSimilar(item));
    }
    
    public abstract void doFunction(Player player, Location location);
    
    
    public void serialize(Location location, String type) {
        File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/superblocks.yml");
        YamlConfiguration config = new YamlConfiguration();
        
        try {
            if (!file.exists())
                file.createNewFile();
            
            config.load(file);
            
            String serializedLocation = (int) location.getX() + "&-&" + (int) location.getY() + "&-&" + (int) location.getZ() + "&-&" + location.getWorld().getName();
            
            config.set(serializedLocation, type);
            
            config.save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    
    public void removeFromFile(Location location) {
        File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data/superblocks.yml");
        YamlConfiguration config = new YamlConfiguration();
        
        try {
            if (!file.exists())
                file.createNewFile();
            
            config.load(file);
            
            String serializedLocation = (int) location.getX() + "&-&" + (int) location.getY() + "&-&" + (int) location.getZ() + "&-&" + location.getWorld().getName();
            
            config.set(serializedLocation, null);
            
            config.save(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    
}
