package ch.mixin.command.mxCs;

import ch.mixin.command.SubCommand;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends SubCommand {
    public ReloadCommand(MixedCatastrophesPlugin plugin) {
        super(plugin);
    }

    @Override
    public SubCommand fetchCommand(List<String> arguments) {
        return this;
    }

    @Override
    public void execute(CommandSender sender, List<String> arguments) {
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
