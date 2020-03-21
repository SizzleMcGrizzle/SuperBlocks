package me.sizzlemcgrizzle.superblocks.commands;

import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.io.File;
import java.io.IOException;

public class SuperBlocksSetcurrencyCommand extends SimpleSubCommand {
	protected SuperBlocksSetcurrencyCommand(final SimpleCommandGroup parent) {
		super(parent, "setcurrency");
		setPermission("superblocks.setcurrency");
	}

	@Override
	protected void onCommand() {
		ItemStack item = getPlayer().getInventory().getItemInMainHand();
		if (!item.getItemMeta().hasDisplayName()) {
			tell(Settings.PREFIX + "&cYou must be holding an item with a special name!");
			return;
		}
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/settings.yml");
		YamlConfiguration config = new YamlConfiguration();
		try {
			if (!file.exists())
				file.createNewFile();
			config.load(file);
			config.set("Currency", item);
			config.save(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		tell(Settings.PREFIX + "&7The currency has been set to an item named " + item.getItemMeta().getDisplayName() + "&7. Reload the plugin to apply changes.");

	}
}
