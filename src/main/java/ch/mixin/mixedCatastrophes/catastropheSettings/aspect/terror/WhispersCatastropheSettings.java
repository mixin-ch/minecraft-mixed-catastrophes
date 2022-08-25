package ch.mixin.mixedCatastrophes.catastropheSettings.aspect.terror;

import org.bukkit.configuration.ConfigurationSection;

public class WhispersCatastropheSettings {
    private boolean active;
    private int  occurrenceIntervalMin;
    private int  occurrenceIntervalMax;
    private int  terrorGainMin;
    private int  terrorGainMax;
    private int  secretsGainFlat;
    private double  secretsGainTerrorMultiplier;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection whispersSection = superSection.getConfigurationSection("whispers");

        if (whispersSection == null)
            return;

        active = whispersSection.getBoolean("active");
        occurrenceIntervalMin = whispersSection.getInt("occurrenceIntervalMin");
        occurrenceIntervalMax = whispersSection.getInt("occurrenceIntervalMax");
        terrorGainMin = whispersSection.getInt("terrorGainMin");
        terrorGainMax = whispersSection.getInt("terrorGainMax");
        secretsGainFlat = whispersSection.getInt("secretsGainFlat");
        secretsGainTerrorMultiplier = whispersSection.getDouble("secretsGainTerrorMultiplier");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection whispersSection = superSection.createSection("whispers");

        whispersSection.set("active", active);
        whispersSection.set("occurrenceIntervalMin", occurrenceIntervalMin);
        whispersSection.set("occurrenceIntervalMax", occurrenceIntervalMax);
        whispersSection.set("terrorGainMin", terrorGainMin);
        whispersSection.set("terrorGainMax", terrorGainMax);
        whispersSection.set("secretsGainFlat", secretsGainFlat);
        whispersSection.set("secretsGainTerrorMultiplier", secretsGainTerrorMultiplier);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getOccurrenceIntervalMin() {
        return occurrenceIntervalMin;
    }

    public void setOccurrenceIntervalMin(int occurrenceIntervalMin) {
        this.occurrenceIntervalMin = occurrenceIntervalMin;
    }

    public int getOccurrenceIntervalMax() {
        return occurrenceIntervalMax;
    }

    public void setOccurrenceIntervalMax(int occurrenceIntervalMax) {
        this.occurrenceIntervalMax = occurrenceIntervalMax;
    }

    public int getTerrorGainMin() {
        return terrorGainMin;
    }

    public void setTerrorGainMin(int terrorGainMin) {
        this.terrorGainMin = terrorGainMin;
    }

    public int getTerrorGainMax() {
        return terrorGainMax;
    }

    public void setTerrorGainMax(int terrorGainMax) {
        this.terrorGainMax = terrorGainMax;
    }

    public int getSecretsGainFlat() {
        return secretsGainFlat;
    }

    public void setSecretsGainFlat(int secretsGainFlat) {
        this.secretsGainFlat = secretsGainFlat;
    }

    public double getSecretsGainTerrorMultiplier() {
        return secretsGainTerrorMultiplier;
    }

    public void setSecretsGainTerrorMultiplier(double secretsGainTerrorMultiplier) {
        this.secretsGainTerrorMultiplier = secretsGainTerrorMultiplier;
    }
}
