package ch.mixin.mixedCatastrophes.mixedAchievements;

import ch.mixin.mixedAchievements.blueprint.AchievementItemSetup;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementLeaf;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementStage;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Theme;
import net.md_5.bungee.api.ChatColor;

import java.util.*;

public class AspectAchievementManager {
    private static HashMap<AspectType, AspectAchievementPreset> presetMap;

    static {
        presetMap = new HashMap<>();
        presetMap.put(AspectType.Secrets, new AspectAchievementPreset(
                false
                , new ArrayList<>(Arrays.asList(
                new AspectAchievementStagePreset("a Hint now fading", 100)
                , new AspectAchievementStagePreset("a Clue so absurd", 500)
                , new AspectAchievementStagePreset("a Puzzle yet unsolved", 2000)
                , new AspectAchievementStagePreset("a Riddle simply cryptic", 6000)
                , new AspectAchievementStagePreset("an Enigma too complex", 15000)
                , new AspectAchievementStagePreset("a Mystery long forgotten", 30000)
                , new AspectAchievementStagePreset("a Secret never known", 50000)
        ))));
        presetMap.put(AspectType.Terror, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AspectAchievementStagePreset("Worry", 50)
                , new AspectAchievementStagePreset("Anxiety", 100)
                , new AspectAchievementStagePreset("Fear", 200)
                , new AspectAchievementStagePreset("Horror", 300)
                , new AspectAchievementStagePreset("Terror", 500)
        ))));
        presetMap.put(AspectType.Misfortune, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AspectAchievementStagePreset("no 4-Leaf Clover", 1)
                , new AspectAchievementStagePreset("45% Coin Toss", 4)
                , new AspectAchievementStagePreset("always wrong Guesses", 16)
                , new AspectAchievementStagePreset("Dice roll only 1", 64)
        ))));
        presetMap.put(AspectType.Nature_Conspiracy, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AspectAchievementStagePreset("Natures Distaste", 1)
                , new AspectAchievementStagePreset("Natures Dislike", 4)
                , new AspectAchievementStagePreset("Natures Despise", 16)
                , new AspectAchievementStagePreset("Natures Hatred", 64)
        ))));
        presetMap.put(AspectType.Celestial_Favor, new AspectAchievementPreset(
                false
                , new ArrayList<>(Arrays.asList(
                new AspectAchievementStagePreset("make a Wish", 1)
                , new AspectAchievementStagePreset("Guardian Star", 5)
                , new AspectAchievementStagePreset("a Friend to Skies", 20)
        ))));
        presetMap.put(AspectType.Death_Seeker, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AspectAchievementStagePreset("the Day I died", 1)
                , new AspectAchievementStagePreset("a Cat dies 9 Times", 9)
                , new AspectAchievementStagePreset("the Answer to Death and Everything", 42)
        ))));
        presetMap.put(AspectType.Greyhat_Debt, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AspectAchievementStagePreset("Business", 1)
                , new AspectAchievementStagePreset("sold your Soul", 10)
        ))));
    }

    public List<BlueprintAchievementLeaf> makeAspectAchievements() {
        List<BlueprintAchievementLeaf> blueprintAchievementLeafList = new ArrayList<>();

        for (AspectType aspectType : Constants.AspectOrder) {
            AspectAchievementPreset preset = presetMap.get(aspectType);

            if (preset == null)
                continue;

            String achievementId = aspectType.toString();
            String achievementLabel = achievementId.replace("_", " ");
            Theme theme = Constants.AspectThemes.get(aspectType);
            List<BlueprintAchievementStage> blueprintAchievementStageList = new ArrayList<>();

            for (int i = 0; i < preset.getStageList().size(); i++) {
                AspectAchievementStagePreset stagePreset = preset.getStageList().get(i);
                String lore;

                if (preset.isHoard()) {
                    lore = "Hoard up to " + stagePreset.getVerge() + " " + achievementLabel + ".";
                } else {
                    lore = "Collect " + stagePreset.getVerge() + " " + achievementLabel + ".";
                }


                blueprintAchievementStageList.add(
                        new BlueprintAchievementStage(
                                new AchievementItemSetup(
                                        theme.getMaterial()
                                        , theme.getColor() + stagePreset.getName()
                                        , i + 1
                                        , new ArrayList<>(Collections.singletonList(ChatColor.WHITE + lore)))
                                , stagePreset.getVerge()
                        ));
            }

            blueprintAchievementLeafList.add(new BlueprintAchievementLeaf(
                    "achievementId", blueprintAchievementStageList, true));

        }

        return blueprintAchievementLeafList;
    }
}
