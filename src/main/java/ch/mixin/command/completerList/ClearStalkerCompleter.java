package ch.mixin.command.completerList;

import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClearStalkerCompleter implements org.bukkit.command.TabCompleter {
    private final MixedCatastrophesPlugin plugin;

    public ClearStalkerCompleter(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
