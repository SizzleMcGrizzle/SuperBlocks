package me.sizzlemcgrizzle.superblocks.commands;

import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuperBlocksGetCommand extends SimpleSubCommand {
	private SuperBlocksPlugin superBlocks;
	
	public SuperBlocksGetCommand(final SimpleCommandGroup parent) {
		super(parent, "get");
		setMinArguments(1);
		setUsage("<bell/beacon>");
		setPermission("superblocks.get");
		
		this.superBlocks = (SuperBlocksPlugin) SimplePlugin.getInstance();
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
				getPlayer().getInventory().addItem(superBlocks.getSuperBell().getItem());
				tell(Settings.PREFIX + "&eYou have recieved a &6&lPlayer Radar&e.");
			} else if (args[0].equalsIgnoreCase("beacon")) {
				getPlayer().getInventory().addItem(superBlocks.getSuperBeacon().getItem());
				tell(Settings.PREFIX + "&eYou have recieved an &b&oAmplifiedBeacon&e.");
			} else
				tell(Settings.PREFIX + "&cYou did not enter a valid super block type.");
		}
	}
}
