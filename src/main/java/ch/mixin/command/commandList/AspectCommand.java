package ch.mixin.command.commandList;

import ch.mixin.metaData.PlayerData;
import ch.mixin.eventChange.aspect.AspectType;
import ch.mixin.helperClasses.Functions;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AspectCommand implements CommandExecutor {
    private final MixedCatastrophesPlugin plugin;
    private final String commandName;

    public AspectCommand(MixedCatastrophesPlugin plugin, String commandName) {
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

        if (!sender.hasPermission("mixedCatastrophePlugin.aspect")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return true;
        }

        if (args.length != 2 && args.length != 3) {
            sender.sendMessage(ChatColor.RED + "/" + commandName + " <aspectType> <value> (<player>)");
            return true;
        }

        Player player;

        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a Player.");
                return true;
            }

            player = (Player) sender;
        } else {
            player = plugin.getServer().getPlayer(args[2]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
        }

        AspectType aspectType = Functions.toAspectType(args[0]);

        if (aspectType == null) {
            sender.sendMessage(ChatColor.RED + "Unknown AspectType.");
            String validTypes = Stream.of(AspectType.values())
                    .map(AspectType::name)
                    .collect(Collectors.joining(","));
            sender.sendMessage(ChatColor.RED + "Valid Types are: " + validTypes + ".");
            return true;
        }

        int value;
        try {
            value = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid Value Format.");
            return true;
        }

        if (value < 0) {
            sender.sendMessage(ChatColor.RED + "Value must be greater than 0.");
            return true;
        }

        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(aspectType, value - playerData.getAspect(aspectType));

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .execute();

        sender.sendMessage(ChatColor.GREEN + "Set " + aspectType.getLabel() + " to " + value + ".");
        return true;
    }
}
