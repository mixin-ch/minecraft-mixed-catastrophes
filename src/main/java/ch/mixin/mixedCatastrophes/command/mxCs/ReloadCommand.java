package ch.mixin.mixedCatastrophes.command.mxCs;

import ch.mixin.mixedCatastrophes.command.SubCommand;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends SubCommand {
    public ReloadCommand(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
    }

    @Override
    public SubCommand fetchCommand(List<String> arguments) {
        return this;
    }

    @Override
    public void execute(CommandSender sender, List<String> arguments) {
        if (!plugin.isPluginFlawless()) {
            sender.sendMessage(ChatColor.RED + "Catastrophes has Problems.");
            return;
        }

        if (!sender.hasPermission("mixedCatastrophes.reload")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return;
        }

        if (arguments.size() != 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Argument Number.");
            return;
        }

        plugin.reload();
        sender.sendMessage(ChatColor.GREEN + "Successfully reloaded MixedCatastrophes.");
    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        return new ArrayList<>();
    }
}
