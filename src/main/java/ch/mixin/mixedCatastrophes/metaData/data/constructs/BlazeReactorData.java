package ch.mixin.mixedCatastrophes.metaData.data.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public class BlazeReactorData extends ConstructData {
    private int level;
    private int rotation;
    private int fuel;

    public BlazeReactorData(Coordinate3D position, String worldName, int level, int rotation, int fuel) {
        super(position, worldName, 0);
        this.level = level;
        this.rotation = rotation;
        this.fuel = fuel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }
}
