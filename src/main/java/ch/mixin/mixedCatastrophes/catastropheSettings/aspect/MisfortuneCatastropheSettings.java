package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class MisfortuneCatastropheSettings {
    private boolean collectable;
    private boolean missAttack;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection misfortuneSection = superSection.getConfigurationSection("misfortune");

        if (misfortuneSection == null)
            return;

        collectable = misfortuneSection.getBoolean("collectable");
        missAttack = misfortuneSection.getBoolean("missAttack");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection misfortuneSection = superSection.createSection("misfortune");

        misfortuneSection.set("collectable", collectable);
        misfortuneSection.set("missAttack", missAttack);
    }

    public boolean isCollectable() {
        return collectable;
    }

    public void setCollectable(boolean collectable) {
        this.collectable = collectable;
    }

    public boolean isMissAttack() {
        return missAttack;
    }

    public void setMissAttack(boolean missAttack) {
        this.missAttack = missAttack;
    }
}
