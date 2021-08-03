package ch.mixin.mixedCatastrophes.catastropheManager.global.constructs;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.helperClasses.*;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.*;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ConstructManager extends CatastropheManager {
    private final HashMap<ConstructData, ConstructCache> cacheMap = new HashMap<>();

    private final int checkConstructedDuration = 10;
    private int checkConstructedTimer;

    public ConstructManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
    }

    @Override
    public void initializeMetaData() {
        if (mixedCatastrophesData.getMetaData().getGreenWellDataList() == null)
            mixedCatastrophesData.getMetaData().setGreenWellDataList(new ArrayList<>());

        if (mixedCatastrophesData.getMetaData().getBlitzardDataList() == null)
            mixedCatastrophesData.getMetaData().setBlitzardDataList(new ArrayList<>());

        if (mixedCatastrophesData.getMetaData().getLighthouseDataList() == null)
            mixedCatastrophesData.getMetaData().setLighthouseDataList(new ArrayList<>());

        if (mixedCatastrophesData.getMetaData().getBlazeReactorList() == null)
            mixedCatastrophesData.getMetaData().setBlazeReactorList(new ArrayList<>());

        if (mixedCatastrophesData.getMetaData().getScarecrowDataList() == null)
            mixedCatastrophesData.getMetaData().setScarecrowDataList(new ArrayList<>());

        if (mixedCatastrophesData.getMetaData().getEnderRailDataList() == null)
            mixedCatastrophesData.getMetaData().setEnderRailDataList(new ArrayList<>());
    }

    @Override
    public void updateMetaData() {
    }

    @Override
    public void initializeCauser() {
    }

    @Override
    public void tick() {
        if (mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers().size() == 0)
            return;

        tickGreenWell();
        tickBlitzard();
        tickLighthouse();
        tickBlazeReactor();
        tickScarecrow();
        tickEnderRail();

        if (checkConstructedTimer <= 0) {
            checkConstructedTimer = checkConstructedDuration;
        }

        checkConstructedTimer--;
    }

    public void removeHolograms() {
        if (!mixedCatastrophesData.isUseHolographicDisplays())
            return;

        MixedCatastrophesPlugin plugin = mixedCatastrophesData.getPlugin();

        for (Hologram hologram : HologramsAPI.getHolograms(plugin)) {
            hologram.delete();
        }
    }

    private void generateGreenWellHologram(GreenWellData data, boolean isConstructed) {
        ChatColor color = Constants.ConstructThemes.get(ConstructType.GreenWell).getColor();
        ArrayList<String> lines = new ArrayList<>();
        lines.add(color + Functions.splitCamelCase(ConstructType.GreenWell.toString()));
        lines.add(color + "Level: " + data.getLevel());

        if (isConstructed) {
            lines.add(color + "Active");
        } else {
            lines.add(color + "Inactive");
        }

        generateConstructHologram(data, lines, isConstructed, 2);
    }

    private void generateBlazeReactorHologram(BlazeReactorData data, boolean isConstructed) {
        ChatColor color = Constants.ConstructThemes.get(ConstructType.BlazeReactor).getColor();
        ArrayList<String> lines = new ArrayList<>();
        lines.add(color + Functions.splitCamelCase(ConstructType.BlazeReactor.toString()));
        lines.add(color + "Level: " + data.getLevel());

        if (isConstructed) {
            lines.add(color + "Active");
        } else {
            lines.add(color + "Inactive");
        }

        generateConstructHologram(data, lines, isConstructed, 2);
    }

    private void generateBlitzardHologram(BlitzardData data, boolean isConstructed) {
        ChatColor color = Constants.ConstructThemes.get(ConstructType.Blitzard).getColor();
        ArrayList<String> lines = new ArrayList<>();
        lines.add(color + Functions.splitCamelCase(ConstructType.Blitzard.toString()));
        lines.add(color + "Level: " + data.getLevel());

        if (isConstructed) {
            lines.add(color + "Active");
        } else {
            lines.add(color + "Inactive");
        }

        generateConstructHologram(data, lines, isConstructed, 6);
    }

    private void generateLighthouseHologram(LighthouseData data, boolean isConstructed) {
        ChatColor color = Constants.ConstructThemes.get(ConstructType.Lighthouse).getColor();
        ArrayList<String> lines = new ArrayList<>();
        lines.add(color + Functions.splitCamelCase(ConstructType.Lighthouse.toString()));
        lines.add(color + "Level: " + data.getLevel());

        if (isConstructed) {
            lines.add(color + "Active");
        } else {
            lines.add(color + "Inactive");
        }

        generateConstructHologram(data, lines, isConstructed, 6);
    }

    private void generateScarecrowHologram(ScarecrowData data, boolean isConstructed) {
        ChatColor color = Constants.ConstructThemes.get(ConstructType.Scarecrow).getColor();
        ArrayList<String> lines = new ArrayList<>();
        lines.add(color + Functions.splitCamelCase(ConstructType.Scarecrow.toString()));
        lines.add(color + "Terror: " + data.getCollectedTerror());

        if (isConstructed) {
            lines.add(color + "Active");
        } else {
            lines.add(color + "Inactive");
        }

        generateConstructHologram(data, lines, isConstructed, 2);
    }

    private void generateEnderRailHologram(EnderRailData data, boolean isConstructed) {
        ChatColor color = Constants.ConstructThemes.get(ConstructType.EnderRail).getColor();
        ArrayList<String> lines = new ArrayList<>();
        lines.add(color + Functions.splitCamelCase(ConstructType.EnderRail.toString()));
        lines.add(color + "Level: " + data.getLevel());
        lines.add(color + "Direction: " + data.getDirection());

        if (isConstructed) {
            lines.add(color + "Active");
        } else {
            lines.add(color + "Inactive");
        }

        generateConstructHologram(data, lines, isConstructed, 3);
    }

    private void generateConstructHologram(ConstructData data, ArrayList<String> lines, boolean isConstructed, int height) {
        World world = mixedCatastrophesData.getPlugin().getServer().getWorld(data.getWorldName());

        if (world == null)
            return;

        ConstructCache constructCache = cacheMap.get(data);

        if (constructCache.getHologram() == null) {
            Location location = data.getPosition().toLocation(world).add(0.5, height + 0.5, 0.5);
            Hologram hologram = makeHologram(lines, location);
            constructCache.setHologram(hologram);
        } else if (constructCache.isChanged() || constructCache.isActive() != isConstructed) {
            constructCache.setChanged(false);
            constructCache.setActive(isConstructed);
            fillHologram(constructCache.getHologram(), lines);
        }
    }

    private Hologram makeHologram(ArrayList<String> lines, Location location) {
        Hologram hologram = HologramsAPI.createHologram(mixedCatastrophesData.getPlugin(), location);
        for (String line : lines) {
            TextLine t = hologram.appendTextLine(line);
        }

        return hologram;
    }

    private void fillHologram(Hologram hologram, ArrayList<String> lines) {
        hologram.clearLines();
        for (String line : lines) {
            TextLine t = hologram.appendTextLine(line);
        }
    }


    private void tickConstructCache(List<ConstructData> constructDataList, ConstructShape constructShape) {
        tickConstructCache(constructDataList, new ArrayList<>(Arrays.asList(constructShape)));
    }

    private void tickConstructCache(List<ConstructData> constructDataList, List<ConstructShape> constructShapeList) {
        for (int i = 0; i < constructDataList.size(); i++) {
            ConstructData constructData = constructDataList.get(i);
            ConstructCache constructCache = cacheMap.get(constructData);

            if (constructCache == null) {
                constructCache = new ConstructCache(null, true, false, null);
                cacheMap.put(constructData, constructCache);
            }

            boolean newlyActive = constructCache.isActive();
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(constructData.getWorldName());

            if (world == null) {
                newlyActive = false;
            } else if (checkConstructedTimer <= 0 || constructCache.getShapeCompareResult() == null) {
                Location location = constructData.getPosition().toLocation(world);
                ShapeCompareResult shapeCompareResult = null;

                for (ConstructShape constructShape : constructShapeList){
                    shapeCompareResult = constructShape.checkConstructed(location);

                    if (shapeCompareResult.isConstructed())
                        break;
                }

                constructCache.setShapeCompareResult(shapeCompareResult);
                newlyActive = shapeCompareResult.isConstructed();
            }

            if (constructCache.isActive() != newlyActive) {
                constructCache.setChanged(true);
            }

            constructCache.setActive(newlyActive);

            if (!constructCache.isActive()) {
                constructData.inactiveTick();

                if (constructData.getInactiveTime() >= 60 * 60) {
                    cacheMap.remove(constructData);
                    constructDataList.remove(i);
                    i--;
                }
            }
        }
    }

    private void tickGreenWell() {
        List<GreenWellData> greenWellDataList = mixedCatastrophesData.getMetaData().getGreenWellDataList();

        List<? extends ConstructData> constructDataList = greenWellDataList;
        tickConstructCache((List<ConstructData>) constructDataList, Constants.GreenWell);

        for (GreenWellData greenWellData : greenWellDataList) {
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(greenWellData.getWorldName());

            if (world == null)
                continue;

            ConstructCache constructCache = cacheMap.get(greenWellData);

            boolean isActive = constructCache.isActive();
            generateGreenWellHologram(greenWellData, isActive);

            if (!isActive)
                continue;

            int level = greenWellData.getLevel();
            Coordinate3D center = greenWellData.getPosition();
            Location locationCenter = center.toLocation(world);
            Location locationCenterMiddle = center.sum(0.5, 0.5, 0.5).toLocation(world);

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(center);
            particles.add(center.sum(0, 1, 0));

            mixedCatastrophesData.getParticler().spawnParticles(Particle.VILLAGER_HAPPY, particles, world, level * 0.25, 4, 5);

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

    private void tickBlazeReactor() {
        List<BlazeReactorData> blazeReactorDataList = mixedCatastrophesData.getMetaData().getBlazeReactorList();

        List<? extends ConstructData> constructDataList = blazeReactorDataList;
        tickConstructCache((List<ConstructData>) constructDataList, Constants.BlazeReactor);

        for (BlazeReactorData blazeReactorData : blazeReactorDataList) {
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(blazeReactorData.getWorldName());

            if (world == null)
                continue;

            ConstructCache constructCache = cacheMap.get(blazeReactorData);

            boolean isActive = constructCache.isActive();
            generateBlazeReactorHologram(blazeReactorData, isActive);

            if (!isActive)
                continue;

            Coordinate3D center = blazeReactorData.getPosition();

            int level = blazeReactorData.getLevel();
            int fuel = blazeReactorData.getFuel();

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(new Coordinate3D(0, 0, 0));
            particles.add(new Coordinate3D(2, -1, 0));

            for (int i = 0; i < particles.size(); i++) {
                Coordinate3D particle = particles.get(i).rotateY90Clockwise(constructCache.getShapeCompareResult().getRotations());
                particles.remove(i);
                particles.add(i, particle.sum(center));
            }

            mixedCatastrophesData.getParticler().spawnParticles(Particle.SMOKE_LARGE, particles, world, new Coordinate3D(0, 0.2 + level * 0.02, 0), Math.pow(level, 1.5) * 0.15, 4, 5);

            if (fuel == 0) {
                List<Coordinate3D> relativeCauldronList = new ArrayList<>();
                relativeCauldronList.add(new Coordinate3D(3, -1, -1));
                relativeCauldronList.add(new Coordinate3D(3, -1, 0));
                relativeCauldronList.add(new Coordinate3D(3, -1, 1));
                relativeCauldronList.add(new Coordinate3D(2, -1, -1));
                relativeCauldronList.add(new Coordinate3D(2, -1, 1));

                List<Location> cauldronList = new ArrayList<>();

                for (Coordinate3D relativeCauldron : relativeCauldronList) {
                    cauldronList.add(relativeCauldron.rotateY90Clockwise(constructCache.getShapeCompareResult().getRotations()).sum(center).toLocation(world));
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
                continue;

            fuel--;
            blazeReactorData.setFuel(fuel);

            double chances = 0.1 * blazeReactorData.getLevel();
            int amount = (int) Math.floor(chances);
            chances = chances % 1;

            if (new Random().nextDouble() < chances)
                amount++;

            if (amount > 0) {
                Coordinate3D relativeDrop = new Coordinate3D(-1, -1, 0).rotateY90Clockwise(constructCache.getShapeCompareResult().getRotations());
                Location dropLocation = relativeDrop.sum(center).sum(0.5, 0.5, 0.5).toLocation(world);
                world.dropItem(dropLocation, new ItemStack(Material.COBBLESTONE, amount));
            }
        }
    }

    private void tickBlitzard() {
        List<BlitzardData> blitzardDataList = mixedCatastrophesData.getMetaData().getBlitzardDataList();

        List<? extends ConstructData> constructDataList = blitzardDataList;
        tickConstructCache((List<ConstructData>) constructDataList, Constants.Blitzard);

        for (BlitzardData blitzardData : blitzardDataList) {
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(blitzardData.getWorldName());

            if (world == null)
                continue;

            ConstructCache constructCache = cacheMap.get(blitzardData);

            boolean isActive = constructCache.isActive();
            generateBlitzardHologram(blitzardData, isActive);

            if (!isActive)
                continue;

            int level = blitzardData.getLevel();

            Coordinate3D center = blitzardData.getPosition();

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(center);
            particles.add(center.sum(0, 1, 0));

            mixedCatastrophesData.getParticler().spawnParticles(Particle.SPELL_MOB, particles, world, Math.pow(level, 1.25) * 0.2, 4, 5);
        }
    }

    private void tickLighthouse() {
        List<LighthouseData> lighthouseDataList = mixedCatastrophesData.getMetaData().getLighthouseDataList();

        List<? extends ConstructData> constructDataList = lighthouseDataList;
        tickConstructCache((List<ConstructData>) constructDataList, Constants.Lighthouse);

        for (LighthouseData lighthouseData : lighthouseDataList) {
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(lighthouseData.getWorldName());

            if (world == null)
                continue;

            ConstructCache constructCache = cacheMap.get(lighthouseData);

            boolean isActive = constructCache.isActive();
            generateLighthouseHologram(lighthouseData, isActive);

            if (!isActive)
                continue;

            int level = lighthouseData.getLevel();

            Coordinate3D center = lighthouseData.getPosition();

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(center);
            particles.add(center.sum(0, 1, 0));

            mixedCatastrophesData.getParticler().spawnParticles(Particle.LAVA, particles, world, Math.pow(level, 1.1) * 0.25, 4, 5);
        }
    }

    private void tickScarecrow() {
        List<ScarecrowData> scarecrowDataList = mixedCatastrophesData.getMetaData().getScarecrowDataList();

        List<? extends ConstructData> constructDataList = scarecrowDataList;
        tickConstructCache((List<ConstructData>) constructDataList, Constants.Scarecrow);

        for (ScarecrowData scarecrowData : scarecrowDataList) {
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(scarecrowData.getWorldName());

            if (world == null)
                continue;

            ConstructCache constructCache = cacheMap.get(scarecrowData);

            boolean isActive = constructCache.isActive();
            generateScarecrowHologram(scarecrowData, isActive);

            if (!isActive)
                continue;

            Coordinate3D center = scarecrowData.getPosition();

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(new Coordinate3D(0, 0, -2));
            particles.add(new Coordinate3D(0, 0, 2));

            for (int i = 0; i < particles.size(); i++) {
                Coordinate3D particle = particles.get(i).rotateY90Clockwise(constructCache.getShapeCompareResult().getRotations());
                particles.remove(i);
                particles.add(i, particle.sum(center));
            }

            int terror = scarecrowData.getCollectedTerror();
            mixedCatastrophesData.getParticler().spawnParticles(Particle.SOUL, particles, world, new Coordinate3D(0, 0.1 + Math.pow(terror, 0.5) * 0.01, 0), 0.1 + terror * 0.01, 4, 5);
        }
    }

    private void tickEnderRail() {
        List<EnderRailData> enderRailDataList = mixedCatastrophesData.getMetaData().getEnderRailDataList();

        List<? extends ConstructData> constructDataList = enderRailDataList;
        tickConstructCache((List<ConstructData>) constructDataList, new ArrayList<>(Arrays.asList(
                Constants.EnderRail_Side,
                Constants.EnderRail_Down,
                Constants.EnderRail_Up
        )));

        for (EnderRailData enderRailData : enderRailDataList) {
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(enderRailData.getWorldName());

            if (world == null)
                continue;

            ConstructCache constructCache = cacheMap.get(enderRailData);
            boolean isActive = constructCache.isActive();
            generateEnderRailHologram(enderRailData, isActive);

            if (!isActive)
                continue;

            int level = enderRailData.getLevel();

            Coordinate3D center = enderRailData.getPosition();

            List<Coordinate3D> particles = new ArrayList<>();
            particles.add(center);
            particles.add(center.sum(0, 1, 0));

            mixedCatastrophesData.getParticler().spawnParticles(Particle.PORTAL, particles, world, 1 + level * 0.25, 4, 5);
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

    public List<BlitzardData> getBlitzardListIsActive(List<BlitzardData> constructDataList) {
        List<BlitzardData> constructDataListIsConstructed = new ArrayList<>();

        for (BlitzardData constructData : constructDataList) {
            ConstructCache constructCache = cacheMap.get(constructData);

            if (constructCache == null)
                continue;

            if (constructCache.isActive())
                constructDataListIsConstructed.add(constructData);
        }

        return constructDataListIsConstructed;
    }

    public List<LighthouseData> getLighthouseListIsConstructed(List<LighthouseData> constructDataList) {
        List<LighthouseData> constructDataListIsConstructed = new ArrayList<>();

        for (LighthouseData constructData : constructDataList) {
            ConstructCache constructCache = cacheMap.get(constructData);

            if (constructCache == null)
                continue;

            if (constructCache.isActive())
                constructDataListIsConstructed.add(constructData);
        }

        return constructDataListIsConstructed;
    }

    public List<ScarecrowData> getScarecrowListIsConstructed(List<ScarecrowData> constructDataList) {
        List<ScarecrowData> constructDataListIsConstructed = new ArrayList<>();

        for (ScarecrowData constructData : constructDataList) {
            ConstructCache constructCache = cacheMap.get(constructData);

            if (constructCache == null)
                continue;

            if (constructCache.isActive())
                constructDataListIsConstructed.add(constructData);
        }

        return constructDataListIsConstructed;
    }

    public BlitzardData getStrongestBlitzard(List<BlitzardData> constructList, Location location) {
        BlitzardData strongestConstruct = null;
        double strongestPull = -1;

        for (BlitzardData constructData : constructList) {
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(constructData.getWorldName());

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
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(constructData.getWorldName());

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
            World world = mixedCatastrophesData.getPlugin().getServer().getWorld(constructData.getWorldName());

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

    public void constructChanged(ConstructData constructData) {
        ConstructCache constructCache = cacheMap.get(constructData);

        if (constructCache == null)
            return;

        constructCache.setChanged(true);
    }
}
