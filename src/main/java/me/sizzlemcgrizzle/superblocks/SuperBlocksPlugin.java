package me.sizzlemcgrizzle.superblocks;


import me.sizzlemcgrizzle.superblocks.beacon.BeaconEffects;
import me.sizzlemcgrizzle.superblocks.commands.SuperBlocksCommandGroup;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBlockListener;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public class SuperBlocksPlugin extends SimplePlugin {
	@Override
	public void onPluginStart() {
		registerEvents(new SuperBlockListener());

		registerCommands("superblocks", new SuperBlocksCommandGroup());

		activateBeaconTimer();
	}

	@Override
	protected void onPluginReload() {
		activateBeaconTimer();
	}

	@Override
	public List<Class<? extends YamlStaticConfig>> getSettings() {
		return Arrays.asList(Settings.class);
	}

	private void activateBeaconTimer() {
		new BeaconEffects(160, 160);
	}
}