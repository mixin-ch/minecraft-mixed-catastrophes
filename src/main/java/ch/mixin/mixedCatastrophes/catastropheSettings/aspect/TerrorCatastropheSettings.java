package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class TerrorCatastropheSettings {
    private boolean whispers;
    private boolean assault;
    private boolean paranoia;
    private boolean stalker;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection terrorSection = superSection.getConfigurationSection("terror");

        if (terrorSection == null)
            return;

        whispers = terrorSection.getBoolean("whispers");
        assault = terrorSection.getBoolean("assault");
        paranoia = terrorSection.getBoolean("paranoia");
        stalker = terrorSection.getBoolean("stalker");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection terrorSection = superSection.createSection("terror");

        terrorSection.set("whispers", whispers);
        terrorSection.set("assault", assault);
        terrorSection.set("paranoia", paranoia);
        terrorSection.set("stalker", stalker);
    }

    public boolean isWhispers() {
        return whispers;
    }

    public void setWhispers(boolean whispers) {
        this.whispers = whispers;
    }

    public boolean isAssault() {
        return assault;
    }

    public void setAssault(boolean assault) {
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
