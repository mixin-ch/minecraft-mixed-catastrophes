package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ActionListener implements Listener {
    protected final MixedCatastrophesPlugin plugin;

    public ActionListener(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void openMixIslandDictionary(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!plugin.getAffectedWorlds().contains(world))
            return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStack mixIslandDictionary = HelpInventoryManager.HelpBookItem;

        if (itemStack.getType() != mixIslandDictionary.getType())
            return;

        if (!itemStack.getItemMeta().equals(mixIslandDictionary.getItemMeta()))
            return;

        plugin.getHelpInventoryManager().open(player);
    }

    @EventHandler
    public void dream(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!Constants.Beds.contains(event.getClickedBlock().getType()))
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!plugin.getAffectedWorlds().contains(world))
            return;

        plugin.getRootCatastropheManager().getPersonalCatastropheManager().getDreamManager().performDream(player, event.getClickedBlock());
    }

    @EventHandler
    public void rite(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!plugin.getAffectedWorlds().contains(world))
            return;

        if (!Constants.Fires.contains(event.getBlockPlaced().getType()))
            return;

        Location location = event.getBlockPlaced().getLocation();
        Location LocationN1 = Coordinate3D.toCoordinate(location).sum(0, -1, 0).toLocation(world);
        Location LocationN2 = Coordinate3D.toCoordinate(LocationN1).sum(0, -1, 0).toLocation(world);
        Block blockN1 = LocationN1.getBlock();
        Block blockN2 = LocationN2.getBlock();

        plugin.getRootCatastropheManager().getPersonalCatastropheManager().getRiteManager().performRite(player, blockN1, blockN2);
    }
}
