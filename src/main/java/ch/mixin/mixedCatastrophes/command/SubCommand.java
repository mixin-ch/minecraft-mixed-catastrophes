package ch.mixin.mixedCatastrophes.command;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {
    protected final MixedCatastrophesPlugin plugin;
    protected final MixedCatastrophesData mixedCatastrophesData;

    public SubCommand(MixedCatastrophesData mixedCatastrophesData) {
        plugin = mixedCatastrophesData.getPlugin();
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    public abstract SubCommand fetchCommand(List<String> arguments);

    public abstract void execute(CommandSender sender, List<String> arguments);

    public abstract List<String> getOptions(List<String> arguments);
}
