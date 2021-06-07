package ch.mixin.mixedCatastrophes.catastropheManager.global.timeDistortion;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class TimeDistortionManager extends CatastropheManager {
    private int timeDistortionTimer;

    public TimeDistortionManager(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor);
    }

    @Override
    public void initializeMetaData() {
        if (metaData.getTimeDistortionTimer() <= 0) {
            metaData.setTimeDistortionTimer(timeDistortionTimer());
        }
    }

    @Override
    public void updateMetaData() {
        metaData.setTimeDistortionTimer(timeDistortionTimer);
    }

    @Override
    public void initializeCauser() {
        timeDistortionTimer = metaData.getTimeDistortionTimer();
    }

    private int timeDistortionTimer() {
        return Functions.random(60, 300);
    }

    @Override
    public void tick() {
        timeDistortionTimer--;

        if (timeDistortionTimer <= 0) {
            causeTimeDistortion();
        }
    }

    public void causeTimeDistortion() {
        timeDistortionTimer = timeDistortionTimer();
        int momentum = (int) Math.round((new Random().nextDouble() * 0.5 + 0.25) * 24000);
        boolean forward = new Random().nextBoolean();
        List<World> worlds = mixedCatastrophesManagerAccessor.getAffectedWorlds();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.AMBIENT_BASALT_DELTAS_ADDITIONS, 10.0f, 1.0f);
            mixedCatastrophesManagerAccessor.getEventChangeManager()
                    .eventChange(player)
                    .withEventMessage("Time is shifting.")
                    .withColor(ChatColor.BLUE)
                    .withTitle(true)
                    .finish()
                    .execute();
        }

        partialTimeDistortion(worlds, momentum, forward);
    }

    private void partialTimeDistortion(List<World> worlds, int momentum, boolean forward) {
        int jumpAmount = Math.min(50, momentum);

        for (World world : worlds) {
            world.setFullTime(world.getFullTime() + jumpAmount * (forward ? 1 : -1));
        }

        int momentumRemaining = momentum - jumpAmount;

        if (momentumRemaining > 0) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                            partialTimeDistortion(worlds, momentumRemaining, forward)
                    , 1);
        }
    }
}
