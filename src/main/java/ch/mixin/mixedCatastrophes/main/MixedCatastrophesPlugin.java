package ch.mixin.mixedCatastrophes.main;

import ch.mixin.islandgenerator.main.IslandGeneratorPlugin;
import ch.mixin.mixedAchievements.main.MixedAchievementsPlugin;
import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
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

    public static IslandGeneratorPlugin IslandGeneratorPlugin;
    public static boolean UseIslandGeneratorPlugin;
    public static MixedAchievementsPlugin MixedAchievementsPlugin;
    public static boolean UseMixedAchievementsPlugin;

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

    public boolean pluginFlawless;
    MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor;

    @Override
    public void onEnable() {
        PLUGIN = this;
        PLUGIN_NAME = getDescription().getName();
        System.out.println(PLUGIN_NAME + " enabled");
        loadConfig();
        initialize();
        start();
    }

    @Override
    public void onDisable() {
        System.out.println(PLUGIN_NAME + " disabled");
        mixedCatastrophesManagerAccessor.getMetaData().save();
    }

    public void reload() {
        super.reloadConfig();
        initialize();
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void initialize() {
        mixedCatastrophesManagerAccessor = new MixedCatastrophesManagerAccessor(this);
        initializeMetaData();
        initializeDependentPlugins();
        mixedCatastrophesManagerAccessor.setEventChangeManager(new EventChangeManager(mixedCatastrophesManagerAccessor));
        mixedCatastrophesManagerAccessor.setRootCatastropheManager(new RootCatastropheManager(mixedCatastrophesManagerAccessor));
        mixedCatastrophesManagerAccessor.setHelpInventoryManager(new HelpInventoryManager(this));
        mixedCatastrophesManagerAccessor.setParticler(new Particler(this));
        List<World> affectedWorlds = new ArrayList<>();
        mixedCatastrophesManagerAccessor.setAffectedWorlds(affectedWorlds);

        for (String worldName : getConfig().getStringList("worlds")) {
            World world = getServer().getWorld(worldName);
            if (world != null) {
                affectedWorlds.add(world);
            }
        }

        CommandInitializer.setupCommands(mixedCatastrophesManagerAccessor);
        EventListenerInitializer.setupEventListener(mixedCatastrophesManagerAccessor);
    }

    private void initializeMetaData() {
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

        String jsonString = String.join("\n", readFile(METADATA_FILE));
        MetaData metaData;
        if (jsonString.equals("")) {
            metaData = new MetaData();
        } else {
            metaData = new Gson().fromJson(jsonString, MetaData.class);
        }

        mixedCatastrophesManagerAccessor.setMetaData(metaData);
    }

    private void initializeDependentPlugins() {
        IslandGeneratorPlugin = (IslandGeneratorPlugin) Bukkit.getServer().getPluginManager().getPlugin("IslandGenerator");
        UseIslandGeneratorPlugin = IslandGeneratorPlugin != null;
        System.out.println("IslandGeneratorPlugin: " + UseIslandGeneratorPlugin);

        MixedAchievementsPlugin = (MixedAchievementsPlugin) Bukkit.getServer().getPluginManager().getPlugin("MixedAchievements");
        UseMixedAchievementsPlugin = MixedAchievementsPlugin != null;
        System.out.println("MixedAchievementsPlugin: " + UseMixedAchievementsPlugin);
        mixedCatastrophesManagerAccessor.setMixedAchievementsManager(new MixedAchievementsManager());

        if (UseMixedAchievementsPlugin) {
            getServer().getScheduler().scheduleSyncDelayedTask(this
                    , this::tickMixedAchievementsPlugin
                    , 20);
        }

    }

    private void tickMixedAchievementsPlugin() {
        if (MixedAchievementsPlugin.isActive()) {
            mixedCatastrophesManagerAccessor.getMixedAchievementsManager().initializeAchievements();
            mixedCatastrophesManagerAccessor.getEventChangeManager().updateAchievementProgress();
        } else {
            getServer().getScheduler().scheduleSyncDelayedTask(this
                    , this::tickMixedAchievementsPlugin
                    , 20);
        }
    }

    private void start() {
        if (mixedCatastrophesManagerAccessor.getMetaData().isActive()) {
            mixedCatastrophesManagerAccessor.getRootCatastropheManager().start();
        }

        pluginFlawless = true;
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

    public boolean isPluginFlawless() {
        return pluginFlawless;
    }
}
