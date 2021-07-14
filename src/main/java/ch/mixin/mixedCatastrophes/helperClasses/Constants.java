package ch.mixin.mixedCatastrophes.helperClasses;

import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.dream.DreamType;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constants {
    public static final int BlitzardRangeFactor = 10;
    public static final int LighthouseRangeFactor = 10;
    public static final int ScareCrowRange = 50;

    public static final List<AspectType> AspectOrder;
    public static final List<ConstructType> ConstructOrder;

    public static final Theme TimeDistortionTheme;
    public static final HashMap<WeatherCatastropheType, Theme> WeatherThemes;
    public static final HashMap<AspectType, Theme> AspectThemes;
    public static final HashMap<DreamType, Theme> DreamThemes;
    public static final HashMap<ConstructType, Theme> ConstructThemes;

    public static final ArrayList<Material> Airs;
    public static final ArrayList<Material> Logs;
    public static final ArrayList<Material> Flowers_Small;
    public static final ArrayList<Material> Flowers_Tall;
    public static final ArrayList<Material> PottedFlowers;
    public static final ArrayList<Material> Mirrors;
    public static final ArrayList<Material> Stones;
    public static final ArrayList<Material> Beds;
    public static final ArrayList<Material> Fires;
    public static final ArrayList<Material> HotItems;
    public static final ArrayList<Material> Lanterns;
    public static final ArrayList<Material> Torches;
    public static final ArrayList<Material> Fences;
    public static final ArrayList<Material> Cauldrons;

    public static final ConstructShape GreenWell;
    public static final ConstructShape Blitzard;
    public static final ConstructShape Lighthouse;
    public static final ConstructShape BlazeReactor;
    public static final ConstructShape Scarecrow;
    public static final ConstructShape EnderRail_Side;
    public static final ConstructShape EnderRail_Up;
    public static final ConstructShape EnderRail_Down;

    static {
        AspectOrder = new ArrayList<>();
        AspectOrder.add(AspectType.Nobility);
        AspectOrder.add(AspectType.Secrets);
        AspectOrder.add(AspectType.Terror);
        AspectOrder.add(AspectType.Nature_Conspiracy);
        AspectOrder.add(AspectType.Celestial_Favor);
        AspectOrder.add(AspectType.Misfortune);
        AspectOrder.add(AspectType.Death_Seeker);
        AspectOrder.add(AspectType.Greyhat_Debt);
        AspectOrder.add(AspectType.Resolve);

        ConstructOrder = new ArrayList<>();
        ConstructOrder.add(ConstructType.GreenWell);
        ConstructOrder.add(ConstructType.BlazeReactor);
        ConstructOrder.add(ConstructType.Blitzard);
        ConstructOrder.add(ConstructType.Lighthouse);
        ConstructOrder.add(ConstructType.Scarecrow);
        ConstructOrder.add(ConstructType.EnderRail);

        TimeDistortionTheme = new Theme(ChatColor.of("#7F7FFF"), Material.CLOCK);

        WeatherThemes = new HashMap<>();
        WeatherThemes.put(WeatherCatastropheType.Nothing, new Theme(ChatColor.of("#FFFFFF"), Material.AIR));
        WeatherThemes.put(WeatherCatastropheType.RadiantSky, new Theme(ChatColor.of("#FFBF3F"), Material.GLOWSTONE));
        WeatherThemes.put(WeatherCatastropheType.SearingCold, new Theme(ChatColor.of("#3F7FFF"), Material.ICE));
        WeatherThemes.put(WeatherCatastropheType.ThunderStorm, new Theme(ChatColor.of("#7FBFFF"), Material.CHAIN));
        WeatherThemes.put(WeatherCatastropheType.GravityLoss, new Theme(ChatColor.of("#BFBFBF"), Material.FEATHER));
        WeatherThemes.put(WeatherCatastropheType.CatsAndDogs, new Theme(ChatColor.of("#BF7F5F"), Material.BONE));

        AspectThemes = new HashMap<>();
        AspectThemes.put(AspectType.Nobility, new Theme(ChatColor.of("#FFDF7F"), Material.GOLDEN_HELMET));
        AspectThemes.put(AspectType.Secrets, new Theme(ChatColor.of("#FF00FF"), Material.MAP));
        AspectThemes.put(AspectType.Terror, new Theme(ChatColor.of("#FF0000"), Material.BEETROOT_SOUP));
        AspectThemes.put(AspectType.Nature_Conspiracy, new Theme(ChatColor.of("#3FFF00"), Material.DANDELION));
        AspectThemes.put(AspectType.Celestial_Favor, new Theme(ChatColor.of("#00FFFF"), Material.GHAST_TEAR));
        AspectThemes.put(AspectType.Misfortune, new Theme(ChatColor.of("#3F00BF"), Material.FLINT));
        AspectThemes.put(AspectType.Death_Seeker, new Theme(ChatColor.of("#3F005F"), Material.WITHER_SKELETON_SKULL));
        AspectThemes.put(AspectType.Greyhat_Debt, new Theme(ChatColor.of("#BFBFBF"), Material.IRON_HELMET));
        AspectThemes.put(AspectType.Resolve, new Theme(ChatColor.of("#DFFF7F"), Material.LANTERN));

        DreamThemes = new HashMap<>();
        DreamThemes.put(DreamType.SereneDreams, new Theme(ChatColor.of("#FF7FBF"), Material.WHITE_BED));
        DreamThemes.put(DreamType.SkyDreams, new Theme(ChatColor.of("#0000FF"), Material.LAPIS_BLOCK));
        DreamThemes.put(DreamType.BloodstainedDreams, new Theme(AspectThemes.get(AspectType.Terror).getColor(), Material.REDSTONE_BLOCK));
        DreamThemes.put(DreamType.ClockworkDreams, new Theme(ChatColor.of("#BFBFBF"), Material.IRON_BLOCK));
        DreamThemes.put(DreamType.GloryDreams, new Theme(ChatColor.of("#FFBF00"), Material.GOLD_BLOCK));
        DreamThemes.put(DreamType.PerfectDreams, new Theme(ChatColor.of("#BFFFFF"), Material.DIAMOND_BLOCK));

        ConstructThemes = new HashMap<>();
        ConstructThemes.put(ConstructType.GreenWell, new Theme(AspectThemes.get(AspectType.Nature_Conspiracy).getColor(), Material.OAK_LOG));
        ConstructThemes.put(ConstructType.BlazeReactor, new Theme(ChatColor.of("#FF7F00"), Material.MAGMA_BLOCK));
        ConstructThemes.put(ConstructType.Blitzard, new Theme(WeatherThemes.get(WeatherCatastropheType.ThunderStorm).getColor(), Material.IRON_BARS));
        ConstructThemes.put(ConstructType.Lighthouse, new Theme(ChatColor.of("#FFFF00"), Material.LANTERN));
        ConstructThemes.put(ConstructType.Scarecrow, new Theme(AspectThemes.get(AspectType.Terror).getColor(), Material.CARVED_PUMPKIN));
        ConstructThemes.put(ConstructType.EnderRail, new Theme(ChatColor.of("#00BF7F"), Material.RAIL));

        Airs = new ArrayList<>();
        Airs.add(Material.AIR);
        Airs.add(Material.CAVE_AIR);
        Airs.add(Material.VOID_AIR);

        Logs = new ArrayList<>();
        Logs.add(Material.ACACIA_LOG);
        Logs.add(Material.BIRCH_LOG);
        Logs.add(Material.DARK_OAK_LOG);
        Logs.add(Material.JUNGLE_LOG);
        Logs.add(Material.OAK_LOG);
        Logs.add(Material.SPRUCE_LOG);
        Logs.add(Material.CRIMSON_STEM);
        Logs.add(Material.WARPED_STEM);

        Flowers_Small = new ArrayList<>();
        Flowers_Small.add(Material.DANDELION);
        Flowers_Small.add(Material.POPPY);
        Flowers_Small.add(Material.BLUE_ORCHID);
        Flowers_Small.add(Material.ALLIUM);
        Flowers_Small.add(Material.AZURE_BLUET);
        Flowers_Small.add(Material.RED_TULIP);
        Flowers_Small.add(Material.PINK_TULIP);
        Flowers_Small.add(Material.WHITE_TULIP);
        Flowers_Small.add(Material.ORANGE_TULIP);
        Flowers_Small.add(Material.OXEYE_DAISY);
        Flowers_Small.add(Material.CORNFLOWER);
        Flowers_Small.add(Material.LILY_OF_THE_VALLEY);
        Flowers_Small.add(Material.WITHER_ROSE);

        Flowers_Tall = new ArrayList<>();
        Flowers_Tall.add(Material.SUNFLOWER);
        Flowers_Tall.add(Material.LILAC);
        Flowers_Tall.add(Material.ROSE_BUSH);
        Flowers_Tall.add(Material.PEONY);

        PottedFlowers = new ArrayList<>();
        PottedFlowers.add(Material.POTTED_DANDELION);
        PottedFlowers.add(Material.POTTED_POPPY);
        PottedFlowers.add(Material.POTTED_BLUE_ORCHID);
        PottedFlowers.add(Material.POTTED_ALLIUM);
        PottedFlowers.add(Material.POTTED_AZURE_BLUET);
        PottedFlowers.add(Material.POTTED_RED_TULIP);
        PottedFlowers.add(Material.POTTED_PINK_TULIP);
        PottedFlowers.add(Material.POTTED_WHITE_TULIP);
        PottedFlowers.add(Material.POTTED_ORANGE_TULIP);
        PottedFlowers.add(Material.POTTED_OXEYE_DAISY);
        PottedFlowers.add(Material.POTTED_CORNFLOWER);
        PottedFlowers.add(Material.POTTED_LILY_OF_THE_VALLEY);
        PottedFlowers.add(Material.POTTED_WITHER_ROSE);

        Mirrors = new ArrayList<>();
        Mirrors.add(Material.GLASS);
        Mirrors.add(Material.GLASS_PANE);

        Mirrors.add(Material.BLACK_STAINED_GLASS);
        Mirrors.add(Material.BLUE_STAINED_GLASS);
        Mirrors.add(Material.BROWN_STAINED_GLASS);
        Mirrors.add(Material.CYAN_STAINED_GLASS);
        Mirrors.add(Material.GRAY_STAINED_GLASS);
        Mirrors.add(Material.GREEN_STAINED_GLASS);
        Mirrors.add(Material.LIGHT_BLUE_STAINED_GLASS);
        Mirrors.add(Material.LIGHT_GRAY_STAINED_GLASS);
        Mirrors.add(Material.LIME_STAINED_GLASS);
        Mirrors.add(Material.MAGENTA_STAINED_GLASS);
        Mirrors.add(Material.ORANGE_STAINED_GLASS);
        Mirrors.add(Material.PINK_STAINED_GLASS);
        Mirrors.add(Material.PURPLE_STAINED_GLASS);
        Mirrors.add(Material.RED_STAINED_GLASS);
        Mirrors.add(Material.WHITE_STAINED_GLASS);
        Mirrors.add(Material.YELLOW_STAINED_GLASS);
        Mirrors.add(Material.BLACK_STAINED_GLASS_PANE);
        Mirrors.add(Material.BLUE_STAINED_GLASS_PANE);
        Mirrors.add(Material.BROWN_STAINED_GLASS_PANE);
        Mirrors.add(Material.CYAN_STAINED_GLASS_PANE);
        Mirrors.add(Material.GRAY_STAINED_GLASS_PANE);
        Mirrors.add(Material.GREEN_STAINED_GLASS_PANE);
        Mirrors.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        Mirrors.add(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        Mirrors.add(Material.LIME_STAINED_GLASS_PANE);
        Mirrors.add(Material.MAGENTA_STAINED_GLASS_PANE);
        Mirrors.add(Material.ORANGE_STAINED_GLASS_PANE);
        Mirrors.add(Material.PINK_STAINED_GLASS_PANE);
        Mirrors.add(Material.PURPLE_STAINED_GLASS_PANE);
        Mirrors.add(Material.RED_STAINED_GLASS_PANE);
        Mirrors.add(Material.WHITE_STAINED_GLASS_PANE);
        Mirrors.add(Material.YELLOW_STAINED_GLASS_PANE);

        Stones = new ArrayList<>();
        Stones.add(Material.STONE);
        Stones.add(Material.COBBLESTONE);

        Beds = new ArrayList<>();
        Beds.add(Material.BLACK_BED);
        Beds.add(Material.BLUE_BED);
        Beds.add(Material.BROWN_BED);
        Beds.add(Material.CYAN_BED);
        Beds.add(Material.GRAY_BED);
        Beds.add(Material.GREEN_BED);
        Beds.add(Material.LIGHT_BLUE_BED);
        Beds.add(Material.LIGHT_GRAY_BED);
        Beds.add(Material.LIME_BED);
        Beds.add(Material.MAGENTA_BED);
        Beds.add(Material.ORANGE_BED);
        Beds.add(Material.PINK_BED);
        Beds.add(Material.PURPLE_BED);
        Beds.add(Material.RED_BED);
        Beds.add(Material.WHITE_BED);
        Beds.add(Material.YELLOW_BED);

        Fires = new ArrayList<>();
        Fires.add(Material.FIRE);
        Fires.add(Material.SOUL_FIRE);

        HotItems = new ArrayList<>();
        HotItems.add(Material.GLOWSTONE);
        HotItems.add(Material.LAVA_BUCKET);
        HotItems.add(Material.SOUL_TORCH);
        HotItems.add(Material.TORCH);

        Lanterns = new ArrayList<>();
        Lanterns.add(Material.LANTERN);
        Lanterns.add(Material.SOUL_LANTERN);

        Torches = new ArrayList<>();
        Torches.add(Material.TORCH);
        Torches.add(Material.WALL_TORCH);
        Torches.add(Material.SOUL_TORCH);
        Torches.add(Material.SOUL_WALL_TORCH);
        Torches.add(Material.REDSTONE_TORCH);
        Torches.add(Material.REDSTONE_WALL_TORCH);

        Fences = new ArrayList<>();
        Fences.add(Material.ACACIA_FENCE);
        Fences.add(Material.BIRCH_FENCE);
        Fences.add(Material.CRIMSON_FENCE);
        Fences.add(Material.DARK_OAK_FENCE);
        Fences.add(Material.JUNGLE_FENCE);
        Fences.add(Material.NETHER_BRICK_FENCE);
        Fences.add(Material.OAK_FENCE);
        Fences.add(Material.SPRUCE_FENCE);
        Fences.add(Material.WARPED_FENCE);

        Cauldrons = new ArrayList<>();
        Cauldrons.add(Material.CAULDRON);
        Cauldrons.add(Material.LAVA_CAULDRON);
        Cauldrons.add(Material.POWDER_SNOW_CAULDRON);
        Cauldrons.add(Material.WATER_CAULDRON);

        GreenWell = new ConstructShape(RotationSymmetry.Degrees90, new HashMap<Coordinate3D, Material>() {{
            put(new Coordinate3D(0, 0, 0), Material.WATER);
        }}, new HashMap<Coordinate3D, List<Material>>() {{
            put(new Coordinate3D(-1, 0, -1), Logs);
            put(new Coordinate3D(-1, 0, 0), Logs);
            put(new Coordinate3D(-1, 0, 1), Logs);
            put(new Coordinate3D(0, 0, -1), Logs);
            put(new Coordinate3D(0, 0, 1), Logs);
            put(new Coordinate3D(1, 0, -1), Logs);
            put(new Coordinate3D(1, 0, 0), Logs);
            put(new Coordinate3D(1, 0, 1), Logs);
        }});

        Blitzard = new ConstructShape(RotationSymmetry.Degrees90, new HashMap<Coordinate3D, Material>() {{
            put(new Coordinate3D(0, 0, 0), Material.QUARTZ_BLOCK);
            put(new Coordinate3D(0, 1, 0), Material.IRON_BARS);
            put(new Coordinate3D(0, 2, 0), Material.IRON_BARS);
            put(new Coordinate3D(0, 3, 0), Material.IRON_BARS);
            put(new Coordinate3D(1, 3, 0), Material.IRON_BARS);
            put(new Coordinate3D(-1, 3, 0), Material.IRON_BARS);
            put(new Coordinate3D(0, 3, 1), Material.IRON_BARS);
            put(new Coordinate3D(0, 3, -1), Material.IRON_BARS);
            put(new Coordinate3D(0, 4, 0), Material.IRON_BARS);
        }});

        Lighthouse = new ConstructShape(RotationSymmetry.Degrees90, new HashMap<Coordinate3D, Material>() {{
            put(new Coordinate3D(0, 0, 0), Material.GLOWSTONE);
            put(new Coordinate3D(0, 1, 0), Material.STONE_BRICK_WALL);
            put(new Coordinate3D(0, 2, 0), Material.STONE_BRICK_WALL);
            put(new Coordinate3D(0, 3, 0), Material.STONE_BRICK_WALL);
            put(new Coordinate3D(0, 4, 0), Material.STONE_BRICKS);
            put(new Coordinate3D(1, 4, 0), Material.STONE_BRICK_STAIRS);
            put(new Coordinate3D(-1, 4, 0), Material.STONE_BRICK_STAIRS);
            put(new Coordinate3D(0, 4, 1), Material.STONE_BRICK_STAIRS);
            put(new Coordinate3D(0, 4, -1), Material.STONE_BRICK_STAIRS);
        }}, new HashMap<Coordinate3D, List<Material>>() {{
            put(new Coordinate3D(1, 3, 0), Lanterns);
            put(new Coordinate3D(-1, 3, 0), Lanterns);
            put(new Coordinate3D(0, 3, 1), Lanterns);
            put(new Coordinate3D(0, 3, -1), Lanterns);
        }});

        BlazeReactor = new ConstructShape(RotationSymmetry.Degrees360, new HashMap<Coordinate3D, Material>() {{
            put(new Coordinate3D(2, 0, 0), Material.LAVA);
            put(new Coordinate3D(1, 0, -1), Material.MAGMA_BLOCK);
            put(new Coordinate3D(1, 0, 0), Material.LAVA);
            put(new Coordinate3D(1, 0, 1), Material.MAGMA_BLOCK);
            put(new Coordinate3D(0, 0, -1), Material.BRICKS);
            put(new Coordinate3D(0, 0, 0), Material.LAVA);
            put(new Coordinate3D(0, 0, 1), Material.BRICKS);
            put(new Coordinate3D(-1, 0, -1), Material.BRICKS);
            put(new Coordinate3D(-1, 0, 0), Material.BRICKS);
            put(new Coordinate3D(-1, 0, 1), Material.BRICKS);
            put(new Coordinate3D(2, -1, 0), Material.LAVA);
            put(new Coordinate3D(1, -1, -1), Material.BRICKS);
            put(new Coordinate3D(1, -1, 0), Material.BRICKS);
            put(new Coordinate3D(1, -1, 1), Material.BRICKS);
            put(new Coordinate3D(0, -1, -1), Material.BRICKS);
            put(new Coordinate3D(0, -1, 0), Material.BRICKS);
            put(new Coordinate3D(0, -1, 1), Material.BRICKS);
            put(new Coordinate3D(-1, -1, -1), Material.BRICKS);
            put(new Coordinate3D(-1, -1, 1), Material.BRICKS);
        }}, new HashMap<Coordinate3D, List<Material>>() {{
            put(new Coordinate3D(3, -1, -1), Cauldrons);
            put(new Coordinate3D(3, -1, 0), Cauldrons);
            put(new Coordinate3D(3, -1, 1), Cauldrons);
            put(new Coordinate3D(2, -1, -1), Cauldrons);
            put(new Coordinate3D(2, -1, 1), Cauldrons);
            put(new Coordinate3D(-1, -1, 0), Airs);
        }});

        Scarecrow = new ConstructShape(RotationSymmetry.Degrees180, new HashMap<Coordinate3D, Material>() {{
            put(new Coordinate3D(0, 0, 0), Material.JACK_O_LANTERN);
            put(new Coordinate3D(0, -1, 0), Material.HAY_BLOCK);
            put(new Coordinate3D(0, -2, -2), Material.CHAIN);
            put(new Coordinate3D(0, -2, -1), Material.CHAIN);
            put(new Coordinate3D(0, -2, 1), Material.CHAIN);
            put(new Coordinate3D(0, -2, 2), Material.CHAIN);
            put(new Coordinate3D(0, -3, -2), Material.CHAIN);
            put(new Coordinate3D(0, -3, 2), Material.CHAIN);
            put(new Coordinate3D(0, -4, 0), Material.SOUL_SAND);
        }}, new HashMap<Coordinate3D, List<Material>>() {{
            put(new Coordinate3D(0, 0, -2), Torches);
            put(new Coordinate3D(0, 0, 2), Torches);
            put(new Coordinate3D(0, -1, -2), Fences);
            put(new Coordinate3D(0, -1, -1), Fences);
            put(new Coordinate3D(0, -1, 1), Fences);
            put(new Coordinate3D(0, -1, 2), Fences);
            put(new Coordinate3D(0, -2, 0), Fences);
            put(new Coordinate3D(0, -3, 0), Fences);
        }});

        EnderRail_Side = new ConstructShape(RotationSymmetry.Degrees360, new HashMap<Coordinate3D, Material>() {{
            put(new Coordinate3D(-3, 0, -1), Material.END_STONE);
            put(new Coordinate3D(-3, 0, 0), Material.END_STONE);
            put(new Coordinate3D(-3, 0, 1), Material.END_STONE);
            put(new Coordinate3D(-2, 0, -1), Material.END_STONE);
            put(new Coordinate3D(-2, 0, 0), Material.REDSTONE_BLOCK);
            put(new Coordinate3D(-2, 0, 1), Material.END_STONE);
            put(new Coordinate3D(-1, 0, -1), Material.END_STONE);
            put(new Coordinate3D(-1, 0, 0), Material.END_STONE);
            put(new Coordinate3D(-1, 0, 1), Material.END_STONE);
            put(new Coordinate3D(0, 0, -1), Material.END_STONE);
            put(new Coordinate3D(0, 0, 0), Material.LAPIS_BLOCK);
            put(new Coordinate3D(0, 0, 1), Material.END_STONE);
            put(new Coordinate3D(-3, 1, -1), Material.QUARTZ_BLOCK);
            put(new Coordinate3D(-3, 1, 0), Material.QUARTZ_SLAB);
            put(new Coordinate3D(-3, 1, 1), Material.QUARTZ_BLOCK);
            put(new Coordinate3D(-2, 1, -1), Material.END_ROD);
            put(new Coordinate3D(-2, 1, 0), Material.POWERED_RAIL);
            put(new Coordinate3D(-2, 1, 1), Material.END_ROD);
            put(new Coordinate3D(-1, 1, -1), Material.END_ROD);
            put(new Coordinate3D(-1, 1, 0), Material.POWERED_RAIL);
            put(new Coordinate3D(-1, 1, 1), Material.END_ROD);
            put(new Coordinate3D(0, 1, -1), Material.QUARTZ_BLOCK);
            put(new Coordinate3D(0, 1, 0), Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
            put(new Coordinate3D(0, 1, 1), Material.QUARTZ_BLOCK);
        }});

        EnderRail_Up = new ConstructShape(RotationSymmetry.Degrees90, new HashMap<Coordinate3D, Material>() {{
            put(new Coordinate3D(-1, 0, -1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(-1, 0, 0), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(-1, 0, 1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(0, 0, -1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(0, 0, 0), Material.LAPIS_BLOCK);
            put(new Coordinate3D(0, 0, 1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(1, 0, -1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(1, 0, 0), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(1, 0, 1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(-1, 1, -1), Material.END_ROD);
            put(new Coordinate3D(-1, 1, 1), Material.END_ROD);
            put(new Coordinate3D(0, 1, 0), Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
            put(new Coordinate3D(1, 1, -1), Material.END_ROD);
            put(new Coordinate3D(1, 1, 1), Material.END_ROD);
        }});

        EnderRail_Down = new ConstructShape(RotationSymmetry.Degrees90, new HashMap<Coordinate3D, Material>() {{
            put(new Coordinate3D(-1, 0, -1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(-1, 0, 0), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(-1, 0, 1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(0, 0, -1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(0, 0, 0), Material.LAPIS_BLOCK);
            put(new Coordinate3D(0, 0, 1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(1, 0, -1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(1, 0, 0), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(1, 0, 1), Material.END_STONE_BRICK_STAIRS);
            put(new Coordinate3D(-1, 1, -1), Material.END_ROD);
            put(new Coordinate3D(-1, 1, 1), Material.END_ROD);
            put(new Coordinate3D(0, 1, 0), Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
            put(new Coordinate3D(1, 1, -1), Material.END_ROD);
            put(new Coordinate3D(1, 1, 1), Material.END_ROD);
        }});
    }
}
