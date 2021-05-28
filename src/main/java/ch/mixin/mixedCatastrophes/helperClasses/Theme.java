package ch.mixin.mixedCatastrophes.helperClasses;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public class Theme {
    private final ChatColor color;
    private final Material material;

    public Theme(ChatColor color, Material material) {
        this.color = color;
        this.material = material;
    }

    public ChatColor getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }
}
