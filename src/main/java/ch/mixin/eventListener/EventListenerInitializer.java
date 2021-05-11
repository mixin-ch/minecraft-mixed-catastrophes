package ch.mixin.eventListener;

import ch.mixin.eventListener.eventListenerList.*;
import ch.mixin.main.MixedCatastrophesPlugin;

public class EventListenerInitializer {
    public static void setupEventListener(MixedCatastrophesPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new StatusListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MisdeedListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ConsequenceListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ActionListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new NeedListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ConstructListener(plugin), plugin);
    }
}
