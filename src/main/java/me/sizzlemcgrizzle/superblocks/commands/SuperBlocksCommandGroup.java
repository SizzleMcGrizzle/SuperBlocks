package me.sizzlemcgrizzle.superblocks.commands;

import org.mineacademy.fo.command.SimpleCommandGroup;

public class SuperBlocksCommandGroup extends SimpleCommandGroup {
    @Override
    protected void registerSubcommands() {
        registerSubcommand(new SuperBlocksGetCommand(this));
        registerSubcommand(new SuperBlocksConfigCommand(this));
        registerSubcommand(new SuperBlocksGetcurrencyCommand(this));
        registerSubcommand(new SuperBlocksReloadCommand(this));
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
