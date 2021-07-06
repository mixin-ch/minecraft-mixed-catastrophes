package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class GreyhatDebtCatastropheSettings {
    private boolean seizeDream;

    public void initialize(ConfigurationSection configuration) {
        seizeDream = configuration.getBoolean("seizeDream");
    }

    public boolean isSeizeDream() {
        return seizeDream;
    }

    public void setSeizeDream(boolean seizeDream) {
        this.seizeDream = seizeDream;
    }
}
