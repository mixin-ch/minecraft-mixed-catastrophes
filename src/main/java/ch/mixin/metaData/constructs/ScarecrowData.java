package ch.mixin.metaData.constructs;

import ch.mixin.helperClasses.Coordinate3D;

public class ScarecrowData extends ConstructData {
    private int collectedTerror;

    public ScarecrowData(Coordinate3D position, String worldName, int collectedTerror) {
        super(position, worldName);
        this.collectedTerror = collectedTerror;
    }

    public int getCollectedTerror() {
        return collectedTerror;
    }

    public void setCollectedTerror(int collectedTerror) {
        this.collectedTerror = collectedTerror;
    }
}
