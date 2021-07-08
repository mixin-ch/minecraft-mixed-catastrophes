package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class NatureConspiracyCatastropheSettings {
    private boolean collectable;
    private boolean ravenousFood;
    private boolean theHorde;

    public void initialize(ConfigurationSection configuration) {
        if (configuration == null)
            return;

        collectable = configuration.getBoolean("collectable");
        ravenousFood = configuration.getBoolean("ravenousFood");
        theHorde = configuration.getBoolean("theHorde");
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
