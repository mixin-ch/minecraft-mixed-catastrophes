package ch.mixin.command.completerList;

import ch.mixin.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherCatastropheCompleter implements org.bukkit.command.TabCompleter {
    private final MixedCatastrophesPlugin plugin;

    public WeatherCatastropheCompleter(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Stream.of(WeatherCatastropheType.values())
                    .map(WeatherCatastropheType::name)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
