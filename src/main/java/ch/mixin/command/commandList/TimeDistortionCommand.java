package ch.mixin.command.commandList;

import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TimeDistortionCommand implements CommandExecutor {
    private final MixedCatastrophesPlugin plugin;
    private final String commandName;

    public TimeDistortionCommand(MixedCatastrophesPlugin plugin, String commandName) {
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

        if (!sender.hasPermission("mixedCatastrophePlugin.timeDistortion")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return true;
        }

        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "/" + commandName);
            return true;
        }

        plugin.getRootCatastropheManager().getTimeDistortionManager().causeTimeDistortion();
        sender.sendMessage(ChatColor.GREEN + "Successfully caused TimeDistortion.");
        return true;
    }
}
