package ch.mixin.mixedCatastrophes.catastropheManager.global.constructs;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.helperClasses.*;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.metaData.constructs.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConstructManager extends CatastropheManager {
    public ConstructManager(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor);
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

            mixedCatastrophesManagerAccessor.getParticler().spawnParticles(Particle.VILLAGER_HAPPY, particles, world, level * 0.25, 4, 5);

            if (!Constants.GreenWell.checkConstructed(locationCenter).isConstructed())
                return;

            List<Coordinate2D> square = Functions.getSquareEdge(center.to2D(), 1);
            List<Material> logs = new ArrayList<>();

            for (Coordinate2D field : square) {
                logs.add(field.to3D(center.getY()).toLocation(world).getBlock().getType());
            }

            Random random = new Random();
            double amount = Math.pow(3 + 2 * level, 2) * 0.002;
            int successfulAmount = 0;

            while (amount > 0) {
                double probability = Math.min(1, amount);
                amount--;

                if (random.nextDouble() < probability) {
                    int range = random.nextInt(level + 2);
                    int x = random.nextInt(2 * range + 1) - range;
                    int z = random.nextInt(2 * range + 1) - range;

                    Coordinate3D spot = center.sum(x, 0, z);
                    Location location = spot.toLocation(world);
                    Location locationP1 = location.clone().add(0, 1, 0);

                    boolean success = false;

                    if (location.getBlock().getType() == Material.FLOWER_POT) {
                        success = true;
                        location.getBlock().setType(Constants.PottedFlowers.get(random.nextInt(Constants.PottedFlowers.size())));
                    } else if (Constants.Airs.contains(location.getBlock().getType())) {
                        Location below = spot.sum(0, -1, 0).toLocation(world);

                        if (below.getBlock().getType() == Material.GRASS_BLOCK) {
                            success = true;
                            boolean tall = random.nextDouble() < (Constants.Flowers_Tall.size() / (double) (Constants.Flowers_Small.size() + Constants.Flowers_Tall.size()));
                            tall = tall && Constants.Airs.contains(locationP1.getBlock().getType());

                            if (tall) {
                                Material material = Constants.Flowers_Tall.get(random.nextInt(Constants.Flowers_Tall.size()));
                                setFlower(location.getBlock(), material, Bisected.Half.BOTTOM);
                                setFlower(locationP1.getBlock(), material, Bisected.Half.TOP);
                            } else {
                                Material material = Constants.Flowers_Small.get(random.nextInt(Constants.Flowers_Small.size()));
                                location.getBlock().setType(material);
                            }
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

    private void setFlower(Block block, Material type, Bisected.Half half) {
        block.setType(type, false);
        Bisected data = (Bisected) block.getBlockData();
        data.setHalf(half);
        block.setBlockData(data);
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

            mixedCatastrophesManagerAccessor.getParticler().spawnParticles(Particle.SPELL_MOB, particles, world, Math.pow(level, 1.25) * 0.2, 4, 5);
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

            mixedCatastrophesManagerAccessor.getParticler().spawnParticles(Particle.LAVA, particles, world, Math.pow(level, 1.1) * 0.25, 4, 5);
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

            mixedCatastrophesManagerAccessor.getParticler().spawnParticles(Particle.SMOKE_LARGE, particles, world, new Coordinate3D(0, 0.2 + level * 0.02, 0), Math.pow(level, 1.5) * 0.15, 4, 5);

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
                    cauldronList.remove(index);

                    if (cauldron.getType() != Material.WATER_CAULDRON)
                        continue;

                    cauldron.setType(Material.CAULDRON);
                    fuel += 60 * blazeReactorData.getLevel();
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
            mixedCatastrophesManagerAccessor.getParticler().spawnParticles(Particle.SOUL, particles, world, new Coordinate3D(0, 0.1 + Math.pow(terror, 0.5) * 0.01, 0), 0.1 + terror * 0.01, 4, 5);
        }
    }

    public List<ScarecrowData> getScarecrowDataListInWorld(List<ScarecrowData> constructDataList, String worldName) {
        List<ScarecrowData> constructDataListInWorld = new ArrayList<>();

        for (ScarecrowData constructData : constructDataList) {
            if (constructData.getWorldName().equals(worldName))
                constructDataListInWorld.add(constructData);
        }

        return constructDataListInWorld;
    }

    public List<BlitzardData> getBlitzardListIsConstructed(List<BlitzardData> constructDataList) {
        List<BlitzardData> constructDataListIsConstructed = new ArrayList<>();

        for (BlitzardData constructData : constructDataList) {
            World world = plugin.getServer().getWorld(constructData.getWorldName());

            if (world == null)
                continue;

            Location location = constructData.getPosition().toLocation(world);

            if (Constants.Blitzard.checkConstructed(location).isConstructed())
                constructDataListIsConstructed.add(constructData);
        }

        return constructDataListIsConstructed;
    }

    public List<LighthouseData> getLighthouseListIsConstructed(List<LighthouseData> constructDataList) {
        List<LighthouseData> constructDataListIsConstructed = new ArrayList<>();

        for (LighthouseData constructData : constructDataList) {
            World world = plugin.getServer().getWorld(constructData.getWorldName());

            if (world == null)
                continue;

            Location location = constructData.getPosition().toLocation(world);

            if (Constants.Lighthouse.checkConstructed(location).isConstructed())
                constructDataListIsConstructed.add(constructData);
        }

        return constructDataListIsConstructed;
    }

    public List<ScarecrowData> getScarecrowListIsConstructed(List<ScarecrowData> constructDataList) {
        List<ScarecrowData> constructDataListIsConstructed = new ArrayList<>();

        for (ScarecrowData constructData : constructDataList) {
            World world = plugin.getServer().getWorld(constructData.getWorldName());

            if (world == null)
                continue;

            Location location = constructData.getPosition().toLocation(world);

            if (Constants.Scarecrow.checkConstructed(location).isConstructed())
                constructDataListIsConstructed.add(constructData);
        }

        return constructDataListIsConstructed;
    }

    public BlitzardData getStrongestBlitzard(List<BlitzardData> constructList, Location location) {
        BlitzardData strongestConstruct = null;
        double strongestPull = -1;

        for (BlitzardData constructData : constructList) {
            World world = plugin.getServer().getWorld(constructData.getWorldName());

            if (world == null)
                continue;

            Location constructLocation = constructData.getPosition().toLocation(world);

            if (location.getWorld() != constructLocation.getWorld())
                continue;

            double pull = Constants.BlitzardRangeFactor * constructData.getLevel() - constructLocation.distance(location);

            if (pull < 0)
                continue;

            if (pull <= strongestPull)
                continue;

            strongestPull = pull;
            strongestConstruct = constructData;
        }

        return strongestConstruct;
    }

    public LighthouseData getStrongestLighthouse(List<LighthouseData> constructList, Location location) {
        LighthouseData strongestConstruct = null;
        double strongestPull = -1;

        for (LighthouseData constructData : constructList) {
            World world = plugin.getServer().getWorld(constructData.getWorldName());

            if (world == null)
                continue;

            Location constructLocation = constructData.getPosition().toLocation(world);

            if (location.getWorld() != constructLocation.getWorld())
                continue;

            double pull = Constants.LighthouseRangeFactor * constructData.getLevel() - constructLocation.distance(location);

            if (pull < 0)
                continue;

            if (pull <= strongestPull)
                continue;

            strongestPull = pull;
            strongestConstruct = constructData;
        }

        return strongestConstruct;
    }

    public ScarecrowData getStrongestScarecrow(List<ScarecrowData> constructList, Location location) {
        ScarecrowData strongestConstruct = null;
        double strongestPull = -1;

        for (ScarecrowData constructData : constructList) {
            World world = plugin.getServer().getWorld(constructData.getWorldName());

            if (world == null)
                continue;

            Location constructLocation = constructData.getPosition().toLocation(world);

            if (location.getWorld() != constructLocation.getWorld())
                continue;

            double pull = Constants.ScareCrowRange - constructLocation.distance(location);

            if (pull < 0)
                continue;

            if (pull <= strongestPull)
                continue;

            strongestPull = pull;
            strongestConstruct = constructData;
        }

        return strongestConstruct;
    }
}
