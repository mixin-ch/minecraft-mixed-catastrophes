package ch.mixin.mixedCatastrophes.eventListener;

import ch.mixin.mixedCatastrophes.eventListener.eventListenerList.*;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;

public class EventListenerInitializer {
    public static void setupEventListener(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        MixedCatastrophesPlugin plugin = mixedCatastrophesManagerAccessor.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(new StatusListener(mixedCatastrophesManagerAccessor), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MisdeedListener(mixedCatastrophesManagerAccessor), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ConsequenceListener(mixedCatastrophesManagerAccessor), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ActionListener(mixedCatastrophesManagerAccessor), plugin);
        plugin.getServer().getPluginManager().registerEvents(new NeedListener(mixedCatastrophesManagerAccessor), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(mixedCatastrophesManagerAccessor), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ConstructListener(mixedCatastrophesManagerAccessor), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MixedAchievementListener(mixedCatastrophesManagerAccessor), plugin);
    }
}
