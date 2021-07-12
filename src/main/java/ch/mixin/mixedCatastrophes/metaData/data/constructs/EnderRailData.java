package ch.mixin.mixedCatastrophes.metaData.data.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.metaData.EnderRailDirection;

public class EnderRailData extends ConstructData {
    private int level;
    private EnderRailDirection direction;

    public EnderRailData(Coordinate3D position, String worldName, int level, EnderRailDirection direction) {
        super(position, worldName);
        this.level = level;
       this.direction = direction;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public EnderRailDirection getDirection() {
        return direction;
    }

    public void setDirection(EnderRailDirection direction) {
        this.direction = direction;
    }
}
