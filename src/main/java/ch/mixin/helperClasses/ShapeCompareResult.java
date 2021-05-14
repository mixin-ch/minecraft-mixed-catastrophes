package ch.mixin.helperClasses;

public class ShapeCompareResult {
    private final boolean isConstructed;
    private final int rotations;

    public ShapeCompareResult(boolean isConstructed, int rotations) {
        this.isConstructed = isConstructed;
        this.rotations = rotations;
    }

    public boolean isConstructed() {
        return isConstructed;
    }

    public int getRotations() {
        return rotations;
    }
}
