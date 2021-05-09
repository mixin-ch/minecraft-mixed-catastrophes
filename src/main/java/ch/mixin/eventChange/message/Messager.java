package ch.mixin.eventChange.message;

import ch.mixin.eventChange.aspect.AspectType;
import ch.mixin.helperClasses.Constants;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Messager {
    public void sendCatastropheMessage(Player player, String text, ChatColor chatColor, AspectType cause, HashMap<AspectType, Integer> changeMap) {
        if (cause != null) {
            text = cause.getLabel() + ": " + text;
        }

        player.sendMessage(formatCatastropheMessage(text, chatColor, changeMap));
    }

    public void sendCatastropheTitle(Player player, String text, ChatColor chatColor, AspectType cause, HashMap<AspectType, Integer> changeMap) {
        player.sendTitle("", formatCatastropheMessage(text, chatColor), 10, 5 * 20, 10);
        sendCatastropheMessage(player, text, chatColor, cause, changeMap);
    }

    private String formatCatastropheMessage(String text, ChatColor chatColor) {
        return chatColor + "" + ChatColor.MAGIC + "x " + ChatColor.RESET + chatColor + text + ChatColor.MAGIC + " x";
    }

    private String formatCatastropheMessage(String text, ChatColor chatColor, HashMap<AspectType, Integer> changeMap) {
        String changeText = "";

        if (changeMap.size() > 0) {
            ArrayList<String> changes = new ArrayList<>();

            for (AspectType aspectType : changeMap.keySet()) {
                int value = changeMap.get(aspectType);
                ChatColor color = Constants.AspectThemes.get(aspectType);
                changes.add(color + (value >= 0 ? "+" : "") + value + " " + aspectType.getLabel());
            }

            changeText = String.join(chatColor + ", ", changes);
        }

        return chatColor + "" + ChatColor.MAGIC + "x " + ChatColor.RESET + chatColor + text + ChatColor.MAGIC + " x\n" + changeText;
    }
}
