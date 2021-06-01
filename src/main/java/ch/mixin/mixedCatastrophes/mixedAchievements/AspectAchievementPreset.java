package ch.mixin.mixedCatastrophes.mixedAchievements;

import java.util.List;

public class AspectAchievementPreset {
    private final boolean hoard;
    private final List<AspectAchievementStagePreset> stageList;

    public AspectAchievementPreset(boolean hoard, List<AspectAchievementStagePreset> stageList) {
        this.hoard = hoard;
        this.stageList = stageList;
    }

    public boolean isHoard() {
        return hoard;
    }

    public List<AspectAchievementStagePreset> getStageList() {
        return stageList;
    }
}
