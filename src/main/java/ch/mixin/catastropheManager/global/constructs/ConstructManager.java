package ch.mixin.catastropheManager.global.constructs;

import ch.mixin.metaData.constructs.*;
import ch.mixin.catastropheManager.CatastropheManager;
import ch.mixin.catastropheManager.RootCatastropheManager;
import ch.mixin.helperClasses.*;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConstructManager extends CatastropheManager {
    public ConstructManager(MixedCatastrophesPlugin plugin, RootCatastropheManager rootCatastropheManager) {
        super(plugin, rootCatastropheManager);
    }

    @Override
    public void initializeMetaData() {
        if (metaData.getGreenWellDataList() == null)
            metaData.setGreenWellDataList(new ArrayList<>());

        if (metaData.getBlitzardDataList() == null)
            metaData.setBlitzardDataList(new ArrayList<>());

        if (metaData.getLighthouseDataList() == null)
            metaData.setLighthouseDataList(new ArrayList<>());

        if (metaData.getBlazeReactorList() == null)
            metaData.setBlazeReactorList(new ArrayList<>());

        if (metaData.getScarecrowDataList() == null)
            metaData.setScarecrowDataList(new ArrayList<>());
    }

    @Override
    public void updateMetaData() {
    }

    @Override
    public void initializeCauser() {
    }

    @Override
    public void tick() {
        if (plugin.getServer().getOnlinePlayers().size() == 0)
            return;

        tickGreenWell();
        tickBlitzard();
        tickLighthouse();
        tickBlazeReactor();
        tickScarecrow();
    }


    private void tickGreenWell() {
        List<GreenWellData> greenWellDataList = metaData.getGreenWellDataList();

        for (GreenWellData greenWellData : greenWellDataList) {
            World world = plugin.getServer().getWorld(greenWellData.getWorldName());

            if (world == null)
                return;

            Coordinate3D center = greenWellData.getPosition();
            ShapeCompareResult shapeCompareResult = Constants.GreenWell.checkConstructed(center.toLocation(world));

            if (!shapeCompareResult.isConstructed())
                return;

            int level = greenWellData.getLevel();
            Location locationCenter = center.toLocation(world);
            Location locationCenterMiddle = center.sum(0.5, 0.5, 0.5).toLocation(world);

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(center);
            particles.add(center.sum(0, 1, 0));

            plugin.getParticler().spawnParticles(Particle.VILLAGER_HAPPY, particles, world, level * 0.25, 4, 5);

            if (!Constants.GreenWell.checkConstructed(locationCenter).isConstructed())
                return;

            List<Coordinate2D> square = Functions.getSquareEdge(center.to2D(), 1);
            List<Material> logs = new ArrayList<>();

            for (Coordinate2D field : square) {
                logs.add(field.to3D(center.getY()).toLocation(world).getBlock().getType());
            }

            double amount = Math.pow(3 + 2 * level, 2) * 0.002;
            int successfulAmount = 0;

            while (amount > 0) {
                double probability = Math.min(1, amount);
                amount--;

                if (new Random().nextDouble() < probability) {
                    int range = new Random().nextInt(level + 2);
                    int x = new Random().nextInt(2 * range + 1) - range;
                    int z = new Random().nextInt(2 * range + 1) - range;

                    Coordinate3D spot = center.sum(x, 0, z);
                    Location location = spot.toLocation(world);

                    boolean success = false;

                    if (location.getBlock().getType() == Material.FLOWER_POT) {
                        success = true;
                        location.getBlock().setType(Constants.PottedFlowers.get(new Random().nextInt(Constants.PottedFlowers.size())));
                    } else if (Constants.Airs.contains(location.getBlock().getType())) {
                        Location below = spot.sum(0, -1, 0).toLocation(world);

                        if (below.getBlock().getType() == Material.GRASS_BLOCK) {
                            success = true;
                            location.getBlock().setType(Constants.Flowers.get(new Random().nextInt(Constants.Flowers.size())));
                        } else if (below.getBlock().getType() == Material.DIRT) {
                            below.getBlock().setType(Material.GRASS_BLOCK);
                        }
                    }

                    if (success) {
                        successfulAmount++;
                    }
                }
            }

            if (successfulAmount > 0) {
                world.dropItem(locationCenterMiddle, new ItemStack(logs.get(new Random().nextInt(logs.size())), successfulAmount));
            }
        }
    }

    private void tickBlitzard() {
        List<BlitzardData> blitzardDataList = metaData.getBlitzardDataList();

        for (BlitzardData blitzardData : blitzardDataList) {
            World world = plugin.getServer().getWorld(blitzardData.getWorldName());

            if (world == null)
                return;

            Coordinate3D center = blitzardData.getPosition();
            ShapeCompareResult shapeCompareResult = Constants.Blitzard.checkConstructed(center.toLocation(world));

            if (!shapeCompareResult.isConstructed())
                return;

            int level = blitzardData.getLevel();

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(center);
            particles.add(center.sum(0, 1, 0));

            plugin.getParticler().spawnParticles(Particle.SPELL_MOB, particles, world, Math.pow(level, 1.25) * 0.2, 4, 5);
        }
    }

    private void tickLighthouse() {
        List<LighthouseData> lighthouseDataList = metaData.getLighthouseDataList();

        for (LighthouseData lighthouseData : lighthouseDataList) {
            World world = plugin.getServer().getWorld(lighthouseData.getWorldName());

            if (world == null)
                return;

            Coordinate3D center = lighthouseData.getPosition();
            ShapeCompareResult shapeCompareResult = Constants.Lighthouse.checkConstructed(center.toLocation(world));

            if (!shapeCompareResult.isConstructed())
                return;

            int level = lighthouseData.getLevel();

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(center);
            particles.add(center.sum(0, 1, 0));

            plugin.getParticler().spawnParticles(Particle.LAVA, particles, world, Math.pow(level, 1.1) * 0.25, 4, 5);
        }
    }

    private void tickBlazeReactor() {
        List<BlazeReactorData> blazeReactorDataList = metaData.getBlazeReactorList();

        for (BlazeReactorData blazeReactorData : blazeReactorDataList) {
            World world = plugin.getServer().getWorld(blazeReactorData.getWorldName());

            if (world == null)
                return;

            Coordinate3D center = blazeReactorData.getPosition();
            Location locationCenter = center.toLocation(world);
            ShapeCompareResult shapeCompareResult = Constants.BlazeReactor.checkConstructed(locationCenter);

            if (!shapeCompareResult.isConstructed())
                return;

            int level = blazeReactorData.getLevel();
            int fuel = blazeReactorData.getFuel();

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(new Coordinate3D(0, 0, 0));
            particles.add(new Coordinate3D(2, -1, 0));

            for (int i = 0; i < particles.size(); i++) {
                Coordinate3D particle = particles.get(i).rotateY90Clockwise(shapeCompareResult.getRotations());
                particles.remove(i);
                particles.add(i, particle.sum(center));
            }

            plugin.getParticler().spawnParticles(Particle.SMOKE_LARGE, particles, world, new Coordinate3D(0, 0.2 + level * 0.02, 0), Math.pow(level, 1.5) * 0.15, 4, 5);

            if (fuel == 0) {
                List<Coordinate3D> relativeCauldronList = new ArrayList<>();
                relativeCauldronList.add(new Coordinate3D(3, -1, -1));
                relativeCauldronList.add(new Coordinate3D(3, -1, 0));
                relativeCauldronList.add(new Coordinate3D(3, -1, 1));
                relativeCauldronList.add(new Coordinate3D(2, -1, -1));
                relativeCauldronList.add(new Coordinate3D(2, -1, 1));

                List<Location> cauldronList = new ArrayList<>();

                for (Coordinate3D relativeCauldron : relativeCauldronList) {
                    cauldronList.add(relativeCauldron.rotateY90Clockwise(shapeCompareResult.getRotations()).sum(center).toLocation(world));
                }

                while (cauldronList.size() > 0) {
                    int index = new Random().nextInt(cauldronList.size());
                    Block cauldron = cauldronList.get(index).getBlock();
                    Levelled cauldronData = (Levelled) cauldron.getBlockData();
                    cauldronList.remove(index);

                    if (cauldronData.getLevel() == 0)
                        continue;

                    cauldronData.setLevel(cauldronData.getLevel() - 1);
                    cauldron.setBlockData(cauldronData);
                    fuel += 20 * blazeReactorData.getLevel();
                    break;
                }
            }

            if (fuel == 0)
                return;

            fuel--;
            blazeReactorData.setFuel(fuel);

            double chances = 0.1 * blazeReactorData.getLevel();
            int amount = (int) Math.floor(chances);
            chances = chances % 1;

            if (new Random().nextDouble() < chances)
                amount++;

            if (amount > 0) {
                Coordinate3D relativeDrop = new Coordinate3D(-1, -1, 0).rotateY90Clockwise(shapeCompareResult.getRotations());
                Location dropLocation = relativeDrop.sum(center).sum(0.5, 0.5, 0.5).toLocation(world);
                world.dropItem(dropLocation, new ItemStack(Material.COBBLESTONE, amount));
            }
        }
    }

    private void tickScarecrow() {
        List<ScarecrowData> scarecrowDataList = metaData.getScarecrowDataList();

        for (ScarecrowData scarecrowData : scarecrowDataList) {
            World world = plugin.getServer().getWorld(scarecrowData.getWorldName());

            if (world == null)
                return;

            Coordinate3D center = scarecrowData.getPosition();
            ShapeCompareResult shapeCompareResult = Constants.Scarecrow.checkConstructed(center.toLocation(world));

            if (!shapeCompareResult.isConstructed())
                return;

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(new Coordinate3D(0, 0, -2));
            particles.add(new Coordinate3D(0, 0, 2));

            for (int i = 0; i < particles.size(); i++) {
                Coordinate3D particle = particles.get(i).rotateY90Clockwise(shapeCompareResult.getRotations());
                particles.remove(i);
                particles.add(i, particle.sum(center));
            }

            int terror = scarecrowData.getCollectedTerror();
            plugin.getParticler().spawnParticles(Particle.SOUL, particles, world, new Coordinate3D(0, 0.1 + Math.pow(terror, 0.5) * 0.01, 0), 0.1 + terror * 0.01, 4, 5);
        }
    }
}
