package ch.mixin.catastropheManager.global.starSplinter;

import org.bukkit.Material;

public class StarSplinterPremise {
    private final String name;
    private final Material material;
    private final double multiplier;

    public StarSplinterPremise(String name, Material material, double multiplier) {
        this.name = name;
        this.material = material;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
