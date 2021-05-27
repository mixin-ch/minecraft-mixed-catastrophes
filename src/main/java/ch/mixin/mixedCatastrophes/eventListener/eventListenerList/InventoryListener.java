package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {
    protected final MixedCatastrophesPlugin plugin;

    public InventoryListener(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        HelpInventoryManager helpInventoryManager = plugin.getHelpInventoryManager();

        if (!helpInventoryManager.contains(inventory))
            return;

        helpInventoryManager.click(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        HelpInventoryManager helpInventoryManager = plugin.getHelpInventoryManager();

        if (!helpInventoryManager.contains(inventory))
            return;

        helpInventoryManager.drag(event);
    }
}
