package ch.mixin.command.mxCs;

import ch.mixin.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.command.SubCommand;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherCommand extends SubCommand {
    public WeatherCommand(MixedCatastrophesPlugin plugin) {
        super(plugin);
    }

    @Override
    public SubCommand fetchCommand(List<String> arguments) {
        return this;
    }

    @Override
    public void execute(CommandSender sender, List<String> arguments) {
        if (!plugin.getMetaData().isActive()) {
            sender.sendMessage(ChatColor.RED + "Catastrophes is inactive.");
            return;
        }

        if (!sender.hasPermission("mixedCatastrophes.weather")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return;
        }

        if (arguments.size() != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid Argument Number.");
            return;
        }

        WeatherCatastropheType weatherCatastropheType;

        try {
            weatherCatastropheType = WeatherCatastropheType.valueOf(arguments.get(0));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Unknown Weather.");
            return;
        }

        plugin.getRootCatastropheManager().getWeatherCatastropheManager().changeWeather(weatherCatastropheType);
        sender.sendMessage(ChatColor.GREEN + "You successfully caused " + weatherCatastropheType + ".");
    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        if (arguments.size() == 1) {
            return Stream.of(WeatherCatastropheType.values())
                    .map(WeatherCatastropheType::name)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}
