package ch.mixin.mixedCatastrophes.command.mxCs;

import ch.mixin.mixedCatastrophes.command.SubCommand;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CatastrophesCommand extends SubCommand {
    public CatastrophesCommand(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor);
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

        if (arguments.size() != 1) {
            sender.sendMessage(ChatColor.RED + "Invalid Argument Number.");
            return;
        }

        String argument = arguments.get(0);

        switch (argument) {
            case "activate":
                mixedCatastrophesManagerAccessor.getRootCatastropheManager().start();
                sender.sendMessage(ChatColor.GREEN + "Catastrophes activated.");
                break;
            case "deactivate":
                mixedCatastrophesManagerAccessor.getRootCatastropheManager().stop();
                sender.sendMessage(ChatColor.GREEN + "Catastrophes deactivated.");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Invalid Argument.");
                break;
        }


    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        List<String> options = new ArrayList<>();

        if (arguments.size() == 1) {
            options.add("activate");
            options.add("deactivate");
        }

        return options;
    }
}
