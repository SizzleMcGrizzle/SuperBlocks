package me.sizzlemcgrizzle.superblocks.settings;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.List;
import java.util.stream.Collectors;

public class Settings extends SimpleSettings {
    
    @Override
    protected int getConfigVersion() {
        return 1;
    }
    
    public static String PREFIX;
    public static Boolean USE_ECONOMY;
    public static Double CURRENCY_MONEY;
    public static ItemStack CURRENCY_ITEM;
    public static List<Material> PASSTHROUGH_BLOCKS_MATERIAL;
    
    private static void init() {
        PREFIX = getString("Prefix");
        USE_ECONOMY = getBoolean("Use_Economy");
        CURRENCY_MONEY = getDouble("Currency_Money");
        CURRENCY_ITEM = getConfig().getItemStack("Currency_Item");
        PASSTHROUGH_BLOCKS_MATERIAL = getMaterialList("Beacon_Passthrough_Blocks").getSource().stream().map(CompMaterial::getMaterial).collect(Collectors.toList());
    }
}
