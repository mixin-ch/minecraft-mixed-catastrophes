package ch.mixin.mixedCatastrophes.eventListener;

import ch.mixin.mixedCatastrophes.eventListener.eventListenerList.*;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;

public class EventListenerInitializer {
    public static void setupEventListener(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        MixedCatastrophesPlugin plugin = mixedCatastrophesManagerAccessor.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(new StatusListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MisdeedListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ConsequenceListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ActionListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new NeedListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ConstructListener(mixedCatastrophesManagerAccessor), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MixedAchievementListener(plugin), plugin);
    }
}
