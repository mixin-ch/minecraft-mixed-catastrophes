package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterPremise;
import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterType;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.StarSplinterRemainsData;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.EnderRailData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ActionListener implements Listener {
    private final MixedCatastrophesData mixedCatastrophesData;

    public ActionListener(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    @EventHandler
    public void openMixIslandDictionary(PlayerInteractEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!mixedCatastrophesData.getAffectedWorlds().contains(world))
            return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStack mixIslandDictionary = HelpInventoryManager.HelpBookItem;

        if (itemStack.getType() != mixIslandDictionary.getType())
            return;

        if (!itemStack.getItemMeta().equals(mixIslandDictionary.getItemMeta()))
            return;

        mixedCatastrophesData.getHelpInventoryManager().open(player);
    }

    @EventHandler
    public void dream(PlayerInteractEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!Constants.Beds.contains(event.getClickedBlock().getType()))
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!mixedCatastrophesData.getAffectedWorlds().contains(world))
            return;

        mixedCatastrophesData.getRootCatastropheManager().getPersonalCatastropheManager().getDreamManager().performDream(player, event.getClickedBlock());
    }

    @EventHandler
    public void rite(BlockPlaceEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!mixedCatastrophesData.getAffectedWorlds().contains(world))
            return;

        if (!Constants.Fires.contains(event.getBlockPlaced().getType()))
            return;

        Location location = event.getBlockPlaced().getLocation();
        Location LocationN1 = Coordinate3D.toCoordinate(location).sum(0, -1, 0).toLocation(world);
        Location LocationN2 = Coordinate3D.toCoordinate(LocationN1).sum(0, -1, 0).toLocation(world);
        Block blockN1 = LocationN1.getBlock();
        Block blockN2 = LocationN2.getBlock();

        mixedCatastrophesData.getRootCatastropheManager().getPersonalCatastropheManager().getRiteManager().performRite(player, blockN1, blockN2);
    }

    @EventHandler
    public void crystalDamage(EntityDamageEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        Entity entity = event.getEntity();

        if (entity.getType() != EntityType.ENDER_CRYSTAL)
            return;

        World world = entity.getWorld();

        if (!mixedCatastrophesData.getAffectedWorlds().contains(world))
            return;

        Location location = entity.getLocation();
        StarSplinterRemainsData starSplinter = null;
        List<StarSplinterRemainsData> starSplinterRemainsDataList = mixedCatastrophesData.getMetaData().getStarSplinterRemainsDataList();

        for (int i = 0; i < starSplinterRemainsDataList.size(); i++) {
            StarSplinterRemainsData starSplinterRemainsData = starSplinterRemainsDataList.get(i);
            World remainsWorld = mixedCatastrophesData.getPlugin().getServer().getWorld(starSplinterRemainsData.getWorldName());

            if (remainsWorld == null || !remainsWorld.equals(world))
                continue;

            Location remainsLocation = starSplinterRemainsData.getPosition().toLocation(remainsWorld);

            if (remainsLocation.distance(location) <= 1) {
                starSplinter = starSplinterRemainsData;
                starSplinterRemainsDataList.remove(starSplinterRemainsData);
                break;
            }
        }

        if (starSplinter == null)
            return;

        StarSplinterPremise starSplinterPremise = StarSplinterCatastropheManager.starSplinterPremiseMap.get(starSplinter.getStarSplinterType());
        int amount = (int) Math.round((new Random().nextDouble() + 1) * 5 * starSplinterPremise.getMultiplier());
        world.dropItem(location, new ItemStack(starSplinterPremise.getMaterial(), amount));

        event.setCancelled(true);
        entity.remove();

        world.playSound(entity.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
    }

    @EventHandler
    public void enderRail(PlayerInteractEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!mixedCatastrophesData.getAffectedWorlds().contains(world))
            return;

        if (!event.getAction().equals(Action.PHYSICAL))
            return;

        if (event.getClickedBlock().getType() != Material.LIGHT_WEIGHTED_PRESSURE_PLATE
                && event.getClickedBlock().getType() != Material.HEAVY_WEIGHTED_PRESSURE_PLATE)
            return;

        Location startLocation = event.getClickedBlock().getLocation().add(0, -1, 0);
        Coordinate3D startPosition = Coordinate3D.toCoordinate(startLocation);
        String worldName = world.getName();
        EnderRailData enderRailData = null;
        int rotation = 0;

        List<EnderRailData> railsUp = new ArrayList<>();
        List<EnderRailData> railsDown = new ArrayList<>();
        List<EnderRailData> rails0D = new ArrayList<>();
        List<EnderRailData> rails90D = new ArrayList<>();
        List<EnderRailData> rails180D = new ArrayList<>();
        List<EnderRailData> rails270D = new ArrayList<>();

        enderRailLoop:
        for (EnderRailData erd : mixedCatastrophesData.getMetaData().getEnderRailDataList()) {
            if (!erd.getWorldName().equals(worldName))
                continue;

            boolean active = mixedCatastrophesData.getRootCatastropheManager().getConstructManager().isConstructActive(erd);

            if (!active)
                continue;

            Coordinate3D pos = erd.getPosition();

            if (pos.equals(startPosition)) {
                enderRailData = erd;
                rotation = erd.getRotations();
                continue;
            }

            switch (erd.getDirection()) {
                case Side:
                    if (startPosition.getY() != pos.getY())
                        continue enderRailLoop;

                    switch (erd.getRotations()) {
                        case 0:
                            if (startPosition.getZ() != pos.getZ())
                                continue enderRailLoop;
                            if (startPosition.getX() <= pos.getX())
                                continue enderRailLoop;
                            rails0D.add(erd);
                            break;
                        case 1:
                            if (startPosition.getX() != pos.getX())
                                continue enderRailLoop;
                            if (startPosition.getZ() <= pos.getZ())
                                continue enderRailLoop;
                            rails90D.add(erd);
                            break;
                        case 2:
                            if (startPosition.getZ() != pos.getZ())
                                continue enderRailLoop;
                            if (startPosition.getX() >= pos.getX())
                                continue enderRailLoop;
                            rails180D.add(erd);
                            break;
                        case 3:
                            if (startPosition.getX() != pos.getX())
                                continue enderRailLoop;
                            if (startPosition.getZ() >= pos.getZ())
                                continue enderRailLoop;
                            rails270D.add(erd);
                            break;
                    }
                    break;

                case Up:
                    if (startPosition.getX() != pos.getX())
                        continue enderRailLoop;
                    if (startPosition.getZ() != pos.getZ())
                        continue enderRailLoop;
                    if (startPosition.getY() <= pos.getY())
                        continue enderRailLoop;
                    railsUp.add(erd);
                    break;

                case Down:
                    if (startPosition.getX() != pos.getX())
                        continue enderRailLoop;
                    if (startPosition.getZ() != pos.getZ())
                        continue enderRailLoop;
                    if (startPosition.getY() > pos.getY())
                        continue enderRailLoop;
                    railsDown.add(erd);
                    break;
            }
        }

        if (enderRailData == null)
            return;

        List<EnderRailData> possibleTargets = new ArrayList<>();
        Location endLocationDisplace = new Location(world, 0.5, 0, 0.5);
        Vector playerFacing = player.getLocation().getDirection();
        Vector playerFacingDisplace = playerFacing.clone().setY(0).normalize();

        switch (enderRailData.getDirection()) {
            case Side:
                switch (rotation) {
                    case 0:
                        possibleTargets = rails180D;
                        endLocationDisplace.add(2, 1, 0);
                        break;
                    case 1:
                        possibleTargets = rails270D;
                        endLocationDisplace.add(0, 1, 2);
                        break;
                    case 2:
                        possibleTargets = rails0D;
                        endLocationDisplace.add(-2, 1, 0);
                        break;
                    case 3:
                        possibleTargets = rails90D;
                        endLocationDisplace.add(0, 1, -2);
                        break;
                }
                break;
            case Up:
                possibleTargets = railsDown;
                endLocationDisplace.add(0, 1, 0).add(playerFacingDisplace);
                break;
            case Down:
                possibleTargets = railsUp;
                endLocationDisplace.add(0, 1, 0).add(playerFacingDisplace);
                break;
        }

        if (possibleTargets.isEmpty())
            return;

        EnderRailData closestTarget = null;
        double closestdistance = -1;

        for (EnderRailData target : possibleTargets) {
            double distance = target.getPosition().distance(startPosition);

            if (closestdistance == -1 || distance < closestdistance) {
                closestdistance = distance;
                closestTarget = target;
            }
        }

        Location endLocation = closestTarget.getPosition().toLocation(world).add(endLocationDisplace);
        endLocation.setDirection(playerFacing);
        player.teleport(endLocation);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.ENTITY_ENDERMAN_TELEPORT)
                .execute();

        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getMisfortune().isCollectable())
            return;

        double probability = 0.1 / (enderRailData.getLevel());

        if (new Random().nextDouble() >= probability)
            return;

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Misfortune, 1);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("Something went wrong.")
                .withColor(Constants.AspectThemes.get(AspectType.Misfortune).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }
}
