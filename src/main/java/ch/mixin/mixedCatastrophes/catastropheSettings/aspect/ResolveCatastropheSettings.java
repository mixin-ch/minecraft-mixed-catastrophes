package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class ResolveCatastropheSettings {
    private boolean virtue;
    private boolean critAttack;
    private boolean damageReduction;

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection resolveSection = superSection.getConfigurationSection("resolve");

        if (resolveSection == null)
            return;

        virtue = resolveSection.getBoolean("virtue");
        critAttack = resolveSection.getBoolean("critAttack");
        damageReduction = resolveSection.getBoolean("damageReduction");
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection resolveSection = superSection.createSection("resolve");

        resolveSection.set("virtue", virtue);
        resolveSection.set("critAttack", critAttack);
        resolveSection.set("damageReduction", damageReduction);
    }

    public boolean isVirtue() {
        return virtue;
    }

    public void setVirtue(boolean virtue) {
        this.virtue = virtue;
    }

    public boolean isCritAttack() {
        return critAttack;
    }

    public void setCritAttack(boolean critAttack) {
        this.critAttack = critAttack;
    }

    public boolean isDamageReduction() {
        return damageReduction;
    }

    public void setDamageReduction(boolean damageReduction) {
        this.damageReduction = damageReduction;
    }
}
