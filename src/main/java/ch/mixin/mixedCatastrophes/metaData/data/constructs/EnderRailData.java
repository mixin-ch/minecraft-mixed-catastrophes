package ch.mixin.mixedCatastrophes.metaData.data.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.metaData.EnderRailDirection;

public class EnderRailData extends ConstructData {
    private int level;
    private int rotations;
    private EnderRailDirection direction;

    public EnderRailData(Coordinate3D position, String worldName, int level, int rotations, EnderRailDirection direction) {
        super(position, worldName,0);
        this.level = level;
        this.rotations = rotations;
        this.direction = direction;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRotations() {
        return rotations;
    }

    public void setRotations(int rotations) {
        this.rotations = rotations;
    }

    public EnderRailDirection getDirection() {
        return direction;
    }

    public void setDirection(EnderRailDirection direction) {
        this.direction = direction;
    }
}
