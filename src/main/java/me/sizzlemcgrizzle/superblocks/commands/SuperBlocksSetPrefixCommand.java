package me.sizzlemcgrizzle.superblocks.commands;

import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.io.File;
import java.io.IOException;

public class SuperBlocksSetPrefixCommand extends SimpleSubCommand {
	protected SuperBlocksSetPrefixCommand(final SimpleCommandGroup parent) {
		super(parent, "setprefix");
		setPermission("superblocks.setprefix");
		setMinArguments(1);
	}

	@Override
	protected void onCommand() {
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/settings.yml");
		YamlConfiguration config = new YamlConfiguration();
		try {
			if (!file.exists())
				file.createNewFile();
			config.load(file);
			config.set("Prefix", args[0] + " ");
			config.save(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		tell(Settings.PREFIX + "&aYou have changed the prefix. Reload the plugin to apply changes.");
	}
}
