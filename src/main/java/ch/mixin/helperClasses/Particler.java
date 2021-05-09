package ch.mixin.helperClasses;

import ch.mixin.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particler {
    private final MixedCatastrophesPlugin plugin;

    public Particler(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    public void spawnParticles(Particle particle, List<Coordinate3D> positionList, World world, double amount, int steps, int stepSize) {
        int size = positionList.size();
        double sum = size * amount;

        Random random = new Random();

        while (sum > 0) {
            if (random.nextDouble() < sum) {
                Location location = positionList.get(random.nextInt(size)).sum(random.nextDouble(), random.nextDouble(), random.nextDouble()).toLocation(world);
                location.getWorld().spawnParticle(particle, location, 1);
            }

            sum--;
        }

        int remainingSteps = steps - 1;

        if (remainingSteps > 0) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                    () -> spawnParticles(particle, positionList, world, amount, remainingSteps, stepSize)
                    , stepSize);
        }
    }
}
