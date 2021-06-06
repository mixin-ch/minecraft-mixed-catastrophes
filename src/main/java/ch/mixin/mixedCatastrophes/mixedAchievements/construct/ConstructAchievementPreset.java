package ch.mixin.mixedCatastrophes.mixedAchievements.construct;

import ch.mixin.mixedCatastrophes.mixedAchievements.AchievementStagePreset;

import java.util.List;

public class ConstructAchievementPreset {
    private final List<AchievementStagePreset> stageList;

    public ConstructAchievementPreset(List<AchievementStagePreset> stageList) {
        this.stageList = stageList;
    }

    public List<AchievementStagePreset> getStageList() {
        return stageList;
    }
}
