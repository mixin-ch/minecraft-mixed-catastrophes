package ch.mixin.command.commandList;

import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearStalkerCommand implements CommandExecutor {
    private final MixedCatastrophesPlugin plugin;
    private final String commandName;

    public ClearStalkerCommand(MixedCatastrophesPlugin plugin, String commandName) {
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

        if (!sender.hasPermission("mixedCatastrophePlugin.stalker")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return true;
        }

        if (args.length != 0 && args.length != 1) {
            sender.sendMessage(ChatColor.RED + "/" + commandName + " (<player>)");
            return true;
        }

        Player player;

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a Player.");
                return true;
            }

            player = (Player) sender;
        } else {
            player = plugin.getServer().getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
        }

        plugin.getMetaData().getPlayerDataMap().get((player).getUniqueId()).getTerrorData().getStalkerDatas().clear();
        sender.sendMessage(ChatColor.GREEN + "Stalkers cleared.");
        return true;
    }
}
