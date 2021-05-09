package me.sizzlemcgrizzle.superblocks.commands;

import de.craftlancer.core.Utils;
import de.craftlancer.core.command.SubCommand;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import me.sizzlemcgrizzle.superblocks.SuperBeacon;
import me.sizzlemcgrizzle.superblocks.SuperBell;
import me.sizzlemcgrizzle.superblocks.SuperBlocksPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SuperBlocksGetCommand extends SubCommand {
    private SuperBlocksPlugin plugin;
    
    public SuperBlocksGetCommand(SuperBlocksPlugin plugin) {
        super("superblocks.admin", plugin, false);
        
        this.plugin = plugin;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2)
            return Utils.getMatches(args[1], Arrays.asList("bell", "beacon"));
        
        return Collections.emptyList();
    }
    
    @Override
    protected String execute(CommandSender sender, Command command, String s, String[] args) {
        if (!checkSender(sender)) {
            MessageUtil.sendMessage(plugin, sender, MessageLevel.INFO, "You do not have access to this command.");
            return null;
        }
        
        Player player = (Player) sender;
        
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("bell")) {
                player.getInventory().addItem(SuperBell.getItem());
                MessageUtil.sendMessage(plugin, player, MessageLevel.SUCCESS, "You have received a player radar.");
            } else if (args[1].equalsIgnoreCase("beacon")) {
                player.getInventory().addItem(SuperBeacon.getItem());
                MessageUtil.sendMessage(plugin, player, MessageLevel.SUCCESS, "You have received an amplified beacon.");
            } else
                MessageUtil.sendMessage(plugin, player, MessageLevel.INFO, "You must enter a valid super block.");
        }
        
        return null;
    }
    
    @Override
    public void help(CommandSender commandSender) {
    
    }
}
