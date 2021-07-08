package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class GreyhatDebtCatastropheSettings {
    private boolean seizeDream;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection greyhatDebtSection = superSection.getConfigurationSection("greyhatDebt");

        if (greyhatDebtSection == null)
            return;

        seizeDream = greyhatDebtSection.getBoolean("seizeDream");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection greyhatDebtSection = superSection.createSection("greyhatDebt");

        greyhatDebtSection.set("seizeDream", seizeDream);
    }

    public boolean isSeizeDream() {
        return seizeDream;
    }

    public void setSeizeDream(boolean seizeDream) {
        this.seizeDream = seizeDream;
    }
}
