package ch.mixin.helperClasses;

import ch.mixin.eventChange.aspect.AspectType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Constants {
    public static final HashMap<AspectType, ChatColor> AspectThemes;

    public static final ArrayList<Material> Airs;
    public static final ArrayList<Material> Logs;
    public static final ArrayList<Material> Flowers;
    public static final ArrayList<Material> PottedFlowers;
    public static final ArrayList<Material> Mirrors;
    public static final ArrayList<Material> Stones;
    public static final ArrayList<Material> Beds;
    public static final ArrayList<Material> Fires;
    public static final ArrayList<Material> HotItems;
    public static final ArrayList<Material> Lanterns;

    public static final ConstructShape Greenwell;
    public static final ConstructShape Blitzard;
    public static final ConstructShape Lighthouse;

    static {
        AspectThemes = new HashMap<>();
        AspectThemes.put(AspectType.Death_Seeker, ChatColor.BLACK);
        AspectThemes.put(AspectType.Secrets, ChatColor.DARK_PURPLE);
        AspectThemes.put(AspectType.Terror, ChatColor.DARK_RED);
        AspectThemes.put(AspectType.Nature_Conspiracy, ChatColor.DARK_GREEN);
        AspectThemes.put(AspectType.Misfortune, ChatColor.DARK_BLUE);
        AspectThemes.put(AspectType.Greyhat_Debt, ChatColor.DARK_GRAY);
        AspectThemes.put(AspectType.Celestial_Favor, ChatColor.AQUA);

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

        Flowers = new ArrayList<>();
        Flowers.add(Material.DANDELION);
        Flowers.add(Material.POPPY);
        Flowers.add(Material.BLUE_ORCHID);
        Flowers.add(Material.ALLIUM);
        Flowers.add(Material.AZURE_BLUET);
        Flowers.add(Material.RED_TULIP);
        Flowers.add(Material.PINK_TULIP);
        Flowers.add(Material.WHITE_TULIP);
        Flowers.add(Material.ORANGE_TULIP);
        Flowers.add(Material.OXEYE_DAISY);
        Flowers.add(Material.CORNFLOWER);
        Flowers.add(Material.LILY_OF_THE_VALLEY);
        Flowers.add(Material.WITHER_ROSE);
        Flowers.add(Material.SUNFLOWER);
        Flowers.add(Material.LILAC);
        Flowers.add(Material.ROSE_BUSH);
        Flowers.add(Material.PEONY);

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
        HotItems.add(Material.LANTERN);
        HotItems.add(Material.SOUL_LANTERN);

        Greenwell = new ConstructShape(new HashMap<Coordinate3D, Material>() {{
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

        Blitzard = new ConstructShape(new HashMap<Coordinate3D, Material>() {{
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

        Lighthouse = new ConstructShape(new HashMap<Coordinate3D, Material>() {{
            put(new Coordinate3D(0, 0, 0), Material.GLOWSTONE);
            put(new Coordinate3D(0, 1, 0), Material.STONE_BRICK_WALL);
            put(new Coordinate3D(0, 2, 0), Material.STONE_BRICK_WALL);
            put(new Coordinate3D(0, 3, 0), Material.STONE_BRICK_WALL);
            put(new Coordinate3D(0, 4, 0), Material.STONE_BRICK_WALL);
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
    }
}
