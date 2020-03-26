package me.sizzlemcgrizzle.superblocks.commands;

import me.sizzlemcgrizzle.superblocks.settings.Settings;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBeacon;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBell;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuperBlocksGetCommand extends SimpleSubCommand {
	public SuperBlocksGetCommand(final SimpleCommandGroup parent) {
		super(parent, "get");
		setMinArguments(1);
		setUsage("<bell/beacon>");
		setPermission("superblocks.get");
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1) {
			return Arrays.asList("bell", "beacon");
		}
		return new ArrayList<>();
	}

	@Override
	protected void onCommand() {
		checkConsole();
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("bell")) {
				getPlayer().getInventory().addItem(new SuperBell().getItem());
				tell(Settings.PREFIX + "&eYou have recieved a &6&lPlayer Radar&e.");
			} else if (args[0].equalsIgnoreCase("beacon")) {
				getPlayer().getInventory().addItem(new SuperBeacon().getItem());
				tell(Settings.PREFIX + "&eYou have recieved an &b&oAmplifiedBeacon&e.");
			} else
				tell(Settings.PREFIX + "&cYou did not enter a valid super block type.");
		}
	}
}
