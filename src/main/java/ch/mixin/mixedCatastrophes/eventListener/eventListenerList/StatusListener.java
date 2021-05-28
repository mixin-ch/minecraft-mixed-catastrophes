package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;

public class StatusListener implements Listener {
    protected final MixedCatastrophesPlugin plugin;

    public StatusListener(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        RootCatastropheManager rootCatastropheManager = plugin.getRootCatastropheManager();
        rootCatastropheManager.getPersonalCatastropheManager().initializePlayerData(player);
        plugin.getEventChangeManager().updateScoreBoard(player);

        if (!event.getPlayer().hasPlayedBefore()) {
            player.getInventory().addItem(HelpInventoryManager.HelpBookItem);
        }
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.getInventory().addItem(HelpInventoryManager.HelpBookItem);
    }

    @EventHandler
    public void death(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!plugin.getAffectedWorlds().contains(player.getWorld()))
            return;

        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Celestial_Favor) > 0) {
            saveEssence(event);
        } else {
            loseEssence(event);
        }
    }

    private void loseEssence(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        playerData.getTerrorData().getStalkerDatas().clear();
        playerData.setDreamCooldown(0);
        playerData.setAntiLighthouseTimer(0);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Death_Seeker, 1);
        changeMap.put(AspectType.Secrets, 100);
        changeMap.put(AspectType.Terror, -(int) Math.floor(0.5 * playerData.getAspect(AspectType.Terror)));

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You lose yourself in the grand Emptiness.")
                .withColor(Constants.AspectThemes.get(AspectType.Death_Seeker).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }

    private void saveEssence(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        playerData.getTerrorData().getStalkerDatas().clear();
        playerData.setDreamCooldown(0);
        playerData.setAntiLighthouseTimer(0);

        event.setKeepInventory(true);
        event.setKeepLevel(true);
        event.getDrops().clear();

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Celestial_Favor, -1);
        changeMap.put(AspectType.Terror, -(int) Math.floor(0.2 * playerData.getAspect(AspectType.Terror)));

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Sky keeps your Essence safe.")
                .withCause(AspectType.Celestial_Favor)
                .withTitle(true)
                .finish()
                .execute();
    }
}
