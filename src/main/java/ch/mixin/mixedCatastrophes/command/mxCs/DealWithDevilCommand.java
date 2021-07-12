package ch.mixin.mixedCatastrophes.command.mxCs;

import ch.mixin.mixedCatastrophes.command.SubCommand;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DealWithDevilCommand extends SubCommand {
    public DealWithDevilCommand(MixedCatastrophesData mixedCatastrophesData) {
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

        if (arguments.size() != 0) {
            sender.sendMessage(ChatColor.RED + "Invalid Argument Number.");
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You are not a Player.");
            return;
        }

        Player player = (Player) sender;
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int limit = 5 + playerData.getAspect(AspectType.Death_Seeker);
        if (playerData.getAspect(AspectType.Greyhat_Debt) >= limit) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You currently must have less than " + limit + " Debt to do this.")
                    .withColor(ChatColor.WHITE)
                    .finish()
                    .execute();
            return;
        }

        player.getInventory().addItem(new ItemStack(Material.OAK_SAPLING, 10));
        player.getInventory().addItem(new ItemStack(Material.OAK_LOG, 10));
        player.getInventory().addItem(new ItemStack(Material.DIRT, 32));
        player.getInventory().addItem(new ItemStack(Material.COBBLESTONE, 32));
        player.getInventory().addItem(new ItemStack(Material.COAL, 10));
        player.getInventory().addItem(new ItemStack(Material.APPLE, 10));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Greyhat_Debt, 1);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("An Individual with a grey Hat accepts the Deal.")
                .withColor(Constants.AspectThemes.get(AspectType.Greyhat_Debt).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }

    @Override
    public List<String> getOptions(List<String> arguments) {
        return new ArrayList<>();
    }
}
