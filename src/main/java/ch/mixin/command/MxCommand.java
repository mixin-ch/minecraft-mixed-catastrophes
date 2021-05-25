package ch.mixin.command;

import ch.mixin.command.mx.DealWithDevilCommand;
import ch.mixin.command.mx.HelpCommand;
import ch.mixin.command.mxCs.WeatherCommand;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class MxCommand extends RootCommand {
    public MxCommand(MixedCatastrophesPlugin plugin) {
        super(plugin, "mx", new HashMap<>());
        subCommandMap.put("help", new HelpCommand(plugin));

        ConfigurationSection commandsSection = plugin.getConfig().getConfigurationSection("commands");

        if (commandsSection != null) {
            if (commandsSection.getBoolean("dealWithDevil")) {
                subCommandMap.put("dealWithDevil", new DealWithDevilCommand(plugin));
            }
        }
    }
}
