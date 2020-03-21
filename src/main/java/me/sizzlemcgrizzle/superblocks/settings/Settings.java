package me.sizzlemcgrizzle.superblocks.settings;

import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.settings.SimpleSettings;

public class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	public static String PREFIX;
	public static ItemStack CURRENCY;

	private static void init() {
		PREFIX = getString("Prefix");
		CURRENCY = getConfig().getItemStack("Currency");
	}
}
