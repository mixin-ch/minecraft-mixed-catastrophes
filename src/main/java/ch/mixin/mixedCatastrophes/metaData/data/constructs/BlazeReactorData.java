package ch.mixin.mixedCatastrophes.metaData.data.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public class BlazeReactorData extends ConstructData {
    private int level;
    private int fuel;

    public BlazeReactorData(Coordinate3D position, String worldName, int level, int fuel) {
        super(position, worldName,0);
        this.level = level;
        this.fuel = fuel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }
}
