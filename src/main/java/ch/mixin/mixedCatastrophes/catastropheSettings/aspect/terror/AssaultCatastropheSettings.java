package ch.mixin.mixedCatastrophes.catastropheSettings.aspect.terror;

import org.bukkit.configuration.ConfigurationSection;

public class AssaultCatastropheSettings {
    private boolean active;
    private double pumpkinHeadChance;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection assaultSection = superSection.getConfigurationSection("assault");

        if (assaultSection == null)
            return;

        active = assaultSection.getBoolean("active");
        pumpkinHeadChance = assaultSection.getInt("pumpkinHeadChance");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection assaultSection = superSection.createSection("assault");

        assaultSection.set("active", active);
        assaultSection.set("pumpkinHeadChance", pumpkinHeadChance);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getPumpkinHeadChance() {
        return pumpkinHeadChance;
    }

    public void setPumpkinHeadChance(double pumpkinHeadChance) {
        this.pumpkinHeadChance = pumpkinHeadChance;
    }
}
