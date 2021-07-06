package ch.mixin.mixedCatastrophes.command;

import ch.mixin.mixedCatastrophes.command.mxCs.*;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class MxCsCommand extends RootCommand {
    public MxCsCommand(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData, "mx-cs", new HashMap<>());
        subCommandMap.put("help", new HelpCommand(mixedCatastrophesData));
        subCommandMap.put("reload", new ReloadCommand(mixedCatastrophesData));
        subCommandMap.put("catastrophes", new CatastrophesCommand(mixedCatastrophesData));
        subCommandMap.put("aspect", new AspectCommand(mixedCatastrophesData));
        subCommandMap.put("weather", new WeatherCommand(mixedCatastrophesData));
        subCommandMap.put("timeDistortion", new TimeDistortionCommand(mixedCatastrophesData));
        subCommandMap.put("starSplinter", new StarSplinterCommand(mixedCatastrophesData));
        subCommandMap.put("terror", new TerrorCommand(mixedCatastrophesData));

        ConfigurationSection commandsSection = plugin.getConfig().getConfigurationSection("commands");

        if (commandsSection != null) {
            if (commandsSection.getBoolean("dealWithDevil")) {
                subCommandMap.put("dealWithDevil", new DealWithDevilCommand(mixedCatastrophesData));
            }
        }
    }
}
