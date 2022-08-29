package ch.mixin.mixedCatastrophes.eventChange.aspect;

public enum AspectType {
    Death_Seeker,
    Secrets,
    Terror,
    Misfortune,
    Nature_Conspiracy,
    Greyhat_Debt,
    Celestial_Favor,
    Nobility,
    Resolve,
    SkyScorn,
    SeaScorn,
    ;

    private final String label;

    AspectType() {
        label = this.toString().replace("_", " ");
    }

    public String getLabel() {
        return label;
    }
}
