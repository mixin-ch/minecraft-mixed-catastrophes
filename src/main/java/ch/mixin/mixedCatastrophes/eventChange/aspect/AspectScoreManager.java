package ch.mixin.mixedCatastrophes.eventChange.aspect;

import ch.mixin.mixedCatastrophes.catastropheManager.personal.dream.DreamType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AspectScoreManager {
    private static final List<AspectType> aspectOrder;

    static {
        aspectOrder = new ArrayList<>();
        aspectOrder.add(AspectType.Secrets);
        aspectOrder.add(AspectType.Terror);
        aspectOrder.add(AspectType.Misfortune);
        aspectOrder.add(AspectType.Nature_Conspiracy);
        aspectOrder.add(AspectType.Celestial_Favor);
        aspectOrder.add(AspectType.Death_Seeker);
        aspectOrder.add(AspectType.Greyhat_Debt);
    }

//    private final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private final MixedCatastrophesPlugin plugin;

    public AspectScoreManager(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    public void updateScoreboard(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerData pcd = plugin.getMetaData().getPlayerDataMap().get(playerId);
        HashMap<AspectType, Integer> aspects = pcd.getAspects();

        int dreamCooldown = pcd.getDreamCooldown();
        int antiLighthouseTimer = pcd.getAntiLighthouseTimer();

        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("Aspects");

        if (objective == null) {
            objective = scoreboard.registerNewObjective("Aspects", "dummy", ChatColor.GRAY + "Aspects");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        int index = 0;
        int maxBoardSize = aspectOrder.size() + 2;

        for (AspectType aspectType : aspectOrder) {
            int value = aspects.get(aspectType);

            if (value > 0) {
                makeScore(scoreboard, objective, playerId, index, Constants.AspectThemes.get(aspectType).getColor(), aspectType.getLabel(), value);
                index++;
            }
        }

        if (dreamCooldown > 0) {
            makeScore(scoreboard, objective, playerId, index, Constants.DreamThemes.get(DreamType.SereneDreams).getColor(), "Dreamless", dreamCooldown, "s");
            index++;
        }

        if (antiLighthouseTimer > 0) {
            makeScore(scoreboard, objective, playerId, index, Constants.AspectThemes.get(AspectType.Terror).getColor(), "Red Eye", antiLighthouseTimer, "s");
            index++;
        }

        while (index < maxBoardSize) {
            resetScore(scoreboard, index);
            index++;
        }

        player.setScoreboard(scoreboard);
    }

    private void makeScore(Scoreboard scoreboard, Objective objective, UUID playerId, int index, ChatColor chatColor, String label, int value) {
        makeScore(scoreboard, objective, playerId, index, chatColor, label, value, "");
    }

    private void makeScore(Scoreboard scoreboard, Objective objective, UUID playerId, int index, ChatColor chatColor, String label, int value, String valueUnit) {
        String teamIndexString = playerId + Integer.toString(index);
        String scoreString = getScoreString(index);
        Team team = scoreboard.getTeam(teamIndexString);

        if (team == null) {
            team = scoreboard.registerNewTeam(teamIndexString);
            team.addEntry(scoreString);
        }

        team.setSuffix(chatColor + label + ": " + value + valueUnit);
        objective.getScore(scoreString).setScore(0);
    }

    private void resetScore(Scoreboard scoreboard, int index) {
        scoreboard.resetScores(getScoreString(index));
    }

    private String getScoreString(int index) {
        StringBuilder scoreString = new StringBuilder();

        for (char c : Integer.toString(index).toCharArray()) {
            scoreString.append(ChatColor.translateAlternateColorCodes('&', "&" + c));
        }

        return scoreString.toString();
    }

}
