package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class NatureConspiracyCatastropheSettings {
    private boolean collectable;
    private boolean ravenousFood;
    private boolean theHorde;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection natureConspiracySection = superSection.getConfigurationSection("natureConspiracy");

        if (natureConspiracySection == null)
            return;

        collectable = natureConspiracySection.getBoolean("collectable");
        ravenousFood = natureConspiracySection.getBoolean("ravenousFood");
        theHorde = natureConspiracySection.getBoolean("theHorde");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection natureConspiracySection = superSection.createSection("natureConspiracy");

        natureConspiracySection.set("collectable", collectable);
        natureConspiracySection.set("ravenousFood", ravenousFood);
        natureConspiracySection.set("theHorde", theHorde);
    }

    public boolean isCollectable() {
        return collectable;
    }

    public void setCollectable(boolean collectable) {
        this.collectable = collectable;
    }

    public boolean isRavenousFood() {
        return ravenousFood;
    }

    public void setRavenousFood(boolean ravenousFood) {
        this.ravenousFood = ravenousFood;
    }

    public boolean isTheHorde() {
        return theHorde;
    }

    public void setTheHorde(boolean theHorde) {
        this.theHorde = theHorde;
    }
}
