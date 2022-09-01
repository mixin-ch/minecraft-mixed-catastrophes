package ch.mixin.mixedCatastrophes.catastropheSettings;

import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class CatastropheSettings {
    private int constructCheckPeriod;
    private boolean timeDistortion;
    private boolean starSplinter;
    private boolean preventNaturalIronGolem;
    private boolean radiantSkyMakesFire;
    private HashMap<WeatherCatastropheType, Boolean> weather = new HashMap<>();
    private HashMap<ConstructType, Boolean> construct = new HashMap<>();
    private AspectCatastropheSettings aspect = new AspectCatastropheSettings();

    public CatastropheSettings() {
        for (WeatherCatastropheType weatherType : WeatherCatastropheType.values()) {
            weather.put(weatherType, false);
        }

        for (ConstructType constructType : ConstructType.values()) {
            construct.put(constructType, false);
        }
    }

    public void initialize(ConfigurationSection superSection) {
        if (superSection == null)
            return;

        ConfigurationSection interactionSection = superSection.getConfigurationSection("interaction");

        if (interactionSection == null)
            return;

        constructCheckPeriod = interactionSection.getInt("constructCheckPeriod");
        timeDistortion = interactionSection.getBoolean("timeDistortion");
        starSplinter = interactionSection.getBoolean("starSplinter");
        preventNaturalIronGolem = interactionSection.getBoolean("preventNaturalIronGolem");
        radiantSkyMakesFire = interactionSection.getBoolean("radiantSkyMakesFire");

        ConfigurationSection weatherSection = interactionSection.getConfigurationSection("weather");

        if (weatherSection != null) {
            for (WeatherCatastropheType weatherType : WeatherCatastropheType.values()) {
                String weatherLabel = weatherType.toString();
                weatherLabel = weatherLabel.substring(0, 1).toLowerCase() + weatherLabel.substring(1);
                boolean active = weatherSection.getBoolean(weatherLabel);
                weather.put(weatherType, active);
            }
        }

        ConfigurationSection constructSection = interactionSection.getConfigurationSection("construct");

        if (constructSection != null) {
            for (ConstructType constructType : ConstructType.values()) {
                String constructLabel = constructType.toString();
                constructLabel = constructLabel.substring(0, 1).toLowerCase() + constructLabel.substring(1);
                boolean active = constructSection.getBoolean(constructLabel);
                construct.put(constructType, active);
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
        interactionSection.set("radiantSkyMakesFire", radiantSkyMakesFire);

        ConfigurationSection weatherSection = interactionSection.createSection("weather");

        for (WeatherCatastropheType weatherType : weather.keySet()) {
            String weatherLabel = weatherType.toString();
            weatherLabel = weatherLabel.substring(0, 1).toLowerCase() + weatherLabel.substring(1);
            weatherSection.set(weatherLabel, weather.get(weatherType));
        }

        ConfigurationSection constructSection = interactionSection.createSection("construct");

        for (ConstructType constructType : construct.keySet()) {
            String constructLabel = constructType.toString();
            constructLabel = constructLabel.substring(0, 1).toLowerCase() + constructLabel.substring(1);
            constructSection.set(constructLabel, construct.get(constructType));
        }

        aspect.fillConfig(interactionSection);
    }

    public int getConstructCheckPeriod() {
        return constructCheckPeriod;
    }

    public void setConstructCheckPeriod(int constructCheckPeriod) {
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

    public HashMap<ConstructType, Boolean> getConstruct() {
        return construct;
    }

    public void setConstruct(HashMap<ConstructType, Boolean> construct) {
        this.construct = construct;
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

    public boolean isRadiantSkyMakesFire() {
        return radiantSkyMakesFire;
    }

    public void setRadiantSkyMakesFire(boolean radiantSkyMakesFire) {
        this.radiantSkyMakesFire = radiantSkyMakesFire;
    }
}
