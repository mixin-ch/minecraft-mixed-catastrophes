package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterPremise;
import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterType;
import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.metaData.StarSplinterRemainsData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class ActionListener implements Listener {
    private final MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor;

    public ActionListener(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        this.mixedCatastrophesManagerAccessor = mixedCatastrophesManagerAccessor;
    }

    @EventHandler
    public void openMixIslandDictionary(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!mixedCatastrophesManagerAccessor.getAffectedWorlds().contains(world))
            return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStack mixIslandDictionary = HelpInventoryManager.HelpBookItem;

        if (itemStack.getType() != mixIslandDictionary.getType())
            return;

        if (!itemStack.getItemMeta().equals(mixIslandDictionary.getItemMeta()))
            return;

        mixedCatastrophesManagerAccessor.getHelpInventoryManager().open(player);
    }

    @EventHandler
    public void dream(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!Constants.Beds.contains(event.getClickedBlock().getType()))
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!mixedCatastrophesManagerAccessor.getAffectedWorlds().contains(world))
            return;

        mixedCatastrophesManagerAccessor.getRootCatastropheManager().getPersonalCatastropheManager().getDreamManager().performDream(player, event.getClickedBlock());
    }

    @EventHandler
    public void rite(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!mixedCatastrophesManagerAccessor.getAffectedWorlds().contains(world))
            return;

        if (!Constants.Fires.contains(event.getBlockPlaced().getType()))
            return;

        Location location = event.getBlockPlaced().getLocation();
        Location LocationN1 = Coordinate3D.toCoordinate(location).sum(0, -1, 0).toLocation(world);
        Location LocationN2 = Coordinate3D.toCoordinate(LocationN1).sum(0, -1, 0).toLocation(world);
        Block blockN1 = LocationN1.getBlock();
        Block blockN2 = LocationN2.getBlock();

        mixedCatastrophesManagerAccessor.getRootCatastropheManager().getPersonalCatastropheManager().getRiteManager().performRite(player, blockN1, blockN2);
    }

    @EventHandler
    public void crystalDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType() != EntityType.ENDER_CRYSTAL)
            return;

        World world = entity.getWorld();

        if (!mixedCatastrophesManagerAccessor.getAffectedWorlds().contains(world))
            return;

        Location location = entity.getLocation();
        StarSplinterType starSplinterType = null;
        List<StarSplinterRemainsData> starSplinterRemainsDataList = mixedCatastrophesManagerAccessor.getMetaData().getStarSplinterRemainsDataList();

        for (int i = 0; i < starSplinterRemainsDataList.size(); i++) {
            StarSplinterRemainsData starSplinterRemainsData = starSplinterRemainsDataList.get(i);
            World remainsWorld = mixedCatastrophesManagerAccessor.getPlugin().getServer().getWorld(starSplinterRemainsData.getWorldName());

            if (remainsWorld == null || !remainsWorld.equals(world))
                continue;

            Location remainsLocation = starSplinterRemainsData.getPosition().toLocation(remainsWorld);

            if (remainsLocation.distance(location) <= 1) {
                starSplinterType = starSplinterRemainsData.getStarSplinterType();
                starSplinterRemainsDataList.remove(starSplinterRemainsData);
                break;
            }
        }

        if (starSplinterType == null) {
            starSplinterType = Functions.getRandomWithWeights(StarSplinterCatastropheManager.starSplinterWeightMap);
        }

        StarSplinterPremise starSplinterPremise = StarSplinterCatastropheManager.starSplinterPremiseMap.get(starSplinterType);
        int amount = (int) Math.round((new Random().nextDouble() + 1) * 5 * starSplinterPremise.getMultiplier());
        world.dropItem(location, new ItemStack(starSplinterPremise.getMaterial(), amount));

        event.setCancelled(true);
        entity.remove();
    }
}
