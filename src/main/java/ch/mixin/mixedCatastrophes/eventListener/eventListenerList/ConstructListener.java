package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.constructs.*;
import net.md_5.bungee.api.ChatColor;
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
    protected final MixedCatastrophesPlugin plugin;

    public ConstructListener(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void makeConstruct(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getHand() != EquipmentSlot.HAND)
            return;

        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!plugin.getAffectedWorlds().contains(world))
            return;

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        switch (itemStack.getType()) {
            case ENDER_EYE:
                makeGreenWell(event);
                break;
            case QUARTZ:
                makeBlitzard(event);
                break;
            case GLOWSTONE_DUST:
                makeLighthouse(event);
                break;
            case MAGMA_CREAM:
                makeBlazeReactor(event);
                break;
            case PUMPKIN_PIE:
                makeScarecrow(event);
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

        for (GreenWellData gwd : plugin.getMetaData().getGreenWellDataList()) {
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

        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int cost = 160 + 80 * greenWellData.getLevel();
        int costItem = 1 + greenWellData.getLevel();
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            plugin.getEventChangeManager()
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
            plugin.getMetaData().getGreenWellDataList().add(greenWellData);

        greenWellData.setLevel(greenWellData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Green Well has Depth " + greenWellData.getLevel() + ".")
                .withColor(Constants.AspectThemes.get(AspectType.Nature_Conspiracy).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }

    private void makeBlitzard(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();

        if (block == null)
            return;

        if (block .getType() != Material.QUARTZ_BLOCK)
            return;

        if (!Constants.Blitzard.checkConstructed(block.getLocation()).isConstructed())
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        BlitzardData blitzardData = null;

        for (BlitzardData bd : plugin.getMetaData().getBlitzardDataList()) {
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

        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int multiplier = (int) Math.pow(1 + blitzardData.getLevel(), 2);
        int cost = 100 * multiplier;
        int costItem = multiplier;
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            plugin.getEventChangeManager()
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
            plugin.getMetaData().getBlitzardDataList().add(blitzardData);

        blitzardData.setLevel(blitzardData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Blitzard has Range " + blitzardData.getLevel() * 10 + ".")
                .withColor(ChatColor.DARK_AQUA)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void makeLighthouse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();

        if (block == null)
            return;

        if (block .getType() != Material.GLOWSTONE)
            return;

        if (!Constants.Lighthouse.checkConstructed(block.getLocation()).isConstructed())
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        LighthouseData lighthouseData = null;

        for (LighthouseData ld : plugin.getMetaData().getLighthouseDataList()) {
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

        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        double multiplier = Math.pow(1 + lighthouseData.getLevel(), 1.5);
        int cost = (int) Math.round(100 * multiplier);
        int costItem = (int) Math.round(multiplier);
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            plugin.getEventChangeManager()
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
            plugin.getMetaData().getLighthouseDataList().add(lighthouseData);

        lighthouseData.setLevel(lighthouseData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Lighthouse has Range " + lighthouseData.getLevel() * 10 + ".")
                .withColor(ChatColor.GOLD)
                .withTitle(true)
                .finish()
                .execute();
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

        for (BlazeReactorData brd : plugin.getMetaData().getBlazeReactorList()) {
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

        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int multiplier = (int) Math.pow((1 + blazeReactorData.getLevel()), 2);
        int cost = 250 * multiplier;
        int costItem = 1 * multiplier;
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            plugin.getEventChangeManager()
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
            plugin.getMetaData().getBlazeReactorList().add(blazeReactorData);

        blazeReactorData.setLevel(blazeReactorData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Blaze Reactor has heat " + blazeReactorData.getLevel() + ".")
                .withColor(Constants.AspectThemes.get(AspectType.Misfortune).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }

    private void makeScarecrow(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();

        if (block == null)
            return;

        if (block .getType() != Material.JACK_O_LANTERN)
            return;

        if (!Constants.Scarecrow.checkConstructed(block.getLocation()).isConstructed())
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());

        for (ScarecrowData sd : plugin.getMetaData().getScarecrowDataList()) {
            if (center.equals(sd.getPosition()))
                return;
        }

        ScarecrowData scarecrowData = new ScarecrowData(center, world.getName(), 0);
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int cost = 500;
        int costItem = 1;
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costItem + " Pumpkin Pie to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

        plugin.getMetaData().getScarecrowDataList().add(scarecrowData);
        itemStack.setAmount(itemStack.getAmount() - costItem);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You hear a faint Scream far away.")
                .withColor(Constants.AspectThemes.get(AspectType.Terror).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }
}
