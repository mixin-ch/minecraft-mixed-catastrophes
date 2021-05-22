package ch.mixin.command.mxCs.catastrophes;

import ch.mixin.command.SubCommand;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CatastrophesActivateCommand extends SubCommand {
    public CatastrophesActivateCommand(MixedCatastrophesPlugin plugin) {
        super(plugin);
    }

    @Override
    public SubCommand fetchCommand(List<String> arguments) {
        return this;
    }

    @Override
    public void execute(CommandSender sender, List<String> arguments) {
        if (!sender.hasPermission("mixedCatastrophes.catastrophes")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return;
        }

        if (arguments.size() != 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Argument Number.");
            return;
        }

        plugin.getRootCatastropheManager().start();
        sender.sendMessage(ChatColor.GREEN + "Catastrophes activated.");
    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        return new ArrayList<>();
    }
}
