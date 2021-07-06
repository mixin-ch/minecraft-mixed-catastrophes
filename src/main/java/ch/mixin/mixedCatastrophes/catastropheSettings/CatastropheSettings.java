package ch.mixin.mixedCatastrophes.catastropheSettings;

import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class CatastropheSettings {
    private boolean timeDistortion;
    private HashMap<WeatherCatastropheType, Boolean> weather = new HashMap<>();
    private boolean starSplinter;
    private AspectCatastropheSettings aspect = new AspectCatastropheSettings();

    public CatastropheSettings(ConfigurationSection configuration) {
        initialize(configuration);
    }

    public void initialize(ConfigurationSection configuration) {
        ConfigurationSection interactionSection = configuration.getConfigurationSection("interaction");

        if (interactionSection == null)
            return;

        timeDistortion = interactionSection.getBoolean("timeDistortion");
        starSplinter = interactionSection.getBoolean("starSplinter");

        ConfigurationSection weatherSection = configuration.getConfigurationSection("weather");

        for (WeatherCatastropheType weatherType : WeatherCatastropheType.values()) {
            boolean active = false;

            if (weatherSection != null) {
                active = weatherSection.getBoolean(weatherType.toString());
            }

            weather.put(weatherType, active);
        }

        ConfigurationSection aspectSection = configuration.getConfigurationSection("aspect");

        if (aspectSection != null) {
            aspect.initialize(aspectSection);
        }
    }

    public boolean isTimeDistortion() {
        return timeDistortion;
    }

    public void setTimeDistortion(boolean timeDistortion) {
        this.timeDistortion = timeDistortion;
    }

    public HashMap<WeatherCatastropheType, Boolean> getWeather() {
        return weather;
    }

    public void setWeather(HashMap<WeatherCatastropheType, Boolean> weather) {
        this.weather = weather;
    }

    public boolean isStarSplinter() {
        return starSplinter;
    }

    public void setStarSplinter(boolean starSplinter) {
        this.starSplinter = starSplinter;
    }

    public AspectCatastropheSettings getAspect() {
        return aspect;
    }

    public void setAspect(AspectCatastropheSettings aspect) {
        this.aspect = aspect;
    }
}
