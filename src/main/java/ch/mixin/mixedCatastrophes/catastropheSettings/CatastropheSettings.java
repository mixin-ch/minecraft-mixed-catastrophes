package ch.mixin.mixedCatastrophes.catastropheSettings;

import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class CatastropheSettings {
    private double constructCheckPeriod;
    private boolean timeDistortion;
    private boolean starSplinter;
    private boolean preventNaturalIronGolem;
    private HashMap<WeatherCatastropheType, Boolean> weather = new HashMap<>();
    private AspectCatastropheSettings aspect = new AspectCatastropheSettings();

    public CatastropheSettings() {
        for (WeatherCatastropheType weatherType : WeatherCatastropheType.values()) {
            weather.put(weatherType, false);
        }
    }

    public void initialize(ConfigurationSection superSection) {
        if (superSection == null)
            return;

        ConfigurationSection interactionSection = superSection.getConfigurationSection("interaction");

        if (interactionSection == null)
            return;

        constructCheckPeriod = interactionSection.getDouble("constructCheckPeriod");
        timeDistortion = interactionSection.getBoolean("timeDistortion");
        starSplinter = interactionSection.getBoolean("starSplinter");
        preventNaturalIronGolem = interactionSection.getBoolean("preventNaturalIronGolem");

        ConfigurationSection weatherSection = interactionSection.getConfigurationSection("weather");

        if (weatherSection != null) {
            for (WeatherCatastropheType weatherType : WeatherCatastropheType.values()) {
                String weatherLabel = weatherType.toString();
                weatherLabel = weatherLabel.substring(0, 1).toLowerCase() + weatherLabel.substring(1);
                boolean active = weatherSection.getBoolean(weatherLabel);
                weather.put(weatherType, active);
            }
        }

        aspect.initialize(interactionSection);
    }

    public void fillConfig(ConfigurationSection superSection) {
        if (superSection == null)
            return;

        ConfigurationSection interactionSection = superSection.createSection("interaction");

        interactionSection.set("constructCheckPeriod", constructCheckPeriod);
        interactionSection.set("timeDistortion", timeDistortion);
        interactionSection.set("starSplinter", starSplinter);
        interactionSection.set("preventNaturalIronGolem", preventNaturalIronGolem);

        ConfigurationSection weatherSection = interactionSection.createSection("weather");

        for (WeatherCatastropheType weatherType : weather.keySet()) {
            String weatherLabel = weatherType.toString();
            weatherLabel = weatherLabel.substring(0, 1).toLowerCase() + weatherLabel.substring(1);
            weatherSection.set(weatherLabel, weather.get(weatherType));
        }

        aspect.fillConfig(interactionSection);
    }

    public double getConstructCheckPeriod() {
        return constructCheckPeriod;
    }

    public void setConstructCheckPeriod(double constructCheckPeriod) {
        this.constructCheckPeriod = constructCheckPeriod;
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

    public boolean isPreventNaturalIronGolem() {
        return preventNaturalIronGolem;
    }

    public void setPreventNaturalIronGolem(boolean preventNaturalIronGolem) {
        this.preventNaturalIronGolem = preventNaturalIronGolem;
    }
}
