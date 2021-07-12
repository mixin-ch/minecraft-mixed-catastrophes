package ch.mixin.mixedCatastrophes.metaData.data;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public class StalkerData {
    protected String worldName;
    protected Coordinate3D coordinate3D;
    protected double speed;
    protected int remainingTime;

    public StalkerData(String worldName, Coordinate3D coordinate3D, double speed, int remainingTime) {
        this.worldName = worldName;
        this.coordinate3D = coordinate3D;
        this.speed = speed;
        this.remainingTime = remainingTime;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Coordinate3D getCoordinate3D() {
        return coordinate3D;
    }

    public void setCoordinate3D(Coordinate3D coordinate3D) {
        this.coordinate3D = coordinate3D;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
}
