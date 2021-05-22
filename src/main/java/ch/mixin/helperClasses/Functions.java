package ch.mixin.helperClasses;

import ch.mixin.eventChange.aspect.AspectType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Functions {
    public static int random(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static boolean hasShelter(Player player) {
        Location location = player.getLocation();
        Coordinate2D center = new Coordinate2D(location.getBlockX(), location.getBlockZ());
        Location roof = absoluteRoof(player.getWorld(), center);

        if (roof == null) {
            return false;
        }

        return roof.getY() >= location.getY() + 1;
    }

    public static boolean isNight(World world) {
        return world.getTime() > 13 * 1000 && world.getTime() < 23 * 1000;
    }

    public static <T> T getRandomWithWeights(HashMap<T, Double> weights) {
        double sum = 0;

        for (double weight : weights.values()) {
            sum += weight;
        }

        double pointer = new Random().nextDouble() * sum;

        for (T t : weights.keySet()) {
            pointer -= weights.get(t);

            if (pointer <= 0)
                return t;
        }

        return null;
    }

    public static ArrayList<Coordinate2D> getSphere2D(Coordinate2D center, double radius) {
        ArrayList<Coordinate2D> sphere = new ArrayList<>();
        int range = (int) Math.round(Math.ceil(radius));

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (radius >= Math.pow(Math.pow(x, 2) + Math.pow(z, 2), 0.5)) {
                    sphere.add(center.sum(x, z));
                }
            }
        }

        return sphere;
    }

    public static ArrayList<Coordinate2D> getCircle2D(Coordinate2D center, double radius) {
        ArrayList<Coordinate2D> circle = new ArrayList<>();
        int range = (int) Math.round(Math.ceil(radius));

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                double distance = Math.pow(Math.pow(x, 2) + Math.pow(z, 2), 0.5);
                double difference = Math.abs(radius - distance);

                if (difference <= 0.5) {
                    circle.add(center.sum(x, z));
                }
            }
        }

        return circle;
    }

    public static ArrayList<Coordinate2D> getSquareEdge(Coordinate2D center, int radius) {
        ArrayList<Coordinate2D> square = new ArrayList<>();

        if (radius <= 0){
            square.add(center.sum(0,0));
            return square;
        }

        Coordinate2D pos = center.sum(-radius, -radius);

        for (int i = 0; i < 2 * radius; i++) {
            square.add(pos);
            pos = pos.sum(1, 0);
        }

        for (int i = 0; i < 2 * radius; i++) {
            square.add(pos);
            pos = pos.sum(0, 1);
        }

        for (int i = 0; i < 2 * radius; i++) {
            square.add(pos);
            pos = pos.sum(-1, 0);
        }

        for (int i = 0; i < 2 * radius; i++) {
            square.add(pos);
            pos = pos.sum(0, -1);
        }

        return square;
    }

    public static ArrayList<Location> getSphereLocation(Location center, double radius) {
        ArrayList<Location> sphere = new ArrayList<>();
        int range = (int) Math.round(Math.ceil(radius));

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    if (radius >= Math.pow(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2), 0.5)) {
                        int locX = center.getBlockX() + x;
                        int locY = center.getBlockY() + y;
                        int locZ = center.getBlockZ() + z;
                        sphere.add(new Location(center.getWorld(), locX, locY, locZ));
                    }
                }
            }
        }

        return sphere;
    }

    public static <T> ArrayList<T> merge(ArrayList<T> list1, ArrayList<T> list2) {
        ArrayList<T> combined = new ArrayList<>(list1);
        combined.removeAll(list2);
        combined.addAll(list2);
        return combined;
    }

    public static Location absoluteRoof(World world, Coordinate2D space) {
        Block block = world.getHighestBlockAt(space.getXRound(), space.getZRound());

        if (block.isPassable()) {
            return null;
        } else {
            return block.getLocation();
        }

//        for (int y = world.getMaxHeight(); y >= 0; y--) {
//            Location location = new Location(world, space.getX(), y, space.getZ());
//
//            if (!Constants.Airs.contains(location.getBlock().getType())) {
//                return location;
//            }
//        }
//
//        return null;
    }

    public static Location relativeGround(World world, Coordinate3D place) {
        boolean passable = place.toLocation(world).getBlock().isPassable();

        if (passable) {
            while (place.getY() > 0) {
                place.setY(place.getY() - 1);
                Location location = place.toLocation(world);

                if (!location.getBlock().isPassable()) {
                    return location;
                }
            }
        } else {
            while (place.getY() <= world.getMaxHeight()) {
                place.setY(place.getY() + 1);
                Location location = place.toLocation(world);

                if (location.getBlock().isPassable()) {
                    return place.sum(0, -1, 0).toLocation(world);
                }
            }
        }

        return null;
    }

    public static Location offset(Location location, int offsetY) {
        return offset(location, 0, offsetY, 0);
    }

    public static Location offset(Location location, int offsetX, int offsetY, int offsetZ) {
        if (location == null)
            return null;

        int x = location.getBlockX() + offsetX;
        int y = location.getBlockY() + offsetY;
        int z = location.getBlockZ() + offsetZ;

        World world = location.getWorld();

        if (y < 0 || y > world.getMaxHeight())
            return null;

        return new Location(world, x, y, z);
    }
}
