package ch.mixin.mixedCatastrophes.catastropheManager;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.MetaData;

public abstract class CatastropheManager {
    protected final MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor;
    protected final MixedCatastrophesPlugin plugin;
    protected final MetaData metaData;

    public CatastropheManager(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        this.mixedCatastrophesManagerAccessor = mixedCatastrophesManagerAccessor;
        plugin = mixedCatastrophesManagerAccessor.getPlugin();
        metaData = mixedCatastrophesManagerAccessor.getMetaData();
    }

    public abstract void initializeMetaData();

    public abstract void updateMetaData();

    public abstract void initializeCauser();

    public abstract void tick();
}
