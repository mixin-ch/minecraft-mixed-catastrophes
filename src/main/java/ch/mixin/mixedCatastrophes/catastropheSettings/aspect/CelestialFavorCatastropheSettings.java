package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class CelestialFavorCatastropheSettings {
    private boolean saveEssence;
    private boolean starMercy;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection celestialMercySection = superSection.getConfigurationSection("celestialMercy");

        if (celestialMercySection == null)
            return;

        saveEssence = celestialMercySection.getBoolean("saveEssence");
        starMercy = celestialMercySection.getBoolean("starMercy");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection celestialMercySection = superSection.createSection("celestialMercy");

        celestialMercySection.set("saveEssence", saveEssence);
        celestialMercySection.set("starMercy", starMercy);
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
