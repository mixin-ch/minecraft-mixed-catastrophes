package ch.mixin.helperClasses;

import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.List;
import java.util.Random;

public class Particler {
    private final MixedCatastrophesPlugin plugin;

    public Particler(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }


    public void spawnParticles(Particle particle, List<Coordinate3D> positionList, World world, double amount, int steps, int stepSize) {
        spawnParticles(particle, positionList, world, new Coordinate3D(0, 0, 0), amount, steps, stepSize);
    }

    public void spawnParticles(Particle particle, List<Coordinate3D> positionList, World world, Coordinate3D velocity, double amount, int steps, int stepSize) {
        int size = positionList.size();
        double sum = size * amount;

        Random random = new Random();

        while (sum > 0) {
            if (random.nextDouble() < sum) {
                Location location = positionList.get(random.nextInt(size)).sum(random.nextDouble(), random.nextDouble(), random.nextDouble()).toLocation(world);
                //location.getWorld().spawnParticle(particle, location, 1);
                location.getWorld().spawnParticle(particle, location.getX(), location.getY(), location.getZ(), 0, velocity.getX(), velocity.getY(), velocity.getZ(), velocity.length(), null);
            }

            sum--;
        }

        int remainingSteps = steps - 1;

        if (remainingSteps > 0) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
                    () -> spawnParticles(particle, positionList, world, velocity, amount, remainingSteps, stepSize)
                    , stepSize);
        }
    }
}
