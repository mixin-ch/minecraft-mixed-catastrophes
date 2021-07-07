package ch.mixin.mixedCatastrophes.main;

import ch.mixin.mixedAchievements.main.MixedAchievementsPlugin;
import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheSettings.CatastropheSettings;
import ch.mixin.mixedCatastrophes.command.CommandInitializer;
import ch.mixin.mixedCatastrophes.eventChange.EventChangeManager;
import ch.mixin.mixedCatastrophes.eventListener.EventListenerInitializer;
import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.helperClasses.Particler;
import ch.mixin.mixedCatastrophes.metaData.MetaData;
import ch.mixin.mixedCatastrophes.mixedAchievements.MixedAchievementsManager;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class MixedCatastrophesPlugin extends JavaPlugin {
    public static MixedCatastrophesPlugin PLUGIN;
    public static String PLUGIN_NAME;
    public static String ROOT_DIRECTORY_PATH;
    public static String METADATA_DIRECTORY_PATH;
    public static String METADATA_FILE_PATH;
    public static File METADATA_FILE;

    public static MixedAchievementsPlugin MixedAchievementsPlugin;
    public static boolean UseMixedAchievementsPlugin;
    public static boolean SetupMixedAchievementsPlugin;

    static {
        String urlPath = MixedCatastrophesPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = null;

        try {
            decodedPath = URLDecoder.decode(urlPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ROOT_DIRECTORY_PATH = decodedPath.substring(0, decodedPath.lastIndexOf("/"));
    }

    public boolean PluginFlawless;
    MixedCatastrophesData mixedCatastrophesData;

    @Override
    public void onEnable() {
        PLUGIN = this;
        PLUGIN_NAME = getDescription().getName();
        System.out.println(PLUGIN_NAME + " enabled");
        setup();
        load();
        start();
    }

    @Override
    public void onDisable() {
        mixedCatastrophesData.getMetaData().save();
        System.out.println(PLUGIN_NAME + " disabled");
    }

    private void setup() {
        mixedCatastrophesData = new MixedCatastrophesData(this);

        getConfig().options().copyDefaults(true);
        saveConfig();
        setupMetaData();

        mixedCatastrophesData.setAffectedWorlds(new ArrayList<>());
        mixedCatastrophesData.setCatastropheSettings(new CatastropheSettings(getConfig()));

        mixedCatastrophesData.setEventChangeManager(new EventChangeManager(mixedCatastrophesData));
        mixedCatastrophesData.setRootCatastropheManager(new RootCatastropheManager(mixedCatastrophesData));
        mixedCatastrophesData.setHelpInventoryManager(new HelpInventoryManager(this));
        mixedCatastrophesData.setParticler(new Particler(this));

        CommandInitializer.setupCommands(mixedCatastrophesData);
        EventListenerInitializer.setupEventListener(mixedCatastrophesData);

        mixedCatastrophesData.getMetaData().save();
    }

    public void reload() {
        mixedCatastrophesData.getMetaData().save();
        load();
    }

    private void load() {
        super.reloadConfig();
        loadMetaData();
        loadDependentPlugins();

        mixedCatastrophesData.getCatastropheSettings().initialize(getConfig());

        List<World> affectedWorlds = new ArrayList<>();

        for (String worldName : getConfig().getStringList("worlds")) {
            World world = getServer().getWorld(worldName);
            if (world != null) {
                affectedWorlds.add(world);
            }
        }

        mixedCatastrophesData.setAffectedWorlds(affectedWorlds);
    }

    private void setupMetaData() {
        METADATA_DIRECTORY_PATH = ROOT_DIRECTORY_PATH + "/" + PLUGIN_NAME;
        final File folder = new File(METADATA_DIRECTORY_PATH);
        if (!folder.exists() && !folder.mkdirs())
            throw new RuntimeException("Failed to create Metadata Directory.");

        METADATA_FILE_PATH = METADATA_DIRECTORY_PATH + "/Metadata.txt";
        METADATA_FILE = new File(METADATA_FILE_PATH);
        if (!METADATA_FILE.exists()) {
            try {
                METADATA_FILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadMetaData();
    }

    private void loadMetaData() {
        String jsonString = String.join("\n", readFile(METADATA_FILE));
        MetaData metaData;
        if (jsonString.equals("")) {
            metaData = new MetaData();
        } else {
            metaData = new Gson().fromJson(jsonString, MetaData.class);
        }

        mixedCatastrophesData.setMetaData(metaData);
    }

    private void loadDependentPlugins() {
        MixedAchievementsPlugin = (MixedAchievementsPlugin) Bukkit.getServer().getPluginManager().getPlugin("MixedAchievements");
        UseMixedAchievementsPlugin = MixedAchievementsPlugin != null;
        System.out.println("MixedAchievementsPlugin: " + UseMixedAchievementsPlugin);
        mixedCatastrophesData.setMixedAchievementsManager(new MixedAchievementsManager());
    }

    private void start() {
        PluginFlawless = true;
        mixedCatastrophesData.setFullyFunctional(mixedCatastrophesData.getMetaData().isActive());

        tick();
    }

    private void tickTrigger() {
        getServer().getScheduler().scheduleSyncDelayedTask(this, this::tick
                , 20);
    }

    private void tick() {
        if (UseMixedAchievementsPlugin && MixedAchievementsPlugin.isActive() && !SetupMixedAchievementsPlugin) {
            SetupMixedAchievementsPlugin = true;
            mixedCatastrophesData.getMixedAchievementsManager().initializeAchievements();
            mixedCatastrophesData.getEventChangeManager().updateAchievementProgress();
        }

        if (mixedCatastrophesData.getMetaData().isActive()) {
            mixedCatastrophesData.getRootCatastropheManager().tick();
        }

        mixedCatastrophesData.getEventChangeManager().updateScoreBoard();

        tickTrigger();
    }

    public static ArrayList<String> readFile(File file) {
        ArrayList<String> text = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                text.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static void writeFile(File file, String text) {
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
