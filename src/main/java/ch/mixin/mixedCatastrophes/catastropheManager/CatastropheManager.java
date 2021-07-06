package ch.mixin.mixedCatastrophes.catastropheManager;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.MetaData;

public abstract class CatastropheManager {
    protected final MixedCatastrophesData mixedCatastrophesData;

    public CatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    public abstract void initializeMetaData();

    public abstract void updateMetaData();

    public abstract void initializeCauser();

    public abstract void tick();
}
