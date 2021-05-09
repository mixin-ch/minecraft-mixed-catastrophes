package ch.mixin.eventChange.aspect;

import ch.mixin.MetaData.PlayerData;
import ch.mixin.eventChange.aspect.AspectType;
import ch.mixin.helperClasses.Constants;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;

public class AspectScoreManager {
    private final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private final MixedCatastrophesPlugin plugin;

    public AspectScoreManager(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    public void updateScoreboard(Player player) {
        PlayerData pcd = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        HashMap<AspectType, Integer> aspects = pcd.getAspects();

        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Aspects", "dummy", ChatColor.GRAY + "Aspects");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int hierarchy = aspects.size();

        for (AspectType aspectType : AspectType.values()) {
            int value = aspects.get(aspectType);

            if (value > 0) {
                makeScore(objective, hierarchy, Constants.AspectThemes.get(aspectType), aspectType.getLabel(), value);
                hierarchy--;
            }
        }

        player.setScoreboard(scoreboard);
    }

    private static void makeScore(Objective objective, int hierarchy, ChatColor chatColor, String label, int value) {
        if (value == 0)
            return;

        String text = chatColor + label + ": " + value;
        Score score = objective.getScore(text);
        score.setScore(hierarchy);
    }
}
