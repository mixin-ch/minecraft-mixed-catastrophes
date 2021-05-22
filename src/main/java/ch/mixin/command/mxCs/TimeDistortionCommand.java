package ch.mixin.command.mxCs;

import ch.mixin.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.command.SubCommand;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TimeDistortionCommand extends SubCommand {
    public TimeDistortionCommand(MixedCatastrophesPlugin plugin) {
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

        if (!sender.hasPermission("mixedCatastrophes.timeDistortion")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return;
        }

        if (arguments.size() != 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Argument Number.");
            return;
        }

        plugin.getRootCatastropheManager().getTimeDistortionManager().causeTimeDistortion();
        sender.sendMessage(ChatColor.GREEN + "Successfully caused TimeDistortion.");
    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        return new ArrayList<>();
    }
}
