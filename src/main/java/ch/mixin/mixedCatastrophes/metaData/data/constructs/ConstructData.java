package ch.mixin.mixedCatastrophes.metaData.data.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public abstract class ConstructData {
    protected Coordinate3D position;
    protected String worldName;
    protected int inactiveTime;

    protected ConstructData(Coordinate3D position, String worldName, int inactiveTime) {
        this.position = position;
        this.worldName = worldName;
        this.inactiveTime = inactiveTime;
    }

    public void inactiveTick(){
        inactiveTime++;
    }

    public Coordinate3D getPosition() {
        return position;
    }

    public void setPosition(Coordinate3D position) {
        this.position = position;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public int getInactiveTime() {
        return inactiveTime;
    }

    public void setInactiveTime(int inactiveTime) {
        this.inactiveTime = inactiveTime;
    }
}
