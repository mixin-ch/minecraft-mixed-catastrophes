package ch.mixin.catastropheManager.personal.dream;

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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class DreamManager {
    private final static ArrayList<DreamType> dreamOrder;

    static {
        dreamOrder = new ArrayList<>();
        dreamOrder.add(DreamType.ClockworkDreams);
        dreamOrder.add(DreamType.GloryDreams);
        dreamOrder.add(DreamType.PerfectDreams);
        dreamOrder.add(DreamType.BloodstainedDreams);
        dreamOrder.add(DreamType.SkyDreams);
        dreamOrder.add(DreamType.SereneDreams);
    }

    private final MixedCatastrophesPlugin plugin;

    public DreamManager(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    public void performDream(Player player, Block startingBed) {
        DreamType dreamType = getDreamType(startingBed);

        if (dreamType == null)
            return;

        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getDreamCooldown() > 0) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("It is too early to enter Dreams. Wait " + playerData.getDreamCooldown() + " seconds.")
                    .withColor(ChatColor.WHITE)
                    .finish()
                    .execute();
            return;
        }

        switch (dreamType) {
            case SereneDreams:
                sereneDreams(player);
                break;
            case SkyDreams:
                skyDreams(player);
                break;
            case BloodstainedDreams:
                bloodstainedDreams(player);
                break;
            case ClockworkDreams:
                clockworkDreams(player);
                break;
            case GloryDreams:
                gloryDreams(player);
                break;
            case PerfectDreams:
                perfectDreams(player);
                break;
        }
    }

    private DreamType getDreamType(Block startingBed) {
        HashSet<DreamType> dreamTypes = new HashSet<>();

        World world = startingBed.getWorld();
        ArrayList<Coordinate3D> bedList = new ArrayList<>();
        ArrayList<Coordinate3D> searchList = new ArrayList<>();
        searchList.add(Coordinate3D.toCoordinate(startingBed.getLocation()));

        while (searchList.size() > 0) {
            Coordinate3D c3d = searchList.get(0);
            searchList.remove(c3d);

            Material material = c3d.toLocation(world).getBlock().getType();

            switch (material) {
                case IRON_BLOCK:
                    dreamTypes.add(DreamType.ClockworkDreams);
                    break;
                case GOLD_BLOCK:
                    dreamTypes.add(DreamType.GloryDreams);
                    break;
                case DIAMOND_BLOCK:
                    dreamTypes.add(DreamType.PerfectDreams);
                    break;
                case LAPIS_BLOCK:
                    dreamTypes.add(DreamType.SkyDreams);
                    break;
                case REDSTONE_BLOCK:
                    dreamTypes.add(DreamType.BloodstainedDreams);
                    break;
            }

            if (!Constants.Beds.contains(material))
                continue;

            bedList.add(c3d);

            searchList.add(c3d.sum(1, 0, 0));
            searchList.add(c3d.sum(-1, 0, 0));
            searchList.add(c3d.sum(0, 1, 0));
            searchList.add(c3d.sum(0, -1, 0));
            searchList.add(c3d.sum(0, 0, 1));
            searchList.add(c3d.sum(0, 0, -1));

            searchList.removeAll(bedList);
        }

        for (DreamType dreamType : dreamOrder) {
            if (dreamTypes.contains(dreamType)) {
                return dreamType;
            }
        }

        return DreamType.SereneDreams;
    }

    private boolean greyhat(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int greyhatDebt = playerData.getAspect(AspectType.Greyhat_Debt);
        double probability = greyhatDebt / (100.0 + greyhatDebt);

        if (new Random().nextDouble() < probability) {
            int time = 10 * 60;
            playerData.setDreamCooldown(time);

            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("Your Dream has bee seized by Greyhat Inc.")
                    .withCause(AspectType.Greyhat_Debt)
                    .withTitle(true)
                    .finish()
                    .execute();
            return true;
        }

        return false;
    }

    private void sereneDreams(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (greyhat(player))
            return;

        int terrorMinus = (int) Math.min(Math.round(1 + 0.1 * playerData.getAspect(AspectType.Terror)), playerData.getAspect(AspectType.Terror));

        int time = 10 * 60;
        playerData.setDreamCooldown(time);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Terror, -terrorMinus);
        changeMap.put(AspectType.Secrets, 10);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The World is quiet for a short Moment.")
                .withColor(ChatColor.AQUA)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void skyDreams(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int terrorMinus = (int) Math.min(Math.round(10 + 0.2 * playerData.getAspect(AspectType.Terror)), playerData.getAspect(AspectType.Terror));
        int cost = 2 * terrorMinus;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to enter Sky Dreams.")
                    .withColor(ChatColor.WHITE)
                    .finish()
                    .execute();
            return;
        }

        if (greyhat(player))
            return;

        int time = 10 * 60;
        playerData.setDreamCooldown(time);

        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, time * 20, 0));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Terror, -terrorMinus);
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You fly high above the World's Laws.")
                .withColor(ChatColor.BLUE)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void bloodstainedDreams(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int limit = 300;

        if (playerData.getAspect(AspectType.Terror) > limit) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You can have at most " + limit + " Terror to enter Bloodstained Dreams.")
                    .withColor(ChatColor.WHITE)
                    .finish()
                    .execute();
            return;
        }

        if (greyhat(player))
            return;

        int time = 10 * 60;
        playerData.setDreamCooldown(time);

        int terrorPlus = (int) Math.round(10 + 0.1 * playerData.getAspect(AspectType.Terror));
        int secretsPlus = 30 + playerData.getAspect(AspectType.Terror);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Terror, terrorPlus);
        changeMap.put(AspectType.Secrets, secretsPlus);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("There is a Curtain of Blood, and an Eye behind it.")
                .withColor(ChatColor.DARK_RED)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void clockworkDreams(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int cost = 30;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to enter Clockwork Dreams.")
                    .withColor(ChatColor.WHITE)
                    .finish()
                    .execute();
            return;
        }

        if (greyhat(player))
            return;

        int time = 10 * 60;
        playerData.setDreamCooldown(time);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, time * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, time * 20, 0));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You hear the rhythmic Ticking, you sense the Iron Efficiency.")
                .withColor(ChatColor.GRAY)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void gloryDreams(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int cost = 120;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to enter Glory Dreams.")
                    .withColor(ChatColor.WHITE)
                    .finish()
                    .execute();
            return;
        }

        if (greyhat(player))
            return;

        int time = 10 * 60;
        playerData.setDreamCooldown(time);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, time * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, time * 20, 1));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("Your Thoughts are full of Gold and Glory.")
                .withColor(ChatColor.GOLD)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void perfectDreams(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int cost = 480;

        if (playerData.getAspect(AspectType.Secrets) < cost) {
            plugin.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("You need at least " + cost + " Secrets to enter Perfect Dreams.")
                    .withColor(ChatColor.WHITE)
                    .finish()
                    .execute();
            return;
        }

        if (greyhat(player))
            return;

        int time = 10 * 60;
        playerData.setDreamCooldown(time);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time * 20, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, time * 20, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, time * 20, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, time * 20, 2));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, -cost);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You have witnessed Perfection, and know how to achieve it.")
                .withColor(ChatColor.WHITE)
                .withTitle(true)
                .finish()
                .execute();
    }
}
