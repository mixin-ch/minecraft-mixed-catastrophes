package ch.mixin.mixedCatastrophes.metaData.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public class LighthouseData extends ConstructData {
    private int level;

    public LighthouseData(Coordinate3D position, String worldName, int level) {
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