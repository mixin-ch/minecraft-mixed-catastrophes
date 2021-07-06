package ch.mixin.mixedCatastrophes.command;


import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;

public class CommandInitializer {
    public static void setupCommands(MixedCatastrophesData mixedCatastrophesData) {
        MixedCatastrophesPlugin plugin = mixedCatastrophesData.getPlugin();
        MxCsCommand mxCsCommand = new MxCsCommand(mixedCatastrophesData);
        plugin.getCommand(mxCsCommand.getCommandName()).setExecutor(mxCsCommand);
        plugin.getCommand(mxCsCommand.getCommandName()).setTabCompleter(new CommandCompleter(mxCsCommand));
    }
}
