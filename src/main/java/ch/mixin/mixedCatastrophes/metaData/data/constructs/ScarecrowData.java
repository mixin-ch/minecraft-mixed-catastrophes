package ch.mixin.mixedCatastrophes.metaData.data.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public class ScarecrowData extends ConstructData {
    private int collectedTerror;

    public ScarecrowData(Coordinate3D position, String worldName, int collectedTerror) {
        super(position, worldName,0);
        this.collectedTerror = collectedTerror;
    }

    public int getCollectedTerror() {
        return collectedTerror;
    }

    public void setCollectedTerror(int collectedTerror) {
        this.collectedTerror = collectedTerror;
    }
}
