package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class GreyhatDebtCatastropheSettings {
    private boolean seizeDream;

    public GreyhatDebtCatastropheSettings() {
    }

    public GreyhatDebtCatastropheSettings(ConfigurationSection configuration) {
        initialize(configuration);
    }

    public void initialize(ConfigurationSection configuration) {
        if (configuration == null)
            return;

        seizeDream = configuration.getBoolean("seizeDream");
    }

    public boolean isSeizeDream() {
        return seizeDream;
    }

    public void setSeizeDream(boolean seizeDream) {
        this.seizeDream = seizeDream;
    }
}
