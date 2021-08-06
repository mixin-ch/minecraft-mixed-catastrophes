package ch.mixin.mixedCatastrophes.metaData.data.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public class ScarecrowData extends ConstructData {
    private int rotation;
    private int collectedTerror;

    public ScarecrowData(Coordinate3D position, String worldName, int rotation, int collectedTerror) {
        super(position, worldName,0);
        this.rotation = rotation;
        this.collectedTerror = collectedTerror;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getCollectedTerror() {
        return collectedTerror;
    }

    public void setCollectedTerror(int collectedTerror) {
        this.collectedTerror = collectedTerror;
    }
}
