package ch.mixin.command.mxCs;

import ch.mixin.command.SubCommand;
import ch.mixin.eventChange.aspect.AspectType;
import ch.mixin.main.MixedCatastrophesPlugin;
import ch.mixin.metaData.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StarSplinterCommand extends SubCommand {
    public StarSplinterCommand(MixedCatastrophesPlugin plugin) {
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

        if (!sender.hasPermission("mixedCatastrophes.starSplinter")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return;
        }

        if (arguments.size() != 0 && arguments.size() != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid Argument Number.");
            return;
        }

        if (arguments.size() == 1) {
            Player player = plugin.getServer().getPlayer(arguments.get(0));

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return;
            }

            plugin.getRootCatastropheManager().getStarSplinterCatastropheManager().causeStarSplinter(player);
        } else {
            plugin.getRootCatastropheManager().getStarSplinterCatastropheManager().causeStarSplinter();
        }

        sender.sendMessage(ChatColor.GREEN + "Triggered Star-Splinter.");
    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        if (arguments.size() == 1) {
            return plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
