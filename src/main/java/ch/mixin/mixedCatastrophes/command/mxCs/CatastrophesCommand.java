package ch.mixin.mixedCatastrophes.command.mxCs;

import ch.mixin.mixedCatastrophes.command.SubCommand;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CatastrophesCommand extends SubCommand {
    public CatastrophesCommand(MixedCatastrophesData mixedCatastrophesData) {
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
                mixedCatastrophesData.getMetaData().setActive(true);
                mixedCatastrophesData.setFullyFunctional(plugin.isPluginFlawless());
                sender.sendMessage(ChatColor.GREEN + "Catastrophes activated.");
                break;
            case "deactivate":
                mixedCatastrophesData.getMetaData().setActive(false);
                mixedCatastrophesData.setFullyFunctional(false);
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
