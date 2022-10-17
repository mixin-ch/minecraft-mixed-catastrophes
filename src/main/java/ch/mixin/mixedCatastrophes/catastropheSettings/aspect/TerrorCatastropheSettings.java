package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import ch.mixin.mixedCatastrophes.catastropheSettings.aspect.terror.AssaultCatastropheSettings;
import ch.mixin.mixedCatastrophes.catastropheSettings.aspect.terror.WhispersCatastropheSettings;
import org.bukkit.configuration.ConfigurationSection;

public class TerrorCatastropheSettings {
    private WhispersCatastropheSettings whispers = new WhispersCatastropheSettings();
    private AssaultCatastropheSettings assault = new AssaultCatastropheSettings();
    private boolean paranoia;
    private boolean stalker;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection terrorSection = superSection.getConfigurationSection("terror");

        if (terrorSection == null)
            return;

        whispers.initialize(terrorSection);
        assault.initialize(terrorSection);
        paranoia = terrorSection.getBoolean("paranoia");
        stalker = terrorSection.getBoolean("stalker");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection terrorSection = superSection.createSection("terror");

        whispers.fillConfig(terrorSection);
        assault.fillConfig(terrorSection);
        terrorSection.set("paranoia", paranoia);
        terrorSection.set("stalker", stalker);
    }

    public WhispersCatastropheSettings getWhispers() {
        return whispers;
    }

    public void setWhispers(WhispersCatastropheSettings whispers) {
        this.whispers = whispers;
    }

    public AssaultCatastropheSettings getAssault() {
        return assault;
    }

    public void setAssault(AssaultCatastropheSettings assault) {
        this.assault = assault;
    }

    public boolean isParanoia() {
        return paranoia;
    }

    public void setParanoia(boolean paranoia) {
        this.paranoia = paranoia;
    }

    public boolean isStalker() {
        return stalker;
    }

    public void setStalker(boolean stalker) {
        this.stalker = stalker;
    }
}
