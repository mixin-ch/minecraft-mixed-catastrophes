package ch.mixin.mixedCatastrophes.catastropheSettings.aspect;

import org.bukkit.configuration.ConfigurationSection;

public class MisfortuneCatastropheSettings {
    private boolean collectable;
    private boolean missAttack;

    public void initialize(ConfigurationSection configuration) {
        collectable = configuration.getBoolean("collectable");
        missAttack = configuration.getBoolean("missAttack");
    }

    public boolean isCollectable() {
        return collectable;
    }

    public void setCollectable(boolean collectable) {
        this.collectable = collectable;
    }

    public boolean isMissAttack() {
        return missAttack;
    }

    public void setMissAttack(boolean missAttack) {
        this.missAttack = missAttack;
    }
}
