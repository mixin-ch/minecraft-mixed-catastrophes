package ch.mixin.mixedCatastrophes.command.mxCs.terror;

import ch.mixin.mixedCatastrophes.command.SubCommand;
import ch.mixin.mixedCatastrophes.command.mxCs.terror.stalker.StalkerCauseCommand;
import ch.mixin.mixedCatastrophes.command.mxCs.terror.stalker.StalkerClearCommand;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StalkerCommand extends SubCommand {
    private final HashMap<String, SubCommand> subCommandMap;

    public StalkerCommand(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor);
        subCommandMap = new HashMap<>();
        subCommandMap.put("cause", new StalkerCauseCommand(mixedCatastrophesManagerAccessor));
        subCommandMap.put("clear", new StalkerClearCommand(mixedCatastrophesManagerAccessor));
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
