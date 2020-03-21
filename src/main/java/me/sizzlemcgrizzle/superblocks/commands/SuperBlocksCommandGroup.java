package me.sizzlemcgrizzle.superblocks.commands;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class SuperBlocksCommandGroup extends SimpleCommandGroup {
	@Override
	protected void registerSubcommands() {
		registerSubcommand(new SuperBlocksGetCommand(this));
		registerSubcommand(new SuperBlocksSetcurrencyCommand(this));
		registerSubcommand(new SuperBlocksGetcurrencyCommand(this));
		registerSubcommand(new SuperBlocksReloadCommand(this));
		registerSubcommand(new SuperBlocksSetPrefixCommand(this));
	}

	@Override
	protected String getCredits() {
		return "";
	}

	@Override
	protected String getHeaderPrefix() {
		return "&6&l";
	}
}
