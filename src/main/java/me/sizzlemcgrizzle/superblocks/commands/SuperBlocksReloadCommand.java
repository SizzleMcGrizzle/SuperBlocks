package me.sizzlemcgrizzle.superblocks.commands;

import me.sizzlemcgrizzle.superblocks.settings.Settings;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleLocalization;

public class SuperBlocksReloadCommand extends SimpleSubCommand {
	protected SuperBlocksReloadCommand(final SimpleCommandGroup parent) {
		super(parent, "reload");
		setPermission("superblocks.reload");
	}

	@Override
	protected void onCommand() {
		try {
			SimplePlugin.getInstance().reload();
			tell(Settings.PREFIX + "&7SuperBlocks configuration has been successfully reloaded.");
		} catch (Throwable t) {
			t.printStackTrace();

			tell(SimpleLocalization.Commands.RELOAD_FAIL.replace("{error}", t.toString()));

		}
	}
}
