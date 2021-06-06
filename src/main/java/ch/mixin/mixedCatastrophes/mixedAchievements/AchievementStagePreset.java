package ch.mixin.mixedCatastrophes.mixedAchievements;

public class AchievementStagePreset {
    private final String name;
    private final int verge;

    public AchievementStagePreset(String name, int verge) {
        this.name = name;
        this.verge = verge;
    }

    public String getName() {
        return name;
    }

    public int getVerge() {
        return verge;
    }
}
