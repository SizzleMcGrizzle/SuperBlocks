package me.sizzlemcgrizzle.superblocks.commands;

import de.craftlancer.core.command.CommandHandler;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;

public class SuperBlocksCommandHandler extends CommandHandler {
    public SuperBlocksCommandHandler(SuperBlocksPlugin plugin) {
        super(plugin);
        
        registerSubCommand("get", new SuperBlocksGetCommand(plugin));
    }
}
