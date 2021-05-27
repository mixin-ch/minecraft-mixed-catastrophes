package ch.mixin.mixedCatastrophes.command;


import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;

public class CommandInitializer {
    public static void setupCommands(MixedCatastrophesPlugin plugin) {
        MxCsCommand mxCsCommand = new MxCsCommand(plugin);
        plugin.getCommand(mxCsCommand.getCommandName()).setExecutor(mxCsCommand);
        plugin.getCommand(mxCsCommand.getCommandName()).setTabCompleter(new CommandCompleter(mxCsCommand));
    }
}
