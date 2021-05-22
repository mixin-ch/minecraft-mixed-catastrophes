package ch.mixin.command;

import ch.mixin.command.mx.DealWithDevilCommand;
import ch.mixin.command.mx.HelpCommand;
import ch.mixin.command.mxCs.WeatherCommand;
import ch.mixin.main.MixedCatastrophesPlugin;

import java.util.HashMap;

public class MxCommand extends RootCommand {
    public MxCommand(MixedCatastrophesPlugin plugin) {
        super(plugin, "mx", new HashMap<>());
        subCommandMap.put("help", new HelpCommand(plugin));
        subCommandMap.put("dealWithDevil", new DealWithDevilCommand(plugin));
    }
}
