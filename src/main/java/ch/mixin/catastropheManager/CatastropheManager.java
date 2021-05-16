package ch.mixin.catastropheManager;

import ch.mixin.metaData.MetaData;
import ch.mixin.main.MixedCatastrophesPlugin;

public abstract class CatastropheManager {
    protected final MixedCatastrophesPlugin plugin;
    protected final MetaData metaData;
    protected final RootCatastropheManager rootCatastropheManager;

    public CatastropheManager(MixedCatastrophesPlugin plugin, RootCatastropheManager rootCatastropheManager) {
        this.plugin = plugin;
        metaData = plugin.getMetaData();
        this.rootCatastropheManager = rootCatastropheManager;
    }

    public abstract void initializeMetaData();

    public abstract void updateMetaData();

    public abstract void initializeCauser();

    public abstract void tick();
}
