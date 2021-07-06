package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class NobilityCatastropheSettings {
    private boolean critAttack;

    public void initialize(ConfigurationSection configuration) {
        critAttack = configuration.getBoolean("critAttack");
    }

    public boolean isCritAttack() {
        return critAttack;
    }

    public void setCritAttack(boolean critAttack) {
        this.critAttack = critAttack;
    }
}
