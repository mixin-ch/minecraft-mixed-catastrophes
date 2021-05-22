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

//    private final static String prefixSmall = "mx-";
//    private final static String prefix = "mx-cs-";
//
//    public static void setupCommands(MixedCatastrophesPlugin plugin) {
//        plugin.getCommand(prefixSmall + "help").setExecutor(new HelpCommand(plugin, prefixSmall + "help"));
//        plugin.getCommand(prefixSmall + "dealWithDevil").setExecutor(new DealWithDevilCommand(plugin, prefixSmall + "dealWithDevil"));
//        plugin.getCommand(prefix + "startCatastrophes").setExecutor(new StartCatastrophesCommand(plugin, prefix + "startCatastrophes"));
//        plugin.getCommand(prefix + "stopCatastrophes").setExecutor(new StopCatastrophesCommand(plugin, prefix + "stopCatastrophes"));
//        plugin.getCommand(prefix + "aspect").setExecutor(new AspectCommand(plugin, prefix + "aspect"));
//        plugin.getCommand(prefix + "timeDistortion").setExecutor(new TimeDistortionCommand(plugin, prefix + "timeDistortion"));
//        plugin.getCommand(prefix + "weatherCatastrophe").setExecutor(new WeatherCatastropheCommand(plugin, prefix + "weatherCatastrophe"));
//        plugin.getCommand(prefix + "assault").setExecutor(new AssaultCommand(plugin, prefix + "assault"));
//        plugin.getCommand(prefix + "starSplinter").setExecutor(new StarSplinterCommand(plugin, prefix + "starSplinter"));
//        plugin.getCommand(prefix + "causeStalker").setExecutor(new CauseStalkerCommand(plugin, prefix + "causeStalker"));
//        plugin.getCommand(prefix + "clearStalker").setExecutor(new ClearStalkerCommand(plugin, prefix + "clearStalker"));
//        plugin.getCommand(prefix + "causeHorrificWhispers").setExecutor(new CauseHorrificWhispersCommand(plugin, prefix + "causeHorrificWhispers"));
//
//        plugin.getCommand(prefix + "aspect").setTabCompleter(new AspectCompleter(plugin));
//        plugin.getCommand(prefix + "weatherCatastrophe").setTabCompleter(new WeatherCatastropheCompleter(plugin));
//        plugin.getCommand(prefix + "assault").setTabCompleter(new AssaultCompleter(plugin));
//        plugin.getCommand(prefix + "causeStalker").setTabCompleter(new CauseStalkerCompleter(plugin));
//        plugin.getCommand(prefix + "clearStalker").setTabCompleter(new ClearStalkerCompleter(plugin));
//    }
}
