package ch.mixin.command.commandList;

import ch.mixin.MetaData.PlayerData;
import ch.mixin.eventChange.aspect.AspectType;
import ch.mixin.helperClasses.Constants;
import ch.mixin.main.MixedCatastrophesPlugin;
import ch.mixin.eventChange.message.Messager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class DealWithDevilCommand implements CommandExecutor {
    private final MixedCatastrophesPlugin plugin;
    private final String commandName;

    public DealWithDevilCommand(MixedCatastrophesPlugin plugin, String commandName) {
        this.plugin = plugin;
        this.commandName = commandName;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.isPluginFlawless()) {
            sender.sendMessage(ChatColor.RED + "Plugin has Problems.");
            return true;
        }

        if (!command.getName().equalsIgnoreCase(commandName)) {
            sender.sendMessage(ChatColor.RED + "Command does not match.");
            return true;
        }

        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "/" + commandName);
            return true;
        }

        Player player;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You are not a Player.");
            return true;
        }

        player = (Player) sender;
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int limit = 5 + playerData.getAspect(AspectType.Death_Seeker);
        if (playerData.getAspect(AspectType.Greyhat_Debt) >= limit) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You currently must have less than " + limit + " Debt to do this.")
                    .withColor(ChatColor.WHITE)
                    .finish()
                    .execute();
            return true;
        }

        player.getInventory().addItem(new ItemStack(Material.OAK_SAPLING, 10));
        player.getInventory().addItem(new ItemStack(Material.OAK_LOG, 10));
        player.getInventory().addItem(new ItemStack(Material.DIRT, 32));
        player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 32));
        player.getInventory().addItem(new ItemStack(Material.COAL, 10));
        player.getInventory().addItem(new ItemStack(Material.APPLE, 10));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Greyhat_Debt, 1);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("An Individual with a grey Hat accepts the Deal.")
                .withColor(Constants.AspectThemes.get(AspectType.Greyhat_Debt))
                .withTitle(true)
                .finish()
                .execute();
        return true;
    }
}
