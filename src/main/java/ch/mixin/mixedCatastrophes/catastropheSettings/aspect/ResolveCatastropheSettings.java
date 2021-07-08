package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class ResolveCatastropheSettings {
    private boolean critAttack;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection resolveSection = superSection.getConfigurationSection("resolve");

        if (resolveSection == null)
            return;

        critAttack = resolveSection.getBoolean("critAttack");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection resolveSection = superSection.createSection("resolve");

        resolveSection.set("critAttack", critAttack);
    }

    public boolean isCritAttack() {
        return critAttack;
    }

    public void setCritAttack(boolean critAttack) {
        this.critAttack = critAttack;
    }
}
