package ch.mixin.mixedCatastrophes.command.mxCs.terror;

import ch.mixin.mixedCatastrophes.command.SubCommand;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WhispersCommand extends SubCommand {
    public WhispersCommand(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
    }

    @Override
    public SubCommand fetchCommand(List<String> arguments) {
        return this;
    }

    @Override
    public void execute(CommandSender sender, List<String> arguments) {
        if (!plugin.PluginFlawless) {
            sender.sendMessage(ChatColor.RED + "Catastrophes has Problems.");
            return;
        }

        if (!mixedCatastrophesData.getMetaData().isActive()) {
            sender.sendMessage(ChatColor.RED + "Catastrophes is inactive.");
            return;
        }

        if (!sender.hasPermission("mixedCatastrophes.whispers")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return;
        }

        if (arguments.size() != 0 && arguments.size() != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid Argument Number.");
            return;
        }

        Player player;

        if (arguments.size() == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a Player.");
                return;
            }

            player = (Player) sender;
        } else {
            player = plugin.getServer().getPlayer(arguments.get(0));

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return;
            }
        }

        mixedCatastrophesData.getRootCatastropheManager().getPersonalCatastropheManager().getTerrorCatastropheManager().triggerHorrificWhispers(player);
        sender.sendMessage(ChatColor.GREEN + "Horrific Whispers caused.");
    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        if (arguments.size() == 1) {
            return plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
