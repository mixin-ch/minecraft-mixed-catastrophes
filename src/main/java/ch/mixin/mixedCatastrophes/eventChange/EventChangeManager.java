package ch.mixin.mixedCatastrophes.eventChange;


import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectChange;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectScoreManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.eventChange.message.EventMessage;
import ch.mixin.mixedCatastrophes.eventChange.message.Messager;
import ch.mixin.mixedCatastrophes.eventChange.sound.EventSound;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.mixedAchievements.MixedAchievementsManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class EventChangeManager {
    private final MixedCatastrophesPlugin plugin;
    private final AspectScoreManager aspectScoreManager;
    private MixedAchievementsManager mixedAchievementsManager;
    private final Messager messager;

    public EventChangeManager(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
        aspectScoreManager = new AspectScoreManager(plugin);
        messager = new Messager();
    }

    public void configureMixedAchievements(MixedAchievementsManager mixedAchievementsManager) {
        this.mixedAchievementsManager = mixedAchievementsManager;
    }

    public void execute(EventChange eventChange) {
        AspectChange aspectChange = eventChange.getAspectChange();
        EventMessage eventMessage = eventChange.getEventMessage();
        EventSound eventSound = eventChange.getEventSound();

        if (eventMessage != null) {
            HashMap<AspectType, Integer> changeMap;

            if (aspectChange != null) {
                changeMap = aspectChange.getChangeMap();
            } else {
                changeMap = new HashMap<>();
            }

            eventMessage(eventChange.getPlayer(), eventMessage, changeMap);
        }

        if (eventSound != null) {
            eventSound(eventChange.getPlayer(), eventSound);
        }

        if (aspectChange != null) {
            aspectChange(eventChange.getPlayer(), aspectChange);
        }
    }

    private void aspectChange(Player player, AspectChange aspectChange) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        for (AspectType aspectType : aspectChange.getChangeMap().keySet()) {
            playerData.addAspect(aspectType, aspectChange.getChangeMap().get(aspectType));
        }

        aspectScoreManager.updateScoreboard(player);
        updateAchievementProgress(player, aspectChange.getChangeMap());
    }

    private void updateAchievementProgress(Player player, HashMap<AspectType, Integer> changeMap) {
        if (mixedAchievementsManager == null)
            return;

        HashMap<AspectType, Integer> aspects = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId()).getAspects();
        mixedAchievementsManager.updateAchievementProgress(player, aspects, changeMap);
    }

    public void updateAchievementProgress(Player player) {
        if (mixedAchievementsManager == null)
            return;

        updateAchievementProgress(player, new HashMap<>());
    }

    public void updateAchievementProgress() {
        if (mixedAchievementsManager == null)
            return;

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            updateAchievementProgress(player);
        }
    }

    private void eventMessage(Player player, EventMessage eventMessage, HashMap<AspectType, Integer> changeMap) {
        String text = eventMessage.getText();
        AspectType cause = eventMessage.getCause();
        ChatColor color = eventMessage.getColor();

        if (color == null) {
            if (cause != null) {
                color = Constants.AspectThemes.get(eventMessage.getCause()).getColor();
            } else {
                color = ChatColor.WHITE;
            }
        }

        if (eventMessage.isTitle()) {
            messager.sendCatastropheTitle(player, text, color, cause, changeMap);
        } else {
            messager.sendCatastropheMessage(player, text, color, cause, changeMap);
        }
    }

    private void eventSound(Player player, EventSound eventSound) {
        player.playSound(player.getLocation(), eventSound.getSound(), 10.0f, 1.0f);
    }

    public EventChange eventChange(Player player) {
        return new EventChange(this, player);
    }

    public void updateScoreBoard() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            aspectScoreManager.updateScoreboard(player);
        }
    }

    public void updateScoreBoard(Player player) {
        aspectScoreManager.updateScoreboard(player);
    }
}
