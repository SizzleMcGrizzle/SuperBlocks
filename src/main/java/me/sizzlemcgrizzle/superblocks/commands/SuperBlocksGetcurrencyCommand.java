package me.sizzlemcgrizzle.superblocks.commands;

import me.sizzlemcgrizzle.superblocks.settings.Settings;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

public class SuperBlocksGetcurrencyCommand extends SimpleSubCommand {
	protected SuperBlocksGetcurrencyCommand(final SimpleCommandGroup parent) {
		super(parent, "getcurrency");
		setPermission("superblocks.getcurrency");
	}

	@Override
	protected void onCommand() {
		getPlayer().getInventory().addItem(Settings.CURRENCY);
		tell(Settings.PREFIX + "&7You have been given this plugin's currency.");
	}
}
