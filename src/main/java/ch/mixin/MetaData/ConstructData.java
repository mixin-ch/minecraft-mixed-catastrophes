package ch.mixin.MetaData;

import ch.mixin.helperClasses.Coordinate3D;

public abstract class ConstructData {
    protected Coordinate3D position;
    protected String worldName;

    protected ConstructData(Coordinate3D position, String worldName) {
        this.position = position;
        this.worldName = worldName;
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
}
