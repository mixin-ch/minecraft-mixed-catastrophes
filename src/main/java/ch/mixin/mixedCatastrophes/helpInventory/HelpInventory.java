package ch.mixin.mixedCatastrophes.helpInventory;

import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class HelpInventory {
    private final Inventory inventory;
    private final HashMap<Integer, HelpInventoryType> linkInventoryMap;

    public HelpInventory(Inventory inventory, HashMap<Integer, HelpInventoryType> linkInventoryMap) {
        this.inventory = inventory;
        this.linkInventoryMap = linkInventoryMap;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public HashMap<Integer, HelpInventoryType> getLinkInventoryMap() {
        return linkInventoryMap;
    }
}
