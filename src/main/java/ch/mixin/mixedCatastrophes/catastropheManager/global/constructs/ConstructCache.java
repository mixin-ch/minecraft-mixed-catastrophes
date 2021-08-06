package ch.mixin.mixedCatastrophes.catastropheManager.global.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.ShapeCompareResult;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.ConstructData;
import com.gmail.filoghost.holographicdisplays.api.Hologram;

public class ConstructCache {
    private Hologram hologram;
    private boolean active;
    private boolean changed;
    private ShapeCompareResult shapeCompareResult;

    public ConstructCache(Hologram hologram, boolean active, boolean changed, ShapeCompareResult shapeCompareResult) {
        this.hologram = hologram;
        this.active = active;
        this.changed = changed;
        this.shapeCompareResult = shapeCompareResult;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public ShapeCompareResult getShapeCompareResult() {
        return shapeCompareResult;
    }

    public void setShapeCompareResult(ShapeCompareResult shapeCompareResult) {
        this.shapeCompareResult = shapeCompareResult;
    }
}
