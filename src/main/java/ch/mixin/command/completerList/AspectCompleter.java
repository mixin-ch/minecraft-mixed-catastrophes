package ch.mixin.command.completerList;

import ch.mixin.eventChange.aspect.AspectType;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AspectCompleter implements org.bukkit.command.TabCompleter {
    private final MixedCatastrophesPlugin plugin;

    public AspectCompleter(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Stream.of(AspectType.values())
                    .map(AspectType::name)
                    .collect(Collectors.toList());
        }

        if (args.length == 3) {
            return plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }
}
