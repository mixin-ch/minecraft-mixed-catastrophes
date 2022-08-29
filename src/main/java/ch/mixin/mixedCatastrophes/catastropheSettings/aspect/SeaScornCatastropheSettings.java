package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class SeaScornCatastropheSettings {
    private boolean collectable;
    private boolean drowning;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection seaScornSection = superSection.getConfigurationSection("seaScorn");

        if (seaScornSection == null)
            return;

        collectable = seaScornSection.getBoolean("collectable");
        drowning = seaScornSection.getBoolean("drowning");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection seaScornSection = superSection.createSection("seaScorn");

        seaScornSection.set("collectable", collectable);
        seaScornSection.set("drowning", drowning);
    }

    public boolean isCollectable() {
        return collectable;
    }

    public void setCollectable(boolean collectable) {
        this.collectable = collectable;
    }

    public boolean isDrowning() {
        return drowning;
    }

    public void setDrowning(boolean drowning) {
        this.drowning = drowning;
    }
}
