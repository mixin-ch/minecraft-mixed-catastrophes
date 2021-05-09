package ch.mixin.main;

import ch.mixin.eventChange.EventChangeManager;
import ch.mixin.helpInventory.HelpInventoryManager;
import ch.mixin.helperClasses.Particler;
import com.google.gson.Gson;
import ch.mixin.MetaData.MetaData;
import ch.mixin.catastropheManager.RootCatastropheManager;
import ch.mixin.command.CommandInitializer;
import ch.mixin.eventListener.EventListenerInitializer;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Scanner;

public final class MixedCatastrophesPlugin extends JavaPlugin {
    public static MixedCatastrophesPlugin PLUGIN;
    public static String PLUGIN_NAME;
    public static String ROOT_DIRECTORY_PATH;
    public static String METADATA_DIRECTORY_PATH;
    public static String METADATA_FILE_PATH;
    public static File METADATA_FILE;

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
    private MetaData metaData;
    private ArrayList<World> affectedWorlds;
    private EventChangeManager eventChangeManager;
    private RootCatastropheManager rootCatastropheManager;
    private HelpInventoryManager helpInventoryManager;
    private Particler particler;

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
        metaData.save();
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void initialize() {
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
        if (jsonString.equals("")) {
            metaData = new MetaData();
        } else {
            metaData = new Gson().fromJson(jsonString, MetaData.class);
        }

        eventChangeManager = new EventChangeManager(this);
        rootCatastropheManager = new RootCatastropheManager(this);
        helpInventoryManager = new HelpInventoryManager(this);
        particler = new Particler(this);
        affectedWorlds = new ArrayList<>();

        for (String worldName : getConfig().getStringList("worlds")) {
            World world = getServer().getWorld(worldName);
            if (world != null) {
                affectedWorlds.add(world);
            }
        }

        CommandInitializer.setupCommands(this);
        EventListenerInitializer.setupEventListener(this);
    }

    private void start() {
        if (metaData.isActive()){
            rootCatastropheManager.start();
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

    public MetaData getMetaData() {
        return metaData;
    }

    public ArrayList<World> getAffectedWorlds() {
        return affectedWorlds;
    }

    public EventChangeManager getEventChangeManager() {
        return eventChangeManager;
    }

    public RootCatastropheManager getRootCatastropheManager() {
        return rootCatastropheManager;
    }

    public HelpInventoryManager getHelpInventoryManager() {
        return helpInventoryManager;
    }

    public Particler getParticler() {
        return particler;
    }
}
