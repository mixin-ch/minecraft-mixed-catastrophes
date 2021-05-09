package ch.mixin.command.commandList;

import ch.mixin.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WeatherCatastropheCommand implements CommandExecutor {
    private final MixedCatastrophesPlugin plugin;
    private final String commandName;

    public WeatherCatastropheCommand(MixedCatastrophesPlugin plugin, String commandName) {
        this.plugin = plugin;
        this.commandName = commandName;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.isPluginFlawless()) {
            sender.sendMessage(ChatColor.RED + "Plugin has Problems.");
            return true;
        }

        if (!command.getName().equalsIgnoreCase(commandName)) {
            sender.sendMessage(ChatColor.RED + "Command does not match.");
            return true;
        }

        if (!sender.hasPermission("mixedCatastrophePlugin.weather")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "/" + commandName + " <weatherCatastropheType>");
            return true;
        }

        WeatherCatastropheType weatherCatastropheType = WeatherCatastropheType.convert(args[0]);

        if (weatherCatastropheType == null) {
            sender.sendMessage(ChatColor.RED + "Unknown WeatherCatastropheType.");
            String validTypes = Stream.of(WeatherCatastropheType.values())
                    .map(WeatherCatastropheType::name)
                    .collect(Collectors.joining(","));
            sender.sendMessage(ChatColor.RED + "Valid Types are: " + validTypes + ".");
            return true;
        }

        plugin.getRootCatastropheManager().getWeatherCatastropheManager().changeWeather(weatherCatastropheType);
        return true;
    }
}
