package ch.mixin.mixedCatastrophes.mixedAchievements;

import ch.mixin.mixedAchievements.api.AchievementApi;
import ch.mixin.mixedAchievements.blueprint.AchievementItemSetup;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementElement;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementLeaf;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementSet;
import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.mixedAchievements.aspect.AspectAchievementManager;
import ch.mixin.mixedCatastrophes.mixedAchievements.construct.ConstructAchievementManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MixedAchievementsManager {
    private boolean active = false;
    private AchievementApi achievementApi;
    private AspectAchievementManager aspectAchievementManager;
    private ConstructAchievementManager constructAchievementManager;

    public void initializeAchievements() {
        if (!MixedCatastrophesPlugin.UseMixedAchievementsPlugin)
            return;

        aspectAchievementManager = new AspectAchievementManager();
        constructAchievementManager = new ConstructAchievementManager();
        BlueprintAchievementSet blueprintAchievementSet = makeBlueprintAchievementSet();

        try {
            achievementApi = MixedCatastrophesPlugin.MixedAchievementsPlugin.makeAchievementSet(blueprintAchievementSet);
        } catch (ServiceUnavailableException e) {
            e.printStackTrace();
        }

        active = true;
    }

    private BlueprintAchievementSet makeBlueprintAchievementSet() {
        BlueprintAchievementSet achievementSetBlueprint = new BlueprintAchievementSet(
                MixedCatastrophesPlugin.PLUGIN_NAME, "Mixed Catastrophes", new AchievementItemSetup(
                Material.CLOCK, ChatColor.of("#FFFFFF") + "Mixed Catastrophes", 1, new ArrayList<>()
        ));

        List<BlueprintAchievementLeaf> achievementList = new ArrayList<>();
        achievementList.addAll(aspectAchievementManager.makeAspectAchievements());
        achievementList.addAll(constructAchievementManager.makeAspectAchievements());
        fillBlueprintAchievements(achievementSetBlueprint.getBlueprintAchievementElementMap(), achievementList);
        return achievementSetBlueprint;
    }

    private void fillBlueprintAchievements(HashMap<Integer, BlueprintAchievementElement> BlueprintAchievementElementMap, List<BlueprintAchievementLeaf> achievementList) {
        int slot = 0;

        for (BlueprintAchievementLeaf achievement : achievementList) {
            if (slot == 8)
                slot++;

            BlueprintAchievementElementMap.put(slot, achievement);
            slot++;
        }
    }

    public void updateAspectAchievementProgress(Player player, HashMap<AspectType, Integer> aspects, HashMap<AspectType, Integer> changeMap) {
        aspectAchievementManager.updateAchievementProgress(achievementApi, player, aspects, changeMap);
    }

    public void updateConstructAchievementProgress(Player player, ConstructType constructType, int value) {
        constructAchievementManager.updateAchievementProgress(achievementApi, player, constructType, value);
    }

    private int slot(int row, int col) {
        return 9 * (row - 1) + (col - 1);
    }

    public boolean isActive() {
        return active;
    }
}
