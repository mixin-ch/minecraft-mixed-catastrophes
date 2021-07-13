package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.EnderRailDirection;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.*;
import ch.mixin.mixedCatastrophes.mixedAchievements.MixedAchievementsManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class ConstructListener implements Listener {
    private final MixedCatastrophesData mixedCatastrophesData;

    public ConstructListener(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    @EventHandler
    public void makeConstruct(PlayerInteractEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getHand() != EquipmentSlot.HAND)
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!mixedCatastrophesData.getAffectedWorlds().contains(world))
            return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        switch (itemStack.getType()) {
            case BONE_MEAL:
                makeGreenWell(event);
                break;
            case MAGMA_CREAM:
                makeBlazeReactor(event);
                break;
            case QUARTZ:
                makeBlitzard(event);
                break;
            case GLOWSTONE_DUST:
                makeLighthouse(event);
                break;
            case PUMPKIN_PIE:
                makeScarecrow(event);
                break;
            case ENDER_EYE:
                makeEnderRail(event);
                break;
        }
    }

    private void makeGreenWell(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = null;
        List<Block> lineOfSight = event.getPlayer().getLineOfSight(null, 5);

        for (Block b : lineOfSight) {
            if (b.getType() == Material.WATER) {
                block = b;
                break;
            }
        }

        if (block == null)
            return;

        if (!Constants.GreenWell.checkConstructed(block.getLocation()).isConstructed())
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        GreenWellData greenWellData = null;

        for (GreenWellData gwd : mixedCatastrophesData.getMetaData().getGreenWellDataList()) {
            if (center.equals(gwd.getPosition())) {
                greenWellData = gwd;
                break;
            }
        }

        boolean isNew = false;

        if (greenWellData == null) {
            greenWellData = new GreenWellData(center, world.getName(), 0);
            isNew = true;
        }

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int cost = 100 * (2 + greenWellData.getLevel());
        int costItem = 4 * (2 + greenWellData.getLevel());
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costItem + " Bone Meal to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

        if (isNew)
            mixedCatastrophesData.getMetaData().getGreenWellDataList().add(greenWellData);

        greenWellData.setLevel(greenWellData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Green Well has Depth " + greenWellData.getLevel() + ".")
                .withColor(Constants.ConstructThemes.get(ConstructType.GreenWell).getColor())
                .withTitle(true)
                .finish()
                .execute();

        MixedAchievementsManager mixedAchievementsManager = mixedCatastrophesData.getMixedAchievementsManager();

        if (!mixedAchievementsManager.isActive())
            return;

        mixedAchievementsManager.updateConstructAchievementProgress(
                player, ConstructType.GreenWell, greenWellData.getLevel());
    }


    private void makeBlazeReactor(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = null;
        List<Block> lineOfSight = event.getPlayer().getLineOfSight(null, 5);

        for (Block b : lineOfSight) {
            if (b.getType() == Material.LAVA) {
                block = b;
                break;
            }
        }

        if (block == null)
            return;

        if (!Constants.BlazeReactor.checkConstructed(block.getLocation()).isConstructed())
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        BlazeReactorData blazeReactorData = null;

        for (BlazeReactorData brd : mixedCatastrophesData.getMetaData().getBlazeReactorList()) {
            if (center.equals(brd.getPosition())) {
                blazeReactorData = brd;
                break;
            }
        }

        boolean isNew = false;

        if (blazeReactorData == null) {
            blazeReactorData = new BlazeReactorData(center, world.getName(), 0, 0);
            isNew = true;
        }

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int multiplier = (int) Math.pow((1 + blazeReactorData.getLevel()), 2);
        int cost = 250 * multiplier;
        int costItem = 1 * multiplier;
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costItem + " Magma Cream to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

        if (isNew)
            mixedCatastrophesData.getMetaData().getBlazeReactorList().add(blazeReactorData);

        blazeReactorData.setLevel(blazeReactorData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Blaze Reactor has heat " + blazeReactorData.getLevel() + ".")
                .withColor(Constants.ConstructThemes.get(ConstructType.BlazeReactor).getColor())
                .withTitle(true)
                .finish()
                .execute();

        MixedAchievementsManager mixedAchievementsManager = mixedCatastrophesData.getMixedAchievementsManager();

        if (!mixedAchievementsManager.isActive())
            return;

        mixedAchievementsManager.updateConstructAchievementProgress(
                player, ConstructType.BlazeReactor, blazeReactorData.getLevel());
    }

    private void makeBlitzard(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();

        if (block == null)
            return;

        if (block.getType() != Material.QUARTZ_BLOCK)
            return;

        if (!Constants.Blitzard.checkConstructed(block.getLocation()).isConstructed())
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        BlitzardData blitzardData = null;

        for (BlitzardData bd : mixedCatastrophesData.getMetaData().getBlitzardDataList()) {
            if (center.equals(bd.getPosition())) {
                blitzardData = bd;
                break;
            }
        }

        boolean isNew = false;

        if (blitzardData == null) {
            blitzardData = new BlitzardData(center, world.getName(), 0);
            isNew = true;
        }

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int multiplier = (int) Math.pow(1 + blitzardData.getLevel(), 2);
        int cost = 100 * multiplier;
        int costItem = 1 * multiplier;
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costItem + " Quartz to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

        if (isNew)
            mixedCatastrophesData.getMetaData().getBlitzardDataList().add(blitzardData);

        blitzardData.setLevel(blitzardData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Blitzard has Range " + blitzardData.getLevel() * Constants.BlitzardRangeFactor + ".")
                .withColor(Constants.ConstructThemes.get(ConstructType.Blitzard).getColor())
                .withTitle(true)
                .finish()
                .execute();

        MixedAchievementsManager mixedAchievementsManager = mixedCatastrophesData.getMixedAchievementsManager();

        if (!mixedAchievementsManager.isActive())
            return;

        mixedAchievementsManager.updateConstructAchievementProgress(
                player, ConstructType.Blitzard, blitzardData.getLevel());
    }

    private void makeLighthouse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();

        if (block == null)
            return;

        if (block.getType() != Material.GLOWSTONE)
            return;

        if (!Constants.Lighthouse.checkConstructed(block.getLocation()).isConstructed())
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        LighthouseData lighthouseData = null;

        for (LighthouseData ld : mixedCatastrophesData.getMetaData().getLighthouseDataList()) {
            if (center.equals(ld.getPosition())) {
                lighthouseData = ld;
                break;
            }
        }

        boolean isNew = false;

        if (lighthouseData == null) {
            lighthouseData = new LighthouseData(center, world.getName(), 0);
            isNew = true;
        }

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        double multiplier = Math.pow(1 + lighthouseData.getLevel(), 1.5);
        int cost = (int) Math.round(100 * multiplier);
        int costItem = (int) Math.round(multiplier);
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costItem + " Glowstone to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

        if (isNew)
            mixedCatastrophesData.getMetaData().getLighthouseDataList().add(lighthouseData);

        lighthouseData.setLevel(lighthouseData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Lighthouse has Range " + lighthouseData.getLevel() * Constants.LighthouseRangeFactor + ".")
                .withColor(Constants.ConstructThemes.get(ConstructType.Lighthouse).getColor())
                .withTitle(true)
                .finish()
                .execute();

        MixedAchievementsManager mixedAchievementsManager = mixedCatastrophesData.getMixedAchievementsManager();

        if (!mixedAchievementsManager.isActive())
            return;

        mixedAchievementsManager.updateConstructAchievementProgress(
                player, ConstructType.Lighthouse, lighthouseData.getLevel());
    }

    private void makeScarecrow(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();

        if (block == null)
            return;

        if (block.getType() != Material.JACK_O_LANTERN)
            return;

        if (!Constants.Scarecrow.checkConstructed(block.getLocation()).isConstructed())
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());

        for (ScarecrowData sd : mixedCatastrophesData.getMetaData().getScarecrowDataList()) {
            if (center.equals(sd.getPosition()))
                return;
        }

        ScarecrowData scarecrowData = new ScarecrowData(center, world.getName(), 0);
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int cost = 500;
        int costItem = 1;
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costItem + " Pumpkin Pie to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

        mixedCatastrophesData.getMetaData().getScarecrowDataList().add(scarecrowData);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You hear a faint Scream far away.")
                .withColor(Constants.ConstructThemes.get(ConstructType.Scarecrow).getColor())
                .withTitle(true)
                .finish()
                .execute();

        MixedAchievementsManager mixedAchievementsManager = mixedCatastrophesData.getMixedAchievementsManager();

        if (!mixedAchievementsManager.isActive())
            return;

        mixedAchievementsManager.updateConstructAchievementProgress(
                player, ConstructType.Scarecrow, 1);
    }

    private void makeEnderRail(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();

        if (block == null)
            return;

        if (block.getType() != Material.LAPIS_BLOCK)
            return;

        EnderRailDirection direction;

        if (Constants.EnderRail_Side.checkConstructed(block.getLocation()).isConstructed()) {
            direction = EnderRailDirection.Side;
        } else if (Constants.EnderRail_Up.checkConstructed(block.getLocation()).isConstructed()) {
            direction = EnderRailDirection.Up;
        } else if (Constants.EnderRail_Down.checkConstructed(block.getLocation()).isConstructed()) {
            direction = EnderRailDirection.Down;
        } else {
            return;
        }

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        EnderRailData enderRailData = null;

        for (EnderRailData data : mixedCatastrophesData.getMetaData().getEnderRailDataList()) {
            if (center.equals(data.getPosition())) {
                enderRailData = data;
                break;
            }
        }

        boolean isNew = false;

        if (enderRailData == null) {
            enderRailData = new EnderRailData(center, world.getName(), 0, direction);
            isNew = true;
        }

        if (enderRailData.getDirection() != direction)
            return;

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        double multiplier = Functions.logarithm(2 + enderRailData.getLevel(), 2);
        int cost = (int) Math.round(250 * multiplier);
        int costItem = (int) Math.round(multiplier);
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costItem + " Ender Eyes to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

        if (isNew)
            mixedCatastrophesData.getMetaData().getEnderRailDataList().add(enderRailData);

        enderRailData.setLevel(enderRailData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Ender Rail has Range " + enderRailData.getLevel() * Constants.EnderRailRangeFactor + ".")
                .withColor(Constants.ConstructThemes.get(ConstructType.EnderRail).getColor())
                .withTitle(true)
                .finish()
                .execute();

        MixedAchievementsManager mixedAchievementsManager = mixedCatastrophesData.getMixedAchievementsManager();

        if (!mixedAchievementsManager.isActive())
            return;

        mixedAchievementsManager.updateConstructAchievementProgress(
                player, ConstructType.EnderRail, enderRailData.getLevel());
    }
}
