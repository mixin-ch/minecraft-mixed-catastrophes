package ch.mixin.mixedCatastrophes.command.mxCs;

import ch.mixin.mixedCatastrophes.command.SubCommand;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StarSplinterCommand extends SubCommand {
    public StarSplinterCommand(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
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

            mixedCatastrophesManagerAccessor.getRootCatastropheManager().getStarSplinterCatastropheManager().causeStarSplinter(player);
        } else {
            mixedCatastrophesManagerAccessor.getRootCatastropheManager().getStarSplinterCatastropheManager().causeStarSplinter();
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
