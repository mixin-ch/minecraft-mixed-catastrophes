package ch.mixin.command;

import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    protected final MixedCatastrophesPlugin plugin;

    public SubCommand(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract SubCommand fetchCommand(List<String> arguments);

    public abstract void execute(CommandSender sender, List<String> arguments);

    public abstract List<String> getOptions(List<String> arguments);
}
