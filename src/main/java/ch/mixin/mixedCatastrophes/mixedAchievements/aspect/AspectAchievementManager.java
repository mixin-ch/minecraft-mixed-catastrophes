package ch.mixin.mixedCatastrophes.mixedAchievements.aspect;

import ch.mixin.mixedAchievements.api.AchievementApi;
import ch.mixin.mixedAchievements.blueprint.AchievementItemSetup;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementLeaf;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementStage;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Theme;
import ch.mixin.mixedCatastrophes.mixedAchievements.AchievementStagePreset;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class AspectAchievementManager {
    private static HashMap<AspectType, AspectAchievementPreset> presetMap;

    static {
        presetMap = new HashMap<>();
        presetMap.put(AspectType.Secrets, new AspectAchievementPreset(
                false
                , new ArrayList<>(Arrays.asList(
                new AchievementStagePreset("a Hint now fading", 100)
                , new AchievementStagePreset("a Clue so absurd", 500)
                , new AchievementStagePreset("a Puzzle yet unsolved", 2000)
                , new AchievementStagePreset("a Riddle simply cryptic", 6000)
                , new AchievementStagePreset("an Enigma too complex", 15000)
                , new AchievementStagePreset("a Mystery long forgotten", 30000)
                , new AchievementStagePreset("a Secret never known", 50000)
        ))));
        presetMap.put(AspectType.Terror, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AchievementStagePreset("Worry", 50)
                , new AchievementStagePreset("Anxiety", 100)
                , new AchievementStagePreset("Fear", 200)
                , new AchievementStagePreset("Horror", 300)
                , new AchievementStagePreset("Terror", 500)
        ))));
        presetMap.put(AspectType.Nobility, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AchievementStagePreset("Edler", 10)
                , new AchievementStagePreset("Knight", 20)
                , new AchievementStagePreset("Lord", 30)
                , new AchievementStagePreset("Baron", 40)
                , new AchievementStagePreset("Count", 50)
                , new AchievementStagePreset("Duke", 60)
                , new AchievementStagePreset("King", 70)
                , new AchievementStagePreset("Emperor", 80)
        ))));
        presetMap.put(AspectType.Nature_Conspiracy, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AchievementStagePreset("Natures Distaste", 1)
                , new AchievementStagePreset("Natures Dislike", 4)
                , new AchievementStagePreset("Natures Despise", 16)
                , new AchievementStagePreset("Natures Hatred", 64)
        ))));
        presetMap.put(AspectType.Celestial_Favor, new AspectAchievementPreset(
                false
                , new ArrayList<>(Arrays.asList(
                new AchievementStagePreset("make a Wish", 1)
                , new AchievementStagePreset("Guardian Star", 5)
                , new AchievementStagePreset("a Friend to Skies", 20)
        ))));
        presetMap.put(AspectType.Misfortune, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AchievementStagePreset("no 4-Leaf Clover", 1)
                , new AchievementStagePreset("45% Coin Toss", 4)
                , new AchievementStagePreset("always wrong Guesses", 16)
                , new AchievementStagePreset("Dice roll only 1", 64)
        ))));
        presetMap.put(AspectType.Death_Seeker, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AchievementStagePreset("the Day I died", 1)
                , new AchievementStagePreset("a Cat dies 9 Times", 9)
                , new AchievementStagePreset("the Answer to Death and Everything", 42)
        ))));
        presetMap.put(AspectType.Greyhat_Debt, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AchievementStagePreset("Business", 1)
                , new AchievementStagePreset("sold your Soul", 10)
        ))));
        presetMap.put(AspectType.Resolve, new AspectAchievementPreset(
                true
                , new ArrayList<>(Arrays.asList(
                new AchievementStagePreset("Confident", 100)
                , new AchievementStagePreset("Determined", 200)
                , new AchievementStagePreset("Unbreakable", 400)
        ))));
    }

    public List<BlueprintAchievementLeaf> makeAspectAchievements() {
        List<BlueprintAchievementLeaf> blueprintAchievementLeafList = new ArrayList<>();

        for (AspectType aspectType : Constants.AspectOrder) {
            AspectAchievementPreset preset = presetMap.get(aspectType);

            if (preset == null)
                continue;

            String achievementId = aspectType.toString();
            String achievementLabel = aspectType.getLabel();
            Theme theme = Constants.AspectThemes.get(aspectType);
            List<BlueprintAchievementStage> blueprintAchievementStageList = new ArrayList<>();

            for (int i = 0; i < preset.getStageList().size(); i++) {
                AchievementStagePreset stagePreset = preset.getStageList().get(i);
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

            blueprintAchievementLeafList.add(new BlueprintAchievementLeaf(achievementId, blueprintAchievementStageList));
        }

        return blueprintAchievementLeafList;
    }

    public void updateAchievementProgress(AchievementApi achievementApi, Player player, HashMap<AspectType, Integer> aspects, HashMap<AspectType, Integer> changeMap) {
        String playerId = player.getUniqueId().toString();

        for (AspectType aspectType : aspects.keySet()) {
            AspectAchievementPreset preset = presetMap.get(aspectType);

            if (preset == null)
                continue;
            if (!preset.isHoard())
                continue;

            int value = aspects.get(aspectType);

            if (value < 0)
                continue;

            String achievementId = aspectType.toString();
            achievementApi.setPoints(achievementId, playerId, value);
        }

        for (AspectType aspectType : changeMap.keySet()) {
            AspectAchievementPreset preset = presetMap.get(aspectType);

            if (preset == null)
                continue;
            if (preset.isHoard())
                continue;

            int value = changeMap.get(aspectType);

            if (value <= 0)
                continue;

            String achievementId = aspectType.toString();
            achievementApi.addPoints(achievementId, playerId, value);
        }
    }
}
