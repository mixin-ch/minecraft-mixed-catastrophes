package ch.mixin.mixedCatastrophes.mixedAchievements;

import ch.mixin.mixedAchievements.api.AchievementApi;
import ch.mixin.mixedAchievements.blueprint.*;
import ch.mixin.mixedAchievements.main.MixedAchievementsPlugin;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MixedAchievementsManager {
    private boolean active = false;
    private AchievementApi achievementApi;

    public void initializeAchievements() {
        if (!MixedCatastrophesPlugin.UseMixedAchievementsPlugin)
            return;

        MixedAchievementsPlugin mixedAchievementsPlugin = MixedCatastrophesPlugin.MixedAchievementsPlugin;
        BlueprintAchievementSet blueprintAchievementSet = makeBlueprintAchievementSet();

        try {
            achievementApi = mixedAchievementsPlugin.makeAchievementSet(blueprintAchievementSet);
        } catch (ServiceUnavailableException e) {
            e.printStackTrace();
        }

        active = true;
    }

    private BlueprintAchievementSet makeBlueprintAchievementSet() {
        BlueprintAchievementSet achievementSetBlueprint = new BlueprintAchievementSet(
                "MixedCatastrophes", "Mixed Catastrophes", new AchievementItemSetup(
                Material.NETHER_STAR, ChatColor.of("#FFFFFF") + "Mixed Catastrophes", 1, new ArrayList<>()
        ));

        fillBlueprintAchievements(achievementSetBlueprint.getBlueprintAchievementElementMap());
        return achievementSetBlueprint;
    }

    private void fillBlueprintAchievements(HashMap<Integer, BlueprintAchievementElement> BlueprintAchievementElementMap) {
        BlueprintAchievementElementMap.put(slot(1, 1), new BlueprintAchievementLeaf("test1", new AchievementItemSetup(
                Material.OAK_PLANKS, ChatColor.of("#FF007F") + "Test1", 1, new ArrayList<>(Arrays.asList(
                "Do something."
        )))));
        BlueprintAchievementElementMap.put(slot(1, 2), new BlueprintAchievementLeaf("test2", new AchievementItemSetup(
                Material.BIRCH_PLANKS, ChatColor.of("#FF007F") + "Test2", 2, new ArrayList<>(Arrays.asList(
                "Do something."
        )))
                , 5
        ));
        BlueprintAchievementElementMap.put(slot(1, 3), new BlueprintAchievementLeaf("test3", new ArrayList<>(Arrays.asList(
                new AchievementItemSetup(
                        Material.SPRUCE_PLANKS, ChatColor.of("#FF007F") + "Test3.1", 31, new ArrayList<>(Arrays.asList(
                        "Do something."
                )))
                , new AchievementItemSetup(
                        Material.DARK_OAK_PLANKS, ChatColor.of("#FF007F") + "Test3.2", 32, new ArrayList<>(Arrays.asList(
                        "Do 2."
                )))
        ))));
        BlueprintAchievementElementMap.put(slot(1, 4), new BlueprintAchievementLeaf("test4", new ArrayList<>(Arrays.asList(
                new BlueprintAchievementStage(
                        new AchievementItemSetup(
                                Material.ACACIA_PLANKS, ChatColor.of("#FF007F") + "Test4.1", 41, new ArrayList<>(Arrays.asList(
                                "Do something."
                        ))), 3
                )
                , new BlueprintAchievementStage(
                        new AchievementItemSetup(
                                Material.JUNGLE_PLANKS, ChatColor.of("#FF007F") + "Test4.2", 42, new ArrayList<>(Arrays.asList(
                                "Do 2."
                        )))
                )
        )),
                true
        ));
    }

    private int slot(int row, int col) {
        return 9 * (row - 1) + (col - 1);
    }

    public boolean isActive() {
        return active;
    }
}
