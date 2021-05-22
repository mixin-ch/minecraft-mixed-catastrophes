package ch.mixin.command.mxCs.terror;

import ch.mixin.command.SubCommand;
import ch.mixin.command.mxCs.terror.stalker.StalkerCauseCommand;
import ch.mixin.command.mxCs.terror.stalker.StalkerClearCommand;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StalkerCommand extends SubCommand {
    private final HashMap<String, SubCommand> subCommandMap;

    public StalkerCommand(MixedCatastrophesPlugin plugin) {
        super(plugin);
        subCommandMap = new HashMap<>();
        subCommandMap.put("cause", new StalkerCauseCommand(plugin));
        subCommandMap.put("clear", new StalkerClearCommand(plugin));
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
