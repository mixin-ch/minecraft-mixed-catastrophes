package ch.mixin.catastropheManager.personal;

import ch.mixin.MetaData.PlayerData;
import ch.mixin.catastropheManager.CatastropheManager;
import ch.mixin.catastropheManager.RootCatastropheManager;
import ch.mixin.catastropheManager.personal.dream.DreamManager;
import ch.mixin.catastropheManager.personal.rite.RiteManager;
import ch.mixin.catastropheManager.personal.terror.TerrorCatastropheManager;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PersonalCatastropheManager extends CatastropheManager {
    private final TerrorCatastropheManager terrorCatastropheManager;
    private final RiteManager riteManager;
    private final DreamManager dreamManager;

    public PersonalCatastropheManager(MixedCatastrophesPlugin plugin, RootCatastropheManager rootCatastropheManager) {
        super(plugin, rootCatastropheManager);
        terrorCatastropheManager = new TerrorCatastropheManager(plugin, rootCatastropheManager);
        riteManager = new RiteManager(plugin);
        dreamManager = new DreamManager(plugin);
    }

    @Override
    public void initializeMetaData() {
        HashMap<UUID, PlayerData> playerDataMap = metaData.getPlayerDataMap();

        if (playerDataMap == null) {
            playerDataMap = new HashMap<>();
            metaData.setPlayerDataMap(playerDataMap);
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!playerDataMap.containsKey(player.getUniqueId())) {
                initializePlayerData(player);
            }
        }
    }

    public void initializePlayerData(Player player) {
        HashMap<UUID, PlayerData> playerDataMap = metaData.getPlayerDataMap();
        UUID playerId = player.getUniqueId();
        PlayerData playerData = playerDataMap.get(playerId);

        if (playerData == null) {
            playerData = new PlayerData(playerId);
            playerDataMap.put(playerId, playerData);
        }

        playerData.fillAspects();
        terrorCatastropheManager.initializePlayerData(playerData);
    }

    @Override
    public void updateMetaData() {
        terrorCatastropheManager.updateMetaData();
    }

    @Override
    public void initializeCauser() {
        terrorCatastropheManager.initializeCauser();
    }

    @Override
    public void tick() {
        HashMap<UUID, PlayerData> playerDataMap = metaData.getPlayerDataMap();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!plugin.getAffectedWorlds().contains(player.getWorld()))
                continue;

            if (!playerDataMap.containsKey(player.getUniqueId())) {
                initializePlayerData(player);
                plugin.getEventChangeManager().updateScoreBoard(player);
            }

            PlayerData playerData = playerDataMap.get(player.getUniqueId());
            playerData.setDreamCooldown(Math.max(0, playerData.getDreamCooldown() - 1));
        }

        terrorCatastropheManager.tick();
    }

    public TerrorCatastropheManager getTerrorCatastropheManager() {
        return terrorCatastropheManager;
    }

    public RiteManager getRiteManager() {
        return riteManager;
    }

    public DreamManager getDreamManager() {
        return dreamManager;
    }
}
