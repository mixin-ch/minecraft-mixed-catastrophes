package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class ResolveCatastropheSettings {
    private boolean critAttack;

    public void initialize(ConfigurationSection configuration) {
        if (configuration == null)
            return;

        critAttack = configuration.getBoolean("critAttack");
    }

    public boolean isCritAttack() {
        return critAttack;
    }

    public void setCritAttack(boolean critAttack) {
        this.critAttack = critAttack;
    }
}
