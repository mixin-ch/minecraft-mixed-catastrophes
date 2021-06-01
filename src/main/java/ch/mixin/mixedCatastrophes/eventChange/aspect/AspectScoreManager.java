package ch.mixin.mixedCatastrophes.eventChange.aspect;

import ch.mixin.mixedCatastrophes.catastropheManager.personal.dream.DreamType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.UUID;

public class AspectScoreManager {
    private final HashMap<UUID, Scoreboard> playerMap = new HashMap<>();

    private final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
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

        Scoreboard scoreboard = playerMap.get(playerId);

        if (scoreboard == null) {
            scoreboard = scoreboardManager.getNewScoreboard();
            playerMap.put(playerId, scoreboard);
        }

        Objective objective = scoreboard.getObjective("Aspects");

        if (objective == null) {
            objective = scoreboard.registerNewObjective("Aspects", "dummy", ChatColor.GRAY + "Aspects");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        int index = 0;
        int maxBoardSize = Constants.AspectOrder.size() + 2;

        for (AspectType aspectType : Constants.AspectOrder) {
            int value = aspects.get(aspectType);

            if (value > 0) {
                makeScore(scoreboard, objective, index, Constants.AspectThemes.get(aspectType).getColor(), aspectType.getLabel(), value);
                index++;
            }
        }

        if (dreamCooldown > 0) {
            makeScore(scoreboard, objective, index, Constants.DreamThemes.get(DreamType.SereneDreams).getColor(), "Dreamless", dreamCooldown, "s");
            index++;
        }

        if (antiLighthouseTimer > 0) {
            makeScore(scoreboard, objective, index, Constants.AspectThemes.get(AspectType.Terror).getColor(), "Red Eye", antiLighthouseTimer, "s");
            index++;
        }

        while (index < maxBoardSize) {
            resetScore(scoreboard, index);
            index++;
        }

        player.setScoreboard(scoreboard);
    }

    private void makeScore(Scoreboard scoreboard, Objective objective, int index, ChatColor chatColor, String label, int value) {
        makeScore(scoreboard, objective, index, chatColor, label, value, "");
    }

    private void makeScore(Scoreboard scoreboard, Objective objective, int index, ChatColor chatColor, String label, int value, String valueUnit) {
        String teamIndexString = Integer.toString(index);
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
