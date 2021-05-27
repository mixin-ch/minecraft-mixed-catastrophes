package ch.mixin.mixedCatastrophes.command;

import ch.mixin.mixedCatastrophes.command.mxCs.*;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class MxCsCommand extends RootCommand {
    public MxCsCommand(MixedCatastrophesPlugin plugin) {
        super(plugin, "mx-cs", new HashMap<>());
        subCommandMap.put("help", new HelpCommand(plugin));
        subCommandMap.put("reload", new ReloadCommand(plugin));
        subCommandMap.put("catastrophes", new CatastrophesCommand(plugin));
        subCommandMap.put("aspect", new AspectCommand(plugin));
        subCommandMap.put("weather", new WeatherCommand(plugin));
        subCommandMap.put("timeDistortion", new TimeDistortionCommand(plugin));
        subCommandMap.put("starSplinter", new StarSplinterCommand(plugin));
        subCommandMap.put("terror", new TerrorCommand(plugin));

        ConfigurationSection commandsSection = plugin.getConfig().getConfigurationSection("commands");

        if (commandsSection != null) {
            if (commandsSection.getBoolean("dealWithDevil")) {
                subCommandMap.put("dealWithDevil", new DealWithDevilCommand(plugin));
            }
        }
    }
}
