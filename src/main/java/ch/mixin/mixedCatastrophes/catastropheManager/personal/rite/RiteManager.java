package ch.mixin.mixedCatastrophes.catastropheManager.personal.rite;

import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RiteManager {
    private final MixedCatastrophesData mixedCatastrophesData;

    public RiteManager(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
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
            case HAY_BLOCK:
                ultimatum(player, blockN1, blockN2);
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

        mixedCatastrophesData.getEventChangeManager()
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
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
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

        mixedCatastrophesData.getEventChangeManager()
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
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Nature_Conspiracy) <= 0) {
            return;
        }

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        blockN1.setType(Material.COBBLESTONE);
        blockN2.setType(Material.COBBLESTONE);

        int curse = playerData.getAspect(AspectType.Nature_Conspiracy);
        int curseMod = (int) -Math.min(curse, 3 + Math.round(curse * 0.35));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);
        changeMap.put(AspectType.Nature_Conspiracy, curseMod);

        mixedCatastrophesData.getEventChangeManager()
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
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Misfortune) <= 0) {
            return;
        }

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        blockN1.setType(Material.COBBLESTONE);
        blockN2.setType(Material.COBBLESTONE);

        int curse = playerData.getAspect(AspectType.Misfortune);
        int curseMod = (int) -Math.min(curse, 3 + Math.round(curse * 0.35));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);
        changeMap.put(AspectType.Misfortune, curseMod);

        mixedCatastrophesData.getEventChangeManager()
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
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Terror) <= 0) {
            return;
        }

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        blockN1.setType(Material.COBBLESTONE);
        blockN2.setType(Material.COBBLESTONE);

        int curse = playerData.getAspect(AspectType.Terror);
        int curseMod = (int) -Math.min(curse, 3 + Math.round(curse * 0.35));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);
        changeMap.put(AspectType.Terror, curseMod);

        mixedCatastrophesData.getEventChangeManager()
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
            case COAL_BLOCK:
                gainResolve(player, blockN1, blockN2);
                break;
        }
    }

    private void gainCelestialFavor(Player player, Block blockN1, Block blockN2) {
        int cost = 750;
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
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

        mixedCatastrophesData.getEventChangeManager()
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
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int limit = 150;
        int cost = 50 + playerData.getAspect(AspectType.Terror);

        if (playerData.getAspect(AspectType.Terror) > limit) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You can have at most " + limit + " Terror to do this.")
                    .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                    .finish()
                    .execute();
            return;
        }

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
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

        mixedCatastrophesData.getEventChangeManager()
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

    private void gainResolve(Player player, Block blockN1, Block blockN2) {
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int terror = playerData.getAspects().get(AspectType.Terror);
        int nobility = playerData.getAspects().get(AspectType.Nobility);
        int cost = (int) Math.ceil(5 * (10 + Math.pow(terror + nobility, 0.5)));

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
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

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventMessage("The Fire lights your Determination.")
                .withColor(Constants.AspectThemes.get(AspectType.Resolve).getColor())
                .finish()
                .execute();

        ritesParticles(player.getWorld(), Coordinate3D.toCoordinate(blockN1.getLocation()), cost * 0.03);
        mixedCatastrophesData.getRootCatastropheManager().getPersonalCatastropheManager().getResolveCatastropheManager().showVirtue(player);
    }

    private void ultimatum(Player player, Block blockN1, Block blockN2) {
        Material materialN1 = blockN1.getType();
        Material materialN2 = blockN2.getType();

        switch (materialN1) {
            case SOUL_SAND:
                initiateVoid(player, blockN1, blockN2);
                break;
        }
    }

    private void initiateVoid(Player player, Block blockN1, Block blockN2) {
        int cost = 1000;
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        cost += 100 * playerData.getAspect(AspectType.Death_Seeker);

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            mixedCatastrophesData.getEventChangeManager()
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

        for (AspectType aspectType : Constants.AspectOrder) {
            changeMap.put(aspectType, -playerData.getAspect(aspectType));
        }

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("Nothing remains.")
                .withColor(Constants.AspectThemes.get(AspectType.Death_Seeker).getColor())
                .withTitle(true)
                .finish()
                .execute();

        Coordinate3D startPoint = Coordinate3D.toCoordinate(player.getLocation());
        Location destination = null;
        int displaceMin = 250;
        int displaceMax = 500;
        Random random = new Random();
        int tries = 0;

        while (destination == null && tries < 100) {
            tries++;
            Coordinate3D c3d = startPoint.clone();
            c3d.setX(c3d.getX() + (random.nextBoolean() ? 1 : -1) * (random.nextInt(displaceMax - displaceMin + 1) + displaceMin));
            c3d.setZ(c3d.getZ() + (random.nextBoolean() ? 1 : -1) * (random.nextInt(displaceMax - displaceMin + 1) + displaceMin));

            destination = Functions.relativeGround(player.getWorld(), c3d);
        }

        if (destination == null){
            destination = player.getLocation();
        }else{
            destination.add(0, 1, 0);
        }

        player.setBedSpawnLocation(null);
        player.teleport(destination);
        player.getInventory().clear();
        player.getInventory().addItem(HelpInventoryManager.HelpBookItem);
        player.getActivePotionEffects().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);

        mixedCatastrophesData.getMixedAchievementsManager().resetAchievements(player);
        ritesParticles(player.getWorld(), Coordinate3D.toCoordinate(blockN1.getLocation()), cost * 0.03 * 10);
    }

    private void ritesParticles(World world, Coordinate3D topBlock, double multiplier) {
        double root = Math.pow(multiplier, 0.5);
        List<Coordinate3D> particles = new ArrayList<>();
        particles.add(topBlock);
        particles.add(topBlock.sum(0, -1, 0));
        mixedCatastrophesData.getParticler().spawnParticles(Particle.ENCHANTMENT_TABLE, particles, world, root, (int) Math.ceil(root * 4), 5);
    }
}
