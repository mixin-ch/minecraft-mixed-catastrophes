package ch.mixin.MetaData;

import ch.mixin.helperClasses.Coordinate3D;

public class BlitzardData {
    private Coordinate3D position;
    private String worldName;
    private int level;

    public BlitzardData(Coordinate3D position, String worldName, int level) {
        this.position = position;
        this.worldName = worldName;
        this.level = level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
