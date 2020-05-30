package me.sizzlemcgrizzle.superblocks.commands;

import me.sizzlemcgrizzle.superblocks.settings.Settings;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuperBlocksConfigCommand extends SimpleSubCommand {
    protected SuperBlocksConfigCommand(final SimpleCommandGroup parent) {
        super(parent, "config");
        setPermission("superblocks.config");
        setMinArguments(1);
    }
    
    @Override
    protected List<String> tabComplete() {
        if (args.length == 1)
            return completeLastWord(Arrays.asList("setPrefix", "setCurrency", "addPassthroughBlock", "removePassthroughBlock"));
        if (args.length == 2 && args[0].equalsIgnoreCase("removePassthroughBlock"))
            return completeLastWord(Settings.PASSTHROUGH_BLOCKS_STRING);
        return new ArrayList<>();
    }
    
    @Override
    protected void onCommand() {
        checkConsole();
        
        ItemStack item = getPlayer().getInventory().getItemInMainHand();
        
        File file = new File(SimplePlugin.getInstance().getDataFolder(), "settings.yml");
        YamlConfiguration config = new YamlConfiguration();
        try {
            if (!file.exists())
                FileUtil.extract("settings.yml");
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        
        
        if (args[0].equalsIgnoreCase("setCurrency")) {
            if (CompMaterial.isAir(item.getType()) || !item.getItemMeta().hasDisplayName()) {
                tell(Settings.PREFIX + "&cYou must be holding an item with a special name!");
                return;
            }
            config.set("Currency", item);
            tell(Settings.PREFIX + "&7The currency has been set to an item named " + item.getItemMeta().getDisplayName() + "&7. &2/superblocks reload&7 to apply changes.");
            
            
        } else if (args[0].equalsIgnoreCase("setPrefix")) {
            if (args.length < 2) {
                tell(Settings.PREFIX + "&cYou did not enter a prefix!");
                return;
            }
            config.set("Prefix", args[1] + " ");
            tell(Settings.PREFIX + "&7You have changed the prefix. &2/superblocks reload&7 to apply changes.");
            
            
        } else if (args[0].equalsIgnoreCase("addPassthroughBlock")) {
            List<String> serList = Settings.PASSTHROUGH_BLOCKS_STRING;
            if (CompMaterial.isAir(item.getType())) {
                tell(Settings.PREFIX + "&cYou must hold an item!");
                return;
            }
            if (Settings.PASSTHROUGH_BLOCKS_MATERIAL.contains(item.getType())) {
                tell(Settings.PREFIX + "&cThis item is already on the passthrough list!");
                return;
            }
            serList.add(item.getType().toString());
            config.set("Beacon_Passthrough_Blocks", serList);
            tell(Settings.PREFIX + "&7You have added type &6" + item.getType() + " &7to the beacon passthrough blocks list. &2/superblocks reload&7 to apply changes.");
        } else if (args[0].equalsIgnoreCase("removePassthroughBlock")) {
            List<String> serList = Settings.PASSTHROUGH_BLOCKS_STRING;
            if (args.length < 2) {
                tell(Settings.PREFIX + "&cYou must enter a material!");
                return;
            }
            if (Material.getMaterial(args[1]) == null) {
                tell(Settings.PREFIX + "&cThe material you entered is not a valid material type!");
                return;
            }
            if (!serList.contains(args[1])) {
                tell(Settings.PREFIX + "&cThis item is not already on the list!");
                return;
            }
            serList.remove(args[1]);
            config.set("Beacon_Passthrough_Blocks", serList);
            tell(Settings.PREFIX + "&7You have removed type " + args[1] + " from the beacon passthrough blocks list. &2/superblocks reload&7 to apply changes.");
        } else {
            tell(Settings.PREFIX + "&cYou did not enter a valid argument. &oHint: use tab complete!");
            return;
        }
        
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
