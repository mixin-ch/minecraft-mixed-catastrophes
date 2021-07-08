package ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.stalker;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.StalkerData;
import ch.mixin.mixedCatastrophes.metaData.TerrorData;
import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class StalkerCatastropheManager extends CatastropheManager {
    public StalkerCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
    }

    @Override
    public void initializeMetaData() {
    }

    public void initializePlayerData(PlayerData playerData) {
        TerrorData terrorData = playerData.getTerrorData();
        if (terrorData.getStalkerTimer() <= 0) {
            terrorData.setStalkerTimer(stalkerTimer());
        }
    }

    @Override
    public void updateMetaData() {

    }

    @Override
    public void initializeCauser() {
    }

    @Override
    @Deprecated
    public void tick() {
    }

    public void tick(Player player, boolean hasScareCrow) {
        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getTerror().isStalker())
            return;

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        TerrorData terrorData = playerData.getTerrorData();

        int timer = terrorData.getStalkerTimer();
        timer--;

        if (hasScareCrow) {
            timer -= 2;
        }

        if (timer <= 0) {
            timer = stalkerTimer();

            int hauntingDemise = playerData.getAspect(AspectType.Death_Seeker);
            int terror = playerData.getAspect(AspectType.Terror) + (hasScareCrow ? 100 : 0);
            double modifier = Math.pow(Math.max(0, terror + hauntingDemise - 50), 0.5);

            double probability = (modifier) / (modifier + 600.0);

            if (new Random().nextDouble() < probability) {
                causeStalker(player);
            }
        }

        terrorData.setStalkerTimer(timer);
        stalkerActions(player);
    }

    private int stalkerTimer() {
        return Functions.random(30, 90);
    }


    private void stalkerActions(Player player) {
        ArrayList<StalkerData> stalkerDatas = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId()).getTerrorData().getStalkerDatas();
        Coordinate3D playerPosition = Coordinate3D.toCoordinate(player.getLocation()).sum(0, 2, 0);
        World world = player.getWorld();

        ArrayList<Coordinate3D> fromPositions = new ArrayList<>();
        ArrayList<Coordinate3D> toPositions = new ArrayList<>();

        for (int i = 0; i < stalkerDatas.size(); i++) {
            StalkerData stalkerData = stalkerDatas.get(i);
            World stalkerWorld = MixedCatastrophesPlugin.PLUGIN.getServer().getWorld(stalkerData.getWorldName());

            if (stalkerWorld != world)
                continue;

            int remainingTime = stalkerData.getRemainingTime() - 1;

            if (remainingTime <= 0) {
                stalkerDatas.remove(stalkerData);
                i--;

                mixedCatastrophesData.getEventChangeManager()
                        .eventChange(player)
                        .withEventSound(Sound.AMBIENT_CAVE)
                        .withEventMessage("A Shadow fades away.")
                        .withCause(AspectType.Death_Seeker)
                        .withTitle(true)
                        .finish()
                        .execute();

                continue;
            }

            stalkerData.setRemainingTime(remainingTime);
            Coordinate3D stalkerPosition = stalkerData.getCoordinate3D();
            double speed = stalkerData.getSpeed();

            if (playerPosition.distance(stalkerPosition) <= 5) {
                player.damage(1);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2 * 20, 0));
            }

            Coordinate3D endPosition;

            if (playerPosition.distance(stalkerPosition) < speed) {
                endPosition = playerPosition.clone();
            } else {
                endPosition = stalkerPosition.sum(playerPosition.difference(stalkerPosition).normalize().multiply(speed));
            }

            stalkerData.setCoordinate3D(endPosition);

            fromPositions.add(stalkerPosition.clone());
            toPositions.add(endPosition.clone());
        }

        moveStalker(fromPositions, toPositions, world, 20);
    }

    private void moveStalker
            (ArrayList<Coordinate3D> fromPositions, ArrayList<Coordinate3D> toPositions, World world, int ticks) {
        for (int i = 0; i < fromPositions.size(); i++) {
            Coordinate3D fromPosition = fromPositions.get(i);
            Coordinate3D toPosition = toPositions.get(i);
            Location location = fromPosition.sum(toPosition.difference(fromPosition).multiply(1 / (double) ticks)).toLocation(world);

            world.spawnParticle(Particle.ASH, location, 3);
            world.playEffect(location, Effect.SMOKE, 0);
        }

        int remainingTicks = ticks - 1;

        if (remainingTicks > 0) {
            mixedCatastrophesData.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(mixedCatastrophesData.getPlugin(), () -> moveStalker(fromPositions, toPositions, world, remainingTicks)
                    , 1);
        }
    }

    public void causeStalker(Player player) {
        World world = player.getWorld();
        Coordinate3D coordinate3D = Coordinate3D.toCoordinate(player.getLocation()).sum(Coordinate3D.random().multiply(50));

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int hauntingDemise = playerData.getAspect(AspectType.Death_Seeker);
        int terror = playerData.getAspect(AspectType.Terror);
        int modifier = hauntingDemise + terror;

        double baseSpeed = new Random().nextDouble() + 0.5;
        double speed = baseSpeed + 4.0 * (modifier / (200.0 + modifier));
        int remainingTime = (int) Math.round((60.0 + 0.5 * modifier) / speed);

        StalkerData stalkerData = new StalkerData(world.getName(), coordinate3D, speed, remainingTime);
        mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId()).getTerrorData().getStalkerDatas().add(stalkerData);

        if (mixedCatastrophesData.getCatastropheSettings().getAspect().getResolve().isVirtue()) {
            mixedCatastrophesData.getRootCatastropheManager().getPersonalCatastropheManager().getResolveCatastropheManager().mightShowVirtue(player, 0.2);
        }

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.ENTITY_BLAZE_AMBIENT)
                .withEventMessage("Something is coming for you. RUN!")
                .withCause(AspectType.Death_Seeker)
                .withTitle(true)
                .finish()
                .execute();
    }
}
