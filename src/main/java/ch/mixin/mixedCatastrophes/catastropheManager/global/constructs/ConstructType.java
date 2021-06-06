package ch.mixin.mixedCatastrophes.catastropheManager.global.constructs;

public enum ConstructType {
    GreenWell,
    BlazeReactor,
    Blitzard,
    Lighthouse,
    Scarecrow,
    ;

    private final String label;

    ConstructType() {
        label = this.toString().replace("_", " ");
    }

    public String getLabel() {
        return label;
    }
}
