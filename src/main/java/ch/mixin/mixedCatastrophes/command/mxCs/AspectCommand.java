package ch.mixin.mixedCatastrophes.command.mxCs;

import ch.mixin.mixedCatastrophes.command.SubCommand;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AspectCommand extends SubCommand {
    public AspectCommand(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor);
    }

    @Override
    public SubCommand fetchCommand(List<String> arguments) {
        return this;
    }

    @Override
    public void execute(CommandSender sender, List<String> arguments) {
        if (!sender.hasPermission("mixedCatastrophes.aspect")) {
            sender.sendMessage(ChatColor.RED + "You lack Permission.");
            return;
        }

        if (arguments.size() != 2 && arguments.size() != 3) {
            sender.sendMessage(ChatColor.RED + "Invalid Argument Number.");
            return;
        }

        Player player;

        if (arguments.size() == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You are not a Player.");
                return;
            }

            player = (Player) sender;
        } else {
            player = plugin.getServer().getPlayer(arguments.get(2));

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return;
            }
        }

        AspectType aspectType;

        try {
            aspectType = AspectType.valueOf(arguments.get(0));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Unknown Aspect.");
            return;
        }

        int value;
        try {
            value = Integer.parseInt(arguments.get(1));
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid Value Format.");
            return;
        }

        if (value < 0) {
            sender.sendMessage(ChatColor.RED + "Value must be at least 0.");
            return;
        }

        PlayerData playerData = mixedCatastrophesManagerAccessor.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(aspectType, value - playerData.getAspect(aspectType));

        mixedCatastrophesManagerAccessor.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .execute();

        sender.sendMessage(ChatColor.GREEN + "Set " + aspectType.getLabel() + " to " + value + ".");
    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        List<String> options = new ArrayList<>();

        if (arguments.size() == 1) {
            for (AspectType aspectType : AspectType.values()) {
                options.add(aspectType.toString());
            }
        } else if (arguments.size() == 2) {
            options.add("<value>");
        } else if (arguments.size() == 3) {
            options.addAll(plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        }

        return options;
    }
}
