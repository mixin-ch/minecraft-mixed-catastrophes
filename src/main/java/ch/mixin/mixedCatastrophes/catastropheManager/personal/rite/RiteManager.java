package ch.mixin.mixedCatastrophes.catastropheManager.personal.rite;

import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiteManager {
    private final MixedCatastrophesPlugin plugin;

    public RiteManager(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    public void performRite(Player player, Block blockN1, Block blockN2) {
        Material materialN1 = blockN1.getType();
        Material materialN2 = blockN2.getType();

        switch (materialN2) {
            case STONE_BRICKS:
                sacrificeResources(player, blockN1, blockN2);
                break;
            case GLASS:
                produceResources(player, blockN1, blockN2);
                break;
            case QUARTZ_BLOCK:
                removeCurse(player, blockN1, blockN2);
                break;
            case GLOWSTONE:
                receiveBlessing(player, blockN1, blockN2);
                break;
            default:
                return;
        }
    }

    private void sacrificeResources(Player player, Block blockN1, Block blockN2) {
        Material materialN1 = blockN1.getType();
        Material materialN2 = blockN2.getType();

        double multiplier = 0;

        switch (materialN1) {
            case QUARTZ_BLOCK:
                multiplier = 0.1;
                break;
            case IRON_BLOCK:
                multiplier = 1;
                break;
            case LAPIS_BLOCK:
                multiplier = 1;
                break;
            case REDSTONE_BLOCK:
                multiplier = 1;
                break;
            case GOLD_BLOCK:
                multiplier = 2;
                break;
            case EMERALD_BLOCK:
                multiplier = 8;
                break;
            case DIAMOND_BLOCK:
                multiplier = 16;
                break;
            case NETHERITE_BLOCK:
                multiplier = 16;
                break;
            default:
                return;
        }

        blockN1.setType(Material.COBBLESTONE);
        blockN2.setType(Material.COBBLESTONE);

        int gain = (int) Math.round(200 * multiplier);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, gain);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You gain Insight in the World.")
                .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                .withTitle(true)
                .finish()
                .execute();

        ritesParticles(player.getWorld(), Coordinate3D.toCoordinate(blockN1.getLocation()), multiplier * 4);
    }

    private void produceResources(Player player, Block blockN1, Block blockN2) {
        Material materialN1 = blockN1.getType();
        Material materialN2 = blockN2.getType();

        double multiplier = 0;

        switch (materialN1) {
            case QUARTZ_BLOCK:
                multiplier = 0.1;
                break;
            case COAL_BLOCK:
                multiplier = 0.2;
                break;
            case IRON_BLOCK:
                multiplier = 1;
                break;
            case LAPIS_BLOCK:
                multiplier = 1;
                break;
            case REDSTONE_BLOCK:
                multiplier = 1;
                break;
            case GOLD_BLOCK:
                multiplier = 2;
                break;
            case EMERALD_BLOCK:
                multiplier = 8;
                break;
            case DIAMOND_BLOCK:
                multiplier = 16;
                break;
            case NETHERITE_BLOCK:
                multiplier = 16;
                break;
            default:
                return;
        }

        int cost = (int) Math.round(400 * multiplier);
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        blockN2.setType(materialN1);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You traded Secrets for material Gain.")
                .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                .withTitle(true)
                .finish()
                .execute();

        ritesParticles(player.getWorld(), Coordinate3D.toCoordinate(blockN1.getLocation()), multiplier * 4);
    }

    private void removeCurse(Player player, Block blockN1, Block blockN2) {
        Material materialN1 = blockN1.getType();
        Material materialN2 = blockN2.getType();

        switch (materialN1) {
            case SOUL_SAND:
                removeNatureConspiracy(player, blockN1, blockN2);
                break;
            case BOOKSHELF:
                removeMisfortune(player, blockN1, blockN2);
                break;
            case TERRACOTTA:
                removeTerror(player, blockN1, blockN2);
                break;
        }
    }

    private void removeNatureConspiracy(Player player, Block blockN1, Block blockN2) {
        int cost = 1000;
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Nature_Conspiracy) <= 0) {
            return;
        }

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        blockN1.setType(Material.COBBLESTONE);
        blockN2.setType(Material.COBBLESTONE);

        int curseMod = (int) -Math.round(playerData.getAspect(AspectType.Nature_Conspiracy) * 0.5);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);
        changeMap.put(AspectType.Nature_Conspiracy, curseMod);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Green Ones accept your Sacrifice.")
                .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                .withTitle(true)
                .finish()
                .execute();

        ritesParticles(player.getWorld(), Coordinate3D.toCoordinate(blockN1.getLocation()), cost * 0.03);
    }

    private void removeMisfortune(Player player, Block blockN1, Block blockN2) {
        int cost = 1000;
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Misfortune) <= 0) {
            return;
        }

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        blockN1.setType(Material.COBBLESTONE);
        blockN2.setType(Material.COBBLESTONE);

        int curseMod = (int) -Math.round(playerData.getAspect(AspectType.Misfortune) * 0.5);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);
        changeMap.put(AspectType.Misfortune, curseMod);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("With the Books burns also your tainted Fate.")
                .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                .withTitle(true)
                .finish()
                .execute();

        ritesParticles(player.getWorld(), Coordinate3D.toCoordinate(blockN1.getLocation()), cost * 0.03);
    }

    private void removeTerror(Player player, Block blockN1, Block blockN2) {
        int cost = 500;
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Terror) <= 0) {
            return;
        }

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        blockN1.setType(Material.COBBLESTONE);
        blockN2.setType(Material.COBBLESTONE);

        int curseMod = (int) -Math.round(playerData.getAspect(AspectType.Terror) * 0.5);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);
        changeMap.put(AspectType.Terror, curseMod);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Terrors of the World diminish.")
                .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                .withTitle(true)
                .finish()
                .execute();

        ritesParticles(player.getWorld(), Coordinate3D.toCoordinate(blockN1.getLocation()), cost * 0.03);
    }

    private void receiveBlessing(Player player, Block blockN1, Block blockN2) {
        Material materialN1 = blockN1.getType();
        Material materialN2 = blockN2.getType();

        switch (materialN1) {
            case LAPIS_BLOCK:
                gainCelestialFavor(player, blockN1, blockN2);
                break;
            case BOOKSHELF:
                gainTerror(player, blockN1, blockN2);
                break;
        }
    }

    private void gainCelestialFavor(Player player, Block blockN1, Block blockN2) {
        int cost = 750;
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        blockN1.setType(Material.COBBLESTONE);
        blockN2.setType(Material.COBBLESTONE);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);
        changeMap.put(AspectType.Celestial_Favor, 1);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You gained the Favor of an ambitious Star.")
                .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                .withTitle(true)
                .finish()
                .execute();

        ritesParticles(player.getWorld(), Coordinate3D.toCoordinate(blockN1.getLocation()), cost * 0.03);
    }

    private void gainTerror(Player player, Block blockN1, Block blockN2) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int limit = 150;
        int cost = 50 + playerData.getAspect(AspectType.Terror);

        if (playerData.getAspect(AspectType.Terror) > limit) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You can have at most " + limit + " Terror to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        blockN1.setType(Material.COBBLESTONE);
        blockN2.setType(Material.COBBLESTONE);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);
        changeMap.put(AspectType.Terror, 50);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The burning Symbols reveal the Terror of the World.")
                .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                .withTitle(true)
                .finish()
                .execute();

        ritesParticles(player.getWorld(), Coordinate3D.toCoordinate(blockN1.getLocation()), cost * 0.03);
    }

    private void ritesParticles(World world, Coordinate3D topBlock, double multiplier) {
        double root = Math.pow(multiplier, 0.5);
        List<Coordinate3D> particles = new ArrayList<>();
        particles.add(topBlock);
        particles.add(topBlock.sum(0, -1, 0));
        plugin.getParticler().spawnParticles(Particle.ENCHANTMENT_TABLE, particles, world, root, (int) Math.ceil(root * 4), 5);
    }
}
