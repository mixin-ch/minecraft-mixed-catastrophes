package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {
    private final MixedCatastrophesData mixedCatastrophesData;

    public InventoryListener(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!mixedCatastrophesData.getPlugin().PluginFlawless)
            return;

        Inventory inventory = event.getInventory();
        HelpInventoryManager helpInventoryManager = mixedCatastrophesData.getHelpInventoryManager();

        if (!helpInventoryManager.contains(inventory))
            return;

        helpInventoryManager.click(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!mixedCatastrophesData.getPlugin().PluginFlawless)
            return;

        Inventory inventory = event.getInventory();
        HelpInventoryManager helpInventoryManager = mixedCatastrophesData.getHelpInventoryManager();

        if (!helpInventoryManager.contains(inventory))
            return;

        helpInventoryManager.drag(event);
    }
}
