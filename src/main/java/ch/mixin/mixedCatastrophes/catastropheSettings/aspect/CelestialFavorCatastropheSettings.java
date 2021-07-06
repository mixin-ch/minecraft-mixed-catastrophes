package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class CelestialFavorCatastropheSettings {
    private boolean saveEssence;
    private boolean starMercy;

    public CelestialFavorCatastropheSettings() {
    }

    public CelestialFavorCatastropheSettings(ConfigurationSection configuration) {
        initialize(configuration);
    }

    public void initialize(ConfigurationSection configuration) {
        if (configuration == null)
            return;

        saveEssence = configuration.getBoolean("saveEssence");
        starMercy = configuration.getBoolean("starMercy");
    }

    public boolean isSaveEssence() {
        return saveEssence;
    }

    public void setSaveEssence(boolean saveEssence) {
        this.saveEssence = saveEssence;
    }

    public boolean isStarMercy() {
        return starMercy;
    }

    public void setStarMercy(boolean starMercy) {
        this.starMercy = starMercy;
    }
}
