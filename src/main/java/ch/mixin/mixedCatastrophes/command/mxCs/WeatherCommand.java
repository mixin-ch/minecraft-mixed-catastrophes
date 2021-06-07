package ch.mixin.mixedCatastrophes.command.mxCs;

import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.mixedCatastrophes.command.SubCommand;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherCommand extends SubCommand {
    public WeatherCommand(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor);
    }

    @Override
    public SubCommand fetchCommand(List<String> arguments) {
        return this;
    }

    @Override
    public void execute(CommandSender sender, List<String> arguments) {
        if (!mixedCatastrophesManagerAccessor.getMetaData().isActive()) {
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

        mixedCatastrophesManagerAccessor.getRootCatastropheManager().getWeatherCatastropheManager().changeWeather(weatherCatastropheType);
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
