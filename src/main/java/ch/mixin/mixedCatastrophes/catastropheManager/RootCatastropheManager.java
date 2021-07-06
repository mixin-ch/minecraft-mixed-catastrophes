package ch.mixin.mixedCatastrophes.catastropheManager;

import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.timeDistortion.TimeDistortionManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.PersonalCatastropheManager;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.MetaData;

public class RootCatastropheManager {
    private final MixedCatastrophesData mixedCatastrophesData;
    private final MixedCatastrophesPlugin plugin;
    private final MetaData metaData;

    private final TimeDistortionManager timeDistortionManager;
    private final WeatherCatastropheManager weatherCatastropheManager;
    private final StarSplinterCatastropheManager starSplinterCatastropheManager;
    private final ConstructManager constructManager;
    private final PersonalCatastropheManager personalCatastropheManager;

    private final int metaDataSaveDuration = 5 * 60;
    private int metaDataSaveTimer;

    public RootCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
        plugin = mixedCatastrophesData.getPlugin();
        metaData = mixedCatastrophesData.getMetaData();
        timeDistortionManager = new TimeDistortionManager(mixedCatastrophesData);
        weatherCatastropheManager = new WeatherCatastropheManager(mixedCatastrophesData);
        starSplinterCatastropheManager = new StarSplinterCatastropheManager(mixedCatastrophesData);
        constructManager = new ConstructManager(mixedCatastrophesData);
        personalCatastropheManager = new PersonalCatastropheManager(mixedCatastrophesData);
        metaDataSaveTimer = 0;
        initializeMetaData();
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

    public void tick() {
        timeDistortionManager.tick();
        weatherCatastropheManager.tick();
        starSplinterCatastropheManager.tick();
        constructManager.tick();
        personalCatastropheManager.tick();

        update();
    }

    private void update() {
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

    public ConstructManager getConstructManager() {
        return constructManager;
    }

    public PersonalCatastropheManager getPersonalCatastropheManager() {
        return personalCatastropheManager;
    }
}
