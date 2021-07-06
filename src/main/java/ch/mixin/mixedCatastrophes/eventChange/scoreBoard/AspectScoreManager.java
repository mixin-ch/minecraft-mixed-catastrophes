package ch.mixin.mixedCatastrophes.eventChange.scoreBoard;

import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.dream.DreamType;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.MetaData;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.constructs.BlitzardData;
import ch.mixin.mixedCatastrophes.metaData.constructs.LighthouseData;
import ch.mixin.mixedCatastrophes.metaData.constructs.ScarecrowData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AspectScoreManager {
    private final HashMap<UUID, Scoreboard> playerMap = new HashMap<>();

    private final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private final MixedCatastrophesData mixedCatastrophesData;

    public AspectScoreManager(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    public void updateScoreboard(Player player) {
        MetaData metaData = mixedCatastrophesData.getMetaData();
        ConstructManager constructManager = mixedCatastrophesData.getRootCatastropheManager().getConstructManager();

        List<BlitzardData> blitzardDataList = constructManager.getBlitzardListIsConstructed(metaData.getBlitzardDataList());
        List<LighthouseData> lighthouseDataList = constructManager.getLighthouseListIsConstructed(metaData.getLighthouseDataList());
        List<ScarecrowData> scarecrowDataList = constructManager.getScarecrowListIsConstructed(metaData.getScarecrowDataList());

        updateScoreboard(player, blitzardDataList, lighthouseDataList, scarecrowDataList);
    }

    public void updateScoreboard(Player player, List<BlitzardData> blitzardDataList, List<LighthouseData> lighthouseDataList, List<ScarecrowData> scarecrowDataList) {
        UUID playerId = player.getUniqueId();
        PlayerData pcd = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(playerId);
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
        int maxBoardSize = Constants.AspectOrder.size() + 2 + 3 + 1;

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

        ConstructManager constructManager = mixedCatastrophesData.getRootCatastropheManager().getConstructManager();
        BlitzardData blitzardData = constructManager.getStrongestBlitzard(blitzardDataList, player.getLocation());
        LighthouseData lighthouseData = constructManager.getStrongestLighthouse(lighthouseDataList, player.getLocation());
        ScarecrowData scarecrowData = constructManager.getStrongestScarecrow(scarecrowDataList, player.getLocation());
        Location playerLocation = player.getLocation();
        World world = playerLocation.getWorld();

        if (blitzardData != null) {
            int distance = (int) blitzardData.getPosition().toLocation(world).distance(player.getLocation());
            makeScore(scoreboard, objective, index, Constants.ConstructThemes.get(ConstructType.Blitzard).getColor(), "Blitzard", distance, "m");
            index++;
        }

        if (lighthouseData != null) {
            int distance = (int) lighthouseData.getPosition().toLocation(world).distance(player.getLocation());
            makeScore(scoreboard, objective, index, Constants.ConstructThemes.get(ConstructType.Lighthouse).getColor(), "Lighthouse", distance, "m");
            index++;
        }

        if (scarecrowData != null) {
            int distance = (int) scarecrowData.getPosition().toLocation(world).distance(player.getLocation());
            makeScore(scoreboard, objective, index, Constants.ConstructThemes.get(ConstructType.Scarecrow).getColor(), "Scarecrow", distance, "m");
            index++;
        }

        WeatherCatastropheManager weatherCatastropheManager = mixedCatastrophesData.getRootCatastropheManager().getWeatherCatastropheManager();
        WeatherCatastropheType activeWeather = weatherCatastropheManager.getActiveWeather();

        if (activeWeather != WeatherCatastropheType.Nothing) {
            makeScore(scoreboard, objective, index, Constants.WeatherThemes.get(activeWeather).getColor(), Functions.splitCamelCase(activeWeather.toString()), weatherCatastropheManager.getWeatherTimer(), "s");
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
