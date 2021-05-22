package ch.mixin.command;

import ch.mixin.command.mxCs.AspectCommand;
import ch.mixin.command.mxCs.CatastrophesCommand;
import ch.mixin.command.mxCs.WeatherCommand;
import ch.mixin.main.MixedCatastrophesPlugin;

import java.util.HashMap;

public class MxCsCommand extends RootCommand {
    public MxCsCommand(MixedCatastrophesPlugin plugin) {
        super(plugin, "mx-cs", new HashMap<>());
        subCommandMap.put("catastrophes", new CatastrophesCommand(plugin));
        subCommandMap.put("aspect", new AspectCommand(plugin));
        subCommandMap.put("weather", new WeatherCommand(plugin));
        subCommandMap.put("timeDistortion", new WeatherCommand(plugin));
    }
}
