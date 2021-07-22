package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class SkyScornCatastropheSettings {
    private boolean collectable;
    private boolean tearFlesh;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection skyScornSection = superSection.getConfigurationSection("skyScorn");

        if (skyScornSection == null)
            return;

        collectable = skyScornSection.getBoolean("collectable");
        tearFlesh = skyScornSection.getBoolean("tearFlesh");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection skyScornSection = superSection.createSection("skyScorn");

        skyScornSection.set("collectable", collectable);
        skyScornSection.set("tearFlesh", tearFlesh);
    }

    public boolean isCollectable() {
        return collectable;
    }

    public void setCollectable(boolean collectable) {
        this.collectable = collectable;
    }

    public boolean isTearFlesh() {
        return tearFlesh;
    }

    public void setTearFlesh(boolean tearFlesh) {
        this.tearFlesh = tearFlesh;
    }
}
