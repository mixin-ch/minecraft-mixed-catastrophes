package ch.mixin.command.mxCs;

import ch.mixin.command.SubCommand;
import ch.mixin.command.mxCs.catastrophes.CatastrophesActivateCommand;
import ch.mixin.command.mxCs.catastrophes.CatastrophesDeactivateCommand;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CatastrophesCommand extends SubCommand {
    private final HashMap<String, SubCommand> subCommandMap;

    public CatastrophesCommand(MixedCatastrophesPlugin plugin) {
        super(plugin);
        subCommandMap = new HashMap<>();
        subCommandMap.put("activate", new CatastrophesActivateCommand(plugin));
        subCommandMap.put("deactivate", new CatastrophesDeactivateCommand(plugin));
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
        List<String> options = new ArrayList<>();

        if (arguments.size() == 1) {
            options.addAll(subCommandMap.keySet());
        }

        return options;
    }
}
