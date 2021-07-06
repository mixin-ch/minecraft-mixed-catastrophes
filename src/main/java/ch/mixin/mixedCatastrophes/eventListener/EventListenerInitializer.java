package ch.mixin.mixedCatastrophes.eventListener;

import ch.mixin.mixedCatastrophes.eventListener.eventListenerList.*;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;

public class EventListenerInitializer {
    public static void setupEventListener(MixedCatastrophesData mixedCatastrophesData) {
        MixedCatastrophesPlugin plugin = mixedCatastrophesData.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(new StatusListener(mixedCatastrophesData), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MisdeedListener(mixedCatastrophesData), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ConsequenceListener(mixedCatastrophesData), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ActionListener(mixedCatastrophesData), plugin);
        plugin.getServer().getPluginManager().registerEvents(new NeedListener(mixedCatastrophesData), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(mixedCatastrophesData), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ConstructListener(mixedCatastrophesData), plugin);
        plugin.getServer().getPluginManager().registerEvents(new MixedAchievementListener(mixedCatastrophesData), plugin);
        plugin.getServer().getPluginManager().registerEvents(new AttackListener(mixedCatastrophesData), plugin);
    }
}
