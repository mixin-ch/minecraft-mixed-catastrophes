package ch.mixin.mixedCatastrophes.mixedAchievements.construct;

import ch.mixin.mixedAchievements.api.AchievementApi;
import ch.mixin.mixedAchievements.blueprint.AchievementItemSetup;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementLeaf;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementStage;
import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Theme;
import ch.mixin.mixedCatastrophes.mixedAchievements.AchievementStagePreset;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class ConstructAchievementManager {
    private static HashMap<ConstructType, String> buildPresetMap;
    private static HashMap<ConstructType, ConstructAchievementPreset> contributePresetMap;

    static {
        buildPresetMap = new HashMap<>();
        buildPresetMap.put(ConstructType.GreenWell, "the Green Growth");
        buildPresetMap.put(ConstructType.BlazeReactor, "the Machine of Flames");
        buildPresetMap.put(ConstructType.Blitzard, "the Lightnings Favourite");
        buildPresetMap.put(ConstructType.Lighthouse, "the Lantern of Solace");
        buildPresetMap.put(ConstructType.Scarecrow, "the Screams from Beyond");
        buildPresetMap.put(ConstructType.EnderRail, "the Bridge through Space");

        contributePresetMap = new HashMap<>();
        contributePresetMap.put(ConstructType.GreenWell, new ConstructAchievementPreset(
                new ArrayList<>(Arrays.asList(
                        new AchievementStagePreset("sprouting", 2)
                        , new AchievementStagePreset("blooming", 7)
                        , new AchievementStagePreset("overgrowing", 12)
                ))));
        contributePresetMap.put(ConstructType.BlazeReactor, new ConstructAchievementPreset(
                new ArrayList<>(Arrays.asList(
                        new AchievementStagePreset("smoking", 2)
                        , new AchievementStagePreset("sizzling", 4)
                        , new AchievementStagePreset("flaming", 6)
                ))));
        contributePresetMap.put(ConstructType.Blitzard, new ConstructAchievementPreset(
                new ArrayList<>(Arrays.asList(
                        new AchievementStagePreset("smoking", 2)
                        , new AchievementStagePreset("sparking", 6)
                ))));
        contributePresetMap.put(ConstructType.Lighthouse, new ConstructAchievementPreset(
                new ArrayList<>(Arrays.asList(
                        new AchievementStagePreset("glowing", 2)
                        , new AchievementStagePreset("shining", 7)
                        , new AchievementStagePreset("radiating", 12)
                ))));
        contributePresetMap.put(ConstructType.EnderRail, new ConstructAchievementPreset(
                new ArrayList<>(Arrays.asList(
                        new AchievementStagePreset("stabilizing", 2)
                        , new AchievementStagePreset("converging", 12)
                ))));
    }

    public List<BlueprintAchievementLeaf> makeAspectAchievements() {
        List<BlueprintAchievementLeaf> blueprintAchievementLeafList = new ArrayList<>();

        for (ConstructType constructType : Constants.ConstructOrder) {
            if (buildPresetMap.containsKey(constructType)) {
                String presetTitle = buildPresetMap.get(constructType);
                String achievementId = constructType.toString() + "_build";
                String achievementLabel = constructType.getLabel();
                Theme theme = Constants.ConstructThemes.get(constructType);
                String lore = "Build a " + achievementLabel + ".";

                AchievementItemSetup achievementItemSetup = new AchievementItemSetup(
                        theme.getMaterial()
                        , theme.getColor() + presetTitle
                        , 1
                        , new ArrayList<>(Collections.singletonList(ChatColor.WHITE + lore)));

                blueprintAchievementLeafList.add(BlueprintAchievementLeaf.createSimple(achievementId, achievementItemSetup));
            }

            if (contributePresetMap.containsKey(constructType)) {
                ConstructAchievementPreset preset = contributePresetMap.get(constructType);

                String achievementId = constructType.toString() + "_contribute";
                String achievementLabel = constructType.getLabel();
                Theme theme = Constants.ConstructThemes.get(constructType);
                List<BlueprintAchievementStage> blueprintAchievementStageList = new ArrayList<>();

                for (int i = 0; i < preset.getStageList().size(); i++) {
                    AchievementStagePreset stagePreset = preset.getStageList().get(i);
                    String lore = "Contribute on a " + achievementLabel + " to at least Level " + stagePreset.getVerge() + ".";

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
        }

        return blueprintAchievementLeafList;
    }

    public void updateAchievementProgress(AchievementApi achievementApi, Player player, ConstructType constructType, int value) {
        if (value > 1) {
            updateAchievementProgressContribute(achievementApi, player, constructType, value);
        } else {
            updateAchievementProgressBuild(achievementApi, player, constructType);
        }
    }

    private void updateAchievementProgressBuild(AchievementApi achievementApi, Player player, ConstructType constructType) {
        if (!buildPresetMap.containsKey(constructType))
            return;

        String achievementId = constructType.toString() + "_build";
        String playerId = player.getUniqueId().toString();
        achievementApi.completeAbsolut(achievementId, playerId);
    }

    private void updateAchievementProgressContribute(AchievementApi achievementApi, Player player, ConstructType constructType, int value) {
        ConstructAchievementPreset constructAchievementPreset = contributePresetMap.get(constructType);

        if (constructAchievementPreset == null)
            return;

        String achievementId = constructType.toString() + "_contribute";
        String playerId = player.getUniqueId().toString();
        int points = achievementApi.getPoints(achievementId, playerId);

        if (value <= points)
            return;

        achievementApi.setPoints(achievementId, playerId, value);
    }
}
