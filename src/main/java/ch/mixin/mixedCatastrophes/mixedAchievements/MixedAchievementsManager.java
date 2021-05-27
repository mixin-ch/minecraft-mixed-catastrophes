package ch.mixin.mixedCatastrophes.mixedAchievements;

import ch.mixin.mixedAchievements.api.AchievementApi;
import ch.mixin.mixedAchievements.blueprint.AchievementBlueprintFolderElement;
import ch.mixin.mixedAchievements.blueprint.AchievementBlueprintLeafElement;
import ch.mixin.mixedAchievements.blueprint.AchievementItemSetup;
import ch.mixin.mixedAchievements.blueprint.AchievementSetBlueprint;
import ch.mixin.mixedAchievements.main.MixedAchievementsPlugin;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

import javax.naming.ServiceUnavailableException;

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
                new AchievementItemSetup(Material.NETHER_STAR, ChatColor.of("#FFFFFF") + "Mixed Catastrophes", 1)
                , "MixedCatastrophes"
                , "Mixed Catastrophes");

        achievementSetBlueprint.getSubAchievementBlueprintElementMap().put(1, new AchievementBlueprintLeafElement(
                new AchievementItemSetup(Material.OAK_PLANKS, ChatColor.of("#7F00FF") + "Test", 4)
                , "test"
                , "test"
                , 4
        ));
        return achievementSetBlueprint;
    }

    public boolean isActive() {
        return active;
    }
}
