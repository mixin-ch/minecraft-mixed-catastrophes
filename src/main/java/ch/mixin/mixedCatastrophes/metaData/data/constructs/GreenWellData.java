package ch.mixin.mixedCatastrophes.metaData.data.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public class GreenWellData extends ConstructData {
    private int level;

    public GreenWellData(Coordinate3D position, String worldName, int level) {
        super(position, worldName,0);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
