package ch.mixin.mixedCatastrophes.command;

import ch.mixin.mixedCatastrophes.command.mxCs.*;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class MxCsCommand extends RootCommand {
    public MxCsCommand(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor, "mx-cs", new HashMap<>());
        subCommandMap.put("help", new HelpCommand(mixedCatastrophesManagerAccessor));
        subCommandMap.put("reload", new ReloadCommand(mixedCatastrophesManagerAccessor));
        subCommandMap.put("catastrophes", new CatastrophesCommand(mixedCatastrophesManagerAccessor));
        subCommandMap.put("aspect", new AspectCommand(mixedCatastrophesManagerAccessor));
        subCommandMap.put("weather", new WeatherCommand(mixedCatastrophesManagerAccessor));
        subCommandMap.put("timeDistortion", new TimeDistortionCommand(mixedCatastrophesManagerAccessor));
        subCommandMap.put("starSplinter", new StarSplinterCommand(mixedCatastrophesManagerAccessor));
        subCommandMap.put("terror", new TerrorCommand(mixedCatastrophesManagerAccessor));

        ConfigurationSection commandsSection = plugin.getConfig().getConfigurationSection("commands");

        if (commandsSection != null) {
            if (commandsSection.getBoolean("dealWithDevil")) {
                subCommandMap.put("dealWithDevil", new DealWithDevilCommand(mixedCatastrophesManagerAccessor));
            }
        }
    }
}
