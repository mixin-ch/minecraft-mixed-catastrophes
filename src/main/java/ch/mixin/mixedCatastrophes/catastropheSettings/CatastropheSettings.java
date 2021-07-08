package ch.mixin.mixedCatastrophes.catastropheSettings;

import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class CatastropheSettings {
    private boolean timeDistortion;
    private HashMap<WeatherCatastropheType, Boolean> weather = new HashMap<>();
    private boolean starSplinter;
    private AspectCatastropheSettings aspect = new AspectCatastropheSettings();

    public CatastropheSettings() {
        for (WeatherCatastropheType weatherType : WeatherCatastropheType.values()) {
            weather.put(weatherType, false);
        }
    }

    public void initialize(ConfigurationSection configuration) {
        ConfigurationSection interactionSection = configuration.getConfigurationSection("interaction");

        if (interactionSection == null)
            return;

        timeDistortion = interactionSection.getBoolean("timeDistortion");
        starSplinter = interactionSection.getBoolean("starSplinter");

        ConfigurationSection weatherSection = interactionSection.getConfigurationSection("weather");

        if (weatherSection != null) {
            for (WeatherCatastropheType weatherType : WeatherCatastropheType.values()) {
                boolean active = false;

                if (weatherSection != null) {
                    String weatherLabel = weatherType.toString();
                    weatherLabel = weatherLabel.substring(0,1).toLowerCase() + weatherLabel.substring(1);
                    active = weatherSection.getBoolean(weatherLabel);
                }

                weather.put(weatherType, active);
            }
        }

        aspect.initialize(interactionSection.getConfigurationSection("aspect"));
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
