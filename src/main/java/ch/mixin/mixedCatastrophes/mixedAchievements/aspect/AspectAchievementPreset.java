package ch.mixin.mixedCatastrophes.mixedAchievements.aspect;

import ch.mixin.mixedCatastrophes.mixedAchievements.AchievementStagePreset;

import java.util.List;

public class AspectAchievementPreset {
    private final boolean hoard;
    private final List<AchievementStagePreset> stageList;

    public AspectAchievementPreset(boolean hoard, List<AchievementStagePreset> stageList) {
        this.hoard = hoard;
        this.stageList = stageList;
    }

    public boolean isHoard() {
        return hoard;
    }

    public List<AchievementStagePreset> getStageList() {
        return stageList;
    }
}
