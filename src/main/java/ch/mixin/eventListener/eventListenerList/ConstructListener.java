package ch.mixin.eventListener.eventListenerList;

import ch.mixin.MetaData.BlitzardData;
import ch.mixin.MetaData.GreenWellData;
import ch.mixin.MetaData.LighthouseData;
import ch.mixin.MetaData.PlayerData;
import ch.mixin.eventChange.aspect.AspectType;
import ch.mixin.helperClasses.Constants;
import ch.mixin.helperClasses.Coordinate3D;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.ChatColor;
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

        if (!Constants.GreenWell.isConstructed(block.getLocation()))
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        GreenWellData greenWellData = null;

        for (GreenWellData gwd : plugin.getMetaData().getGreenWellDataList()) {
            if (center.equals(gwd.getPosition())) {
                greenWellData = gwd;
                break;
            }
        }

        if (greenWellData == null) {
            greenWellData = new GreenWellData(center, world.getName(), 0);
            plugin.getMetaData().getGreenWellDataList().add(greenWellData);
        } else if (block.getType() != Material.WATER) {
            return;
        }

        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int cost = 160 + 80 * greenWellData.getLevel();
        int costEyes = 1 + (int) Math.floor(0.5 * greenWellData.getLevel());
        boolean success = true;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets))
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costEyes) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costEyes + " Ender Eyes to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets))
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

        greenWellData.setLevel(greenWellData.getLevel() + 1);
        itemStack.setAmount(itemStack.getAmount() - costEyes);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Green Well has Depth " + greenWellData.getLevel() + ".")
                .withColor(Constants.AspectThemes.get(AspectType.Nature_Conspiracy))
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

        if (!Constants.Blitzard.isConstructed(block.getLocation()))
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        BlitzardData blitzardData = null;

        for (BlitzardData bd : plugin.getMetaData().getBlitzardDataList()) {
            if (center.equals(bd.getPosition())) {
                blitzardData = bd;
                break;
            }
        }

        if (blitzardData == null) {
            blitzardData = new BlitzardData(center, world.getName(), 0);
            plugin.getMetaData().getBlitzardDataList().add(blitzardData);
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
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets))
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costItem + " Quartz to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets))
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

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

        if (!Constants.Lighthouse.isConstructed(block.getLocation()))
            return;

        Coordinate3D center = Coordinate3D.toCoordinate(block.getLocation());
        LighthouseData lighthouseData = null;

        for (LighthouseData ld : plugin.getMetaData().getLightHouseDataList()) {
            if (center.equals(ld.getPosition())) {
                lighthouseData = ld;
                break;
            }
        }

        if (lighthouseData == null) {
            lighthouseData = new LighthouseData(center, world.getName(), 0);
            plugin.getMetaData().getLightHouseDataList().add(lighthouseData);
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
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets))
                    .finish()
                    .execute();
            success = false;
        }

        if (itemStack.getAmount() < costItem) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + costItem + " Glowstone to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets))
                    .finish()
                    .execute();
            success = false;
        }

        if (!success)
            return;

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
}
