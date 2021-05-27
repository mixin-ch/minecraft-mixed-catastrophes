package ch.mixin.mixedCatastrophes.catastropheManager;

import ch.mixin.mixedCatastrophes.metaData.MetaData;
import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.timeDistortion.TimeDistortionManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.PersonalCatastropheManager;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;

public class RootCatastropheManager {
    private final MixedCatastrophesPlugin plugin;
    private final MetaData metaData;

    private boolean started;

    private final TimeDistortionManager timeDistortionManager;
    private final WeatherCatastropheManager weatherCatastropheManager;
    private final StarSplinterCatastropheManager starSplinterCatastropheManager;
    private final ConstructManager constructManager;
    private final PersonalCatastropheManager personalCatastropheManager;

    private final int metaDataSaveDuration = 5 * 60;
    private int metaDataSaveTimer;

    public RootCatastropheManager(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
        metaData = plugin.getMetaData();
        started = false;
        timeDistortionManager = new TimeDistortionManager(plugin, this);
        weatherCatastropheManager = new WeatherCatastropheManager(plugin, this);
        starSplinterCatastropheManager = new StarSplinterCatastropheManager(plugin, this);
        constructManager = new ConstructManager(plugin, this);
        personalCatastropheManager = new PersonalCatastropheManager(plugin, this);
        metaDataSaveTimer = 0;
        initializeMetaData();
        plugin.getEventChangeManager().updateScoreBoard();
        initializeCauser();
    }

    private void initializeMetaData() {
        timeDistortionManager.initializeMetaData();
        weatherCatastropheManager.initializeMetaData();
        starSplinterCatastropheManager.initializeMetaData();
        constructManager.initializeMetaData();
        personalCatastropheManager.initializeMetaData();
    }

    private void initializeCauser() {
        timeDistortionManager.initializeCauser();
        weatherCatastropheManager.initializeCauser();
        starSplinterCatastropheManager.initializeCauser();
        constructManager.initializeCauser();
        personalCatastropheManager.initializeCauser();
    }

    public void start() {
        if (!started) {
            started = true;
            System.out.println("Catastrophes started.");
            tickTrigger();
            metaData.setActive(true);
        }
    }

    public void stop() {
        started = false;
        System.out.println("Catastrophes stopped.");
        metaData.setActive(false);
        metaData.save();
    }

    private void tickTrigger() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this::tick
                , 20);
    }

    private void tick() {
        if (!started)
            return;

        timeDistortionManager.tick();
        weatherCatastropheManager.tick();
        starSplinterCatastropheManager.tick();
        constructManager.tick();
        personalCatastropheManager.tick();
        plugin.getEventChangeManager().updateScoreBoard();

        save();
        tickTrigger();
    }

    private void save() {
        timeDistortionManager.updateMetaData();
        weatherCatastropheManager.updateMetaData();
        starSplinterCatastropheManager.updateMetaData();
        constructManager.updateMetaData();
        personalCatastropheManager.updateMetaData();

        if (metaDataSaveTimer <= 0) {
            metaDataSaveTimer = metaDataSaveDuration;
            metaData.save();
        }

        metaDataSaveTimer--;
    }

    public TimeDistortionManager getTimeDistortionManager() {
        return timeDistortionManager;
    }

    public WeatherCatastropheManager getWeatherCatastropheManager() {
        return weatherCatastropheManager;
    }

    public StarSplinterCatastropheManager getStarSplinterCatastropheManager() {
        return starSplinterCatastropheManager;
    }

    public ConstructManager getGreenWellManager() {
        return constructManager;
    }

    public PersonalCatastropheManager getPersonalCatastropheManager() {
        return personalCatastropheManager;
    }
}