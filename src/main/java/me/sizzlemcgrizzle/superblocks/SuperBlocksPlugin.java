package me.sizzlemcgrizzle.superblocks;


import me.sizzlemcgrizzle.superblocks.beacon.BeaconEffects;
import me.sizzlemcgrizzle.superblocks.commands.SuperBlocksCommandGroup;
import me.sizzlemcgrizzle.superblocks.settings.Settings;
import me.sizzlemcgrizzle.superblocks.superblocks.SuperBlockListener;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SuperBlocksPlugin extends SimplePlugin {
	@Override
	public void onPluginStart() {
		registerEvents(new SuperBlockListener());

		registerCommands("superblocks", new SuperBlocksCommandGroup());
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data");

		if (!file.exists()) {
			file.mkdir();
		}
		activateBeaconTimer();
	}

	@Override
	protected void onPluginReload() {
		File file = new File(SuperBlocksPlugin.getData().getAbsolutePath() + File.separator + "/Data");

		if (!file.exists()) {
			file.mkdir();
		}

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