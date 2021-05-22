package ch.mixin.command;


import ch.mixin.main.MixedCatastrophesPlugin;

public class CommandInitializer {
    public static void setupCommands(MixedCatastrophesPlugin plugin) {
        MxCsCommand mxCsCommand = new MxCsCommand(plugin);
        plugin.getCommand(mxCsCommand.getCommandName()).setExecutor(mxCsCommand);
        plugin.getCommand(mxCsCommand.getCommandName()).setTabCompleter(new CommandCompleter(mxCsCommand));
        MxCommand mxCommand = new MxCommand(plugin);
        plugin.getCommand(mxCommand.getCommandName()).setExecutor(mxCommand);
        plugin.getCommand(mxCommand.getCommandName()).setTabCompleter(new CommandCompleter(mxCommand));
    }
}
