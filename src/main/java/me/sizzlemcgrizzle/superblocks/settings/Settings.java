package me.sizzlemcgrizzle.superblocks.settings;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.ArrayList;
import java.util.List;

public class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	public static String PREFIX;
	public static ItemStack CURRENCY;
	public static List<Material> PASSTHROUGH_BLOCKS_MATERIAL;
	public static List<String> PASSTHROUGH_BLOCKS_STRING;

	private static void init() {
		PREFIX = getString("Prefix");
		CURRENCY = getConfig().getItemStack("Currency");
		StrictList<Material> list = getMaterialList("Beacon_Passthrough_Blocks");
		List<Material> matList = new ArrayList<>();
		List<String> strList = new ArrayList<>();
		for (Material mat : list) {
			matList.add(mat);
			strList.add(mat.toString());
		}
		PASSTHROUGH_BLOCKS_MATERIAL = matList;
		PASSTHROUGH_BLOCKS_STRING = strList;
	}
}
