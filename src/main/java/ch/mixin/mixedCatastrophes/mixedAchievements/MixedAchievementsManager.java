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
        AchievementSetBlueprint achievementSetBlueprint = makeAchievementSetBlueprint();

        try {
            achievementApi = mixedAchievementsPlugin.makeAchievementSet(achievementSetBlueprint);
        } catch (ServiceUnavailableException e) {
            e.printStackTrace();
        }

        active = true;
    }

    private AchievementSetBlueprint makeAchievementSetBlueprint() {
        AchievementSetBlueprint achievementSetBlueprint = new AchievementSetBlueprint(
                new AchievementItemSetup(Material.NETHER_STAR, ChatColor.of("#FFFFFF") + "Mixed Catastrophes", 1, new ArrayList<>())
                , "MixedCatastrophes"
                , "Mixed Catastrophes");

        fillBlueprintAchievements(achievementSetBlueprint.getSubAchievementBlueprintElementMap());
        return achievementSetBlueprint;
    }

    private void fillBlueprintAchievements(HashMap<Integer, AchievementBlueprintElement> subAchievementBlueprintElementMap) {
        subAchievementBlueprintElementMap.put(slot(1, 1), new AchievementBlueprintLeafElement(
                new AchievementItemSetup(Material.OAK_PLANKS, ChatColor.of("#7F00FF") + "Test", 4, new ArrayList<>(Arrays.asList(
                        "Do something."
                )))
                , "test"
                , "test"
                , 4
        ));
    }

    private int slot(int row, int col) {
        return 9 * (row - 1) + (col - 1);
    }

    public boolean isActive() {
        return active;
    }
}
