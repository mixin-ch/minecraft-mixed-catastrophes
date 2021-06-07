package ch.mixin.mixedCatastrophes.command;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    protected final MixedCatastrophesPlugin plugin;
    protected final MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor;

    public SubCommand(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        plugin = mixedCatastrophesManagerAccessor.getPlugin();
        this.mixedCatastrophesManagerAccessor = mixedCatastrophesManagerAccessor;
    }

    public abstract SubCommand fetchCommand(List<String> arguments);

    public abstract void execute(CommandSender sender, List<String> arguments);

    public abstract List<String> getOptions(List<String> arguments);
}
