package ch.mixin.mixedCatastrophes.mixedAchievements;

import ch.mixin.mixedAchievements.api.AchievementApi;
import ch.mixin.mixedAchievements.blueprint.AchievementItemSetup;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementElement;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementLeaf;
import ch.mixin.mixedAchievements.blueprint.BlueprintAchievementSet;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.naming.ServiceUnavailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MixedAchievementsManager {
    private boolean active = false;
    private AspectAchievementManager aspectAchievementManager;
    private AchievementApi achievementApi;

    public void initializeAchievements() {
        if (!MixedCatastrophesPlugin.UseMixedAchievementsPlugin)
            return;

        aspectAchievementManager = new AspectAchievementManager();
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
                "MixedCatastrophes", "Mixed Catastrophes", new AchievementItemSetup(
                Material.NETHER_STAR, ChatColor.of("#FFFFFF") + "Mixed Catastrophes", 1, new ArrayList<>()
        ));

        fillBlueprintAchievements(achievementSetBlueprint.getBlueprintAchievementElementMap());
        return achievementSetBlueprint;
    }

    private void fillBlueprintAchievements(HashMap<Integer, BlueprintAchievementElement> BlueprintAchievementElementMap) {
        int slot = 0;
        List<BlueprintAchievementLeaf> aspectAchievementList = aspectAchievementManager.makeAspectAchievements();

        for (BlueprintAchievementLeaf aspectAchievement : aspectAchievementList) {
            if (slot == 8)
                slot++;

            BlueprintAchievementElementMap.put(slot, aspectAchievement);

            slot++;
        }
    }

    public void updateAchievementProgress(Player player, HashMap<AspectType, Integer> aspects, HashMap<AspectType, Integer> changeMap) {
        aspectAchievementManager.updateAchievementProgress(achievementApi, player, aspects, changeMap);
    }

    private int slot(int row, int col) {
        return 9 * (row - 1) + (col - 1);
    }

    public boolean isActive() {
        return active;
    }
}
