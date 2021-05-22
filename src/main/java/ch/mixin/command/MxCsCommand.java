package ch.mixin.command;

import ch.mixin.command.mxCs.*;
import ch.mixin.main.MixedCatastrophesPlugin;

import java.util.HashMap;

public class MxCsCommand extends RootCommand {
    public MxCsCommand(MixedCatastrophesPlugin plugin) {
        super(plugin, "mx-cs", new HashMap<>());
        subCommandMap.put("catastrophes", new CatastrophesCommand(plugin));
        subCommandMap.put("aspect", new AspectCommand(plugin));
        subCommandMap.put("weather", new WeatherCommand(plugin));
        subCommandMap.put("timeDistortion", new TimeDistortionCommand(plugin));
        subCommandMap.put("starSplinter", new StarSplinterCommand(plugin));
        subCommandMap.put("terror", new TerrorCommand(plugin));
    }
}
