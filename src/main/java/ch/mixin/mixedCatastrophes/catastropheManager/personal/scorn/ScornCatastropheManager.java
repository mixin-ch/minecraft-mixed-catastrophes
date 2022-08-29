package ch.mixin.mixedCatastrophes.catastropheManager.personal.scorn;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Random;

public class ScornCatastropheManager extends CatastropheManager {
    public ScornCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
    }

    @Override
    @Deprecated
    public void initializeMetaData() {
    }

    public void initializePlayerData(PlayerData playerData) {
        int skyScornTimer = playerData.getSkyScornTimer();

        if (skyScornTimer <= 0) {
            skyScornTimer = skyScornTimer();
        }
    }

    @Override
    public void updateMetaData() {
    }

    @Override
    public void initializeCauser() {
    }

    @Override
    public void tick() {
        for (Player player : mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers()) {
            if (!mixedCatastrophesData.getAffectedWorlds().contains(player.getWorld()))
                continue;

            PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

            processSkyScorn(player, playerData);
            processSeaScorn(player, playerData);
        }
    }

    private void processSkyScorn(Player player, PlayerData playerData) {
        int skyScorn = playerData.getAspect(AspectType.SkyScorn);
        int loss = (int) Math.ceil(0.1 * skyScorn);

        if (loss <= 0)
            return;

        int timer = playerData.getSkyScornTimer();
        timer--;

        if (timer <= 0) {
            timer = skyScornTimer();

            HashMap<AspectType, Integer> changeMap = new HashMap<>();
            changeMap.put(AspectType.SkyScorn, -loss);

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withAspectChange(changeMap)
                    .execute();
        }

        playerData.setSkyScornTimer(timer);
    }

    private void processSeaScorn(Player player, PlayerData playerData) {
        int seaScorn = playerData.getAspect(AspectType.SeaScorn);
        int loss = (int) Math.ceil(0.1 * seaScorn);

        if (loss >= 1) {
            int timer = playerData.getSeaScornTimer();
            timer--;

            if (timer <= 0) {
                timer = seaScornTimer();

                HashMap<AspectType, Integer> changeMap = new HashMap<>();
                changeMap.put(AspectType.SeaScorn, -loss);

                mixedCatastrophesData.getEventChangeManager()
                        .eventChange(player)
                        .withAspectChange(changeMap)
                        .execute();
            }
        }

        Random random = new Random();

        Entity vehicle = player.getVehicle();
        boolean inBoat = vehicle != null && vehicle.getType() == EntityType.BOAT;
        boolean inWater = player.getLocation().getBlock().getType() == Material.WATER;

        if (!inBoat && !inWater)
            return;

        boolean isCollectable = mixedCatastrophesData.getCatastropheSettings().getAspect().getSeaScorn().isCollectable();
        boolean isDrowning = mixedCatastrophesData.getCatastropheSettings().getAspect().getSeaScorn().isDrowning();

        double drowningProbability = seaScorn / (seaScorn + 400.0);

        if (isDrowning && random.nextDouble() < drowningProbability) {
            if (inBoat)
                vehicle.remove();

            int time = 4 * (int) Math.round(seaScorn * 0.2);

            player.setRemainingAir(0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, time * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time * 20, 10));
            player.removePotionEffect(PotionEffectType.JUMP);
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time * 20, -5));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time * 20, 10));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time * 20, 10));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time * 20, 0));

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventSound(Sound.ENTITY_BLAZE_AMBIENT)
                    .withEventMessage("The Sea pulls you into a watery grave.")
                    .withColor(Constants.AspectThemes.get(AspectType.SeaScorn).getColor())
                    .withTitle(true)
                    .finish()
                    .execute();
        } else if (isCollectable && random.nextDouble() < 0.02) {
            HashMap<AspectType, Integer> changeMap = new HashMap<>();
            changeMap.put(AspectType.SeaScorn, 1);

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withAspectChange(changeMap)
                    .withEventSound(Sound.ENTITY_BLAZE_AMBIENT)
                    .withEventMessage("The Sea dislikes your Hubris of swimming.")
                    .withColor(Constants.AspectThemes.get(AspectType.SeaScorn).getColor())
                    .withTitle(true)
                    .finish()
                    .execute();
        }
    }

    private int skyScornTimer() {
        return 5 * 60;
    }

    private int seaScornTimer() {
        return 10 * 60;
    }
}
