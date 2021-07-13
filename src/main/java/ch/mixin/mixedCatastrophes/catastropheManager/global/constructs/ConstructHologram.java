package ch.mixin.mixedCatastrophes.catastropheManager.global.constructs;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

public class ConstructHologram {
    private Hologram hologram;
    private boolean constructed;
    private boolean changed;

    public ConstructHologram(Hologram hologram, boolean constructed, boolean changed) {
        this.hologram = hologram;
        this.constructed = constructed;
        this.changed = changed;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void setHologram(Hologram hologram) {
        this.hologram = hologram;
    }

    public boolean isConstructed() {
        return constructed;
    }

    public void setConstructed(boolean constructed) {
        this.constructed = constructed;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
