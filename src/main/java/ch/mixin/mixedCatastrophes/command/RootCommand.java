package ch.mixin.mixedCatastrophes.command;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class RootCommand extends SubCommand implements CommandExecutor {
    protected final String commandName;
    protected final HashMap<String, SubCommand> subCommandMap;

    public RootCommand(MixedCatastrophesData mixedCatastrophesData, String commandName, HashMap<String, SubCommand> subCommandMap) {
        super(mixedCatastrophesData);
        this.commandName = commandName;
        this.subCommandMap = subCommandMap;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.isPluginFlawless()) {
            sender.sendMessage(ChatColor.RED + MixedCatastrophesPlugin.PLUGIN_NAME + " Plugin has Problems.");
            return true;
        }

        if (!command.getName().equalsIgnoreCase(commandName))
            return true;

        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        fetchCommand(arguments).execute(sender, arguments);
        return true;
    }

    @Override
    public SubCommand fetchCommand(List<String> arguments) {
        if (arguments.size() == 0)
            return this;

        SubCommand subCommand = subCommandMap.get(arguments.get(0));

        if (subCommand == null)
            return this;

        arguments.remove(0);
        return subCommand.fetchCommand(arguments);
    }

    @Override
    public void execute(CommandSender sender, List<String> arguments) {
        sender.sendMessage(ChatColor.RED + MixedCatastrophesPlugin.PLUGIN_NAME + " Command not found.");
    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        return new ArrayList<>(subCommandMap.keySet());
    }

    public String getCommandName() {
        return commandName;
    }
}
