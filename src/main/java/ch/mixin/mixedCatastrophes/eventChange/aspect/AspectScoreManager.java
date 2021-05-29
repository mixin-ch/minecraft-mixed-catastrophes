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

        int index = 0;

        for (AspectType aspectType : aspectOrder) {
            int value = aspects.get(aspectType);

            if (value > 0) {
                makeScore(scoreboard, objective, index, Constants.AspectThemes.get(aspectType).getColor(), aspectType.getLabel(), value);
                index++;
            }
        }

        boolean hasTimers = dreamCooldown > 0 || antiLighthouseTimer > 0;

        if (hasTimers) {
            emptyScore(objective, index);
            index++;
        }

        if (dreamCooldown > 0) {
            makeScore(scoreboard, objective, index, Constants.DreamThemes.get(DreamType.SereneDreams).getColor(), "Dreamless", dreamCooldown, "s");
            index++;
        }

        if (antiLighthouseTimer > 0) {
            makeScore(scoreboard, objective, index, Constants.AspectThemes.get(AspectType.Terror).getColor(), "Red Eye", antiLighthouseTimer, "s");
            index++;
        }

        player.setScoreboard(scoreboard);
    }

    private void makeScore(Scoreboard scoreboard, Objective objective, int index, ChatColor chatColor, String label, int value) {
        makeScore(scoreboard, objective, index, chatColor, label, value, "");
    }

    private void makeScore(Scoreboard scoreboard, Objective objective, int index, ChatColor chatColor, String label, int value, String valueUnit) {
        String indexString = Integer.toString(index);
        Team team = scoreboard.registerNewTeam(indexString);
        team.setSuffix(chatColor + label + ": " + value + valueUnit);
        StringBuilder scoreString = new StringBuilder();

        for (char c : indexString.toCharArray()) {
            scoreString.append(ChatColor.translateAlternateColorCodes('&', "&" + c));
        }

        team.addEntry(scoreString.toString());
        objective.getScore(scoreString.toString()).setScore(0);
    }

    private void emptyScore(Objective objective, int index) {
        StringBuilder scoreString = new StringBuilder();

        for (char c : Integer.toString(index).toCharArray()) {
            scoreString.append(ChatColor.translateAlternateColorCodes('&', "&" + c));
        }

        Score score = objective.getScore(scoreString.toString());
        score.setScore(0);
    }
}
