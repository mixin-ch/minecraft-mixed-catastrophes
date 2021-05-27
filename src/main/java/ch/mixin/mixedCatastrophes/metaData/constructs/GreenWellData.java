package ch.mixin.mixedCatastrophes.metaData.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public class GreenWellData extends ConstructData {
    private int level;

    public GreenWellData(Coordinate3D position, String worldName, int level) {
        super(position, worldName);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}