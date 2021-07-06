package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class TerrorCatastropheSettings {
    private boolean whispers;
    private boolean assault;
    private boolean paranoia;
    private boolean stalker;

    public void initialize(ConfigurationSection configuration) {
        whispers = configuration.getBoolean("whispers");
        assault = configuration.getBoolean("assault");
        paranoia = configuration.getBoolean("paranoia");
        stalker = configuration.getBoolean("stalker");
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
