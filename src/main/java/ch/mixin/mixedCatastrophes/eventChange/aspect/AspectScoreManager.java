package ch.mixin.mixedCatastrophes.eventChange.aspect;

import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
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

        int dreamCooldown = pcd.getDreamCooldown();
        int antiLighthouseTimer = pcd.getAntiLighthouseTimer();

        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Aspects", "dummy", ChatColor.GRAY + "Aspects");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int hierarchy = 2 + 1 + aspects.size();
        boolean hasTimers = false;

        if (dreamCooldown > 0) {
            makeScore(objective, hierarchy, ChatColor.DARK_AQUA, "Dreamless", dreamCooldown, "s");
            hierarchy--;
            hasTimers = true;
        }

        if (antiLighthouseTimer > 0) {
            makeScore(objective, hierarchy, Constants.AspectThemes.get(AspectType.Terror), "Red Eye", antiLighthouseTimer, "s");
            hierarchy--;
            hasTimers = true;
        }

        if (hasTimers) {
            emptyScore(objective, hierarchy);
            hierarchy--;
        }

        for (AspectType aspectType : AspectType.values()) {
            int value = aspects.get(aspectType);

            if (value > 0) {
                makeScore(objective, hierarchy, Constants.AspectThemes.get(aspectType), aspectType.getLabel(), value);
                hierarchy--;
            }
        }

        player.setScoreboard(scoreboard);
    }

    private void makeScore(Objective objective, int hierarchy, ChatColor chatColor, String label, int value) {
        makeScore(objective, hierarchy, chatColor, label, value, "");
    }

    private void makeScore(Objective objective, int hierarchy, ChatColor chatColor, String label, int value, String valueUnit) {
        String text = chatColor + label + ": " + value + valueUnit;
        Score score = objective.getScore(text);
        score.setScore(hierarchy);
    }

    private void emptyScore(Objective objective, int hierarchy) {
        Score score = objective.getScore("");
        score.setScore(hierarchy);
    }
}
