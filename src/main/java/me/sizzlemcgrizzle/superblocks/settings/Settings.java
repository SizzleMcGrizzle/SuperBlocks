package me.sizzlemcgrizzle.superblocks.settings;

import de.craftlancer.core.util.MessageUtil;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Settings {
    
    public static String PREFIX;
    public static List<Material> PASSTHROUGH_BLOCKS_MATERIAL;
    
    private static void init(ConfigurationSection config) {
        PREFIX = ChatColor.translateAlternateColorCodes('&', config.getString("Prefix", "[SuperBlocks]"));
        PASSTHROUGH_BLOCKS_MATERIAL = config.getStringList("Beacon_Passthrough_Blocks").stream().map(Material::valueOf).collect(Collectors.toList());
    }
    
    public static void load(SuperBlocksPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "settings.yml");
        
        if (!file.exists())
            plugin.saveResource("settings.yml", false);
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        init(config);
        
        MessageUtil.register(plugin, new TextComponent(PREFIX));
    }
}
