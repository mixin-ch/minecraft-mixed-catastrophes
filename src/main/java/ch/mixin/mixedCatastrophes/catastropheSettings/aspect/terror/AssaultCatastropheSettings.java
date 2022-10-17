package ch.mixin.mixedCatastrophes.catastropheSettings.aspect.terror;

import org.bukkit.configuration.ConfigurationSection;

public class AssaultCatastropheSettings {
    private boolean active;
    private double hallowedChance;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection assaultSection = superSection.getConfigurationSection("assault");

        if (assaultSection == null)
            return;

        active = assaultSection.getBoolean("active");
        hallowedChance = assaultSection.getDouble("hallowedChance");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection assaultSection = superSection.createSection("assault");

        assaultSection.set("active", active);
        assaultSection.set("hallowedChance", hallowedChance);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getHallowedChance() {
        return hallowedChance;
    }

    public void setHallowedChance(double hallowedChance) {
        this.hallowedChance = hallowedChance;
    }
}
