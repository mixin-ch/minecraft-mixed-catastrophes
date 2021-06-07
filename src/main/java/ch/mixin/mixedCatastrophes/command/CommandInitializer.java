package ch.mixin.mixedCatastrophes.command;


import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;

public class CommandInitializer {
    public static void setupCommands(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        MixedCatastrophesPlugin plugin = mixedCatastrophesManagerAccessor.getPlugin();
        MxCsCommand mxCsCommand = new MxCsCommand(mixedCatastrophesManagerAccessor);
        plugin.getCommand(mxCsCommand.getCommandName()).setExecutor(mxCsCommand);
        plugin.getCommand(mxCsCommand.getCommandName()).setTabCompleter(new CommandCompleter(mxCsCommand));
    }
}
