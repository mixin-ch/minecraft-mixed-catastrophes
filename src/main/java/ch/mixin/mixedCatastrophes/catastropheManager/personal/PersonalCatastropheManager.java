package ch.mixin.mixedCatastrophes.catastropheManager.personal;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.dream.DreamManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.resolve.ResolveCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.rite.RiteManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.skyScorn.SkyScornCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.TerrorCatastropheManager;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.LighthouseData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PersonalCatastropheManager extends CatastropheManager {
    private final TerrorCatastropheManager terrorCatastropheManager;
    private final ResolveCatastropheManager resolveCatastropheManager;
    private final SkyScornCatastropheManager skyScornCatastropheManager;
    private final RiteManager riteManager;
    private final DreamManager dreamManager;

    public PersonalCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
        terrorCatastropheManager = new TerrorCatastropheManager(mixedCatastrophesData);
        resolveCatastropheManager = new ResolveCatastropheManager(mixedCatastrophesData);
        skyScornCatastropheManager = new SkyScornCatastropheManager(mixedCatastrophesData);
        riteManager = new RiteManager(mixedCatastrophesData);
        dreamManager = new DreamManager(mixedCatastrophesData);
    }

    @Override
    public void initializeMetaData() {
        HashMap<UUID, PlayerData> playerDataMap = mixedCatastrophesData.getMetaData().getPlayerDataMap();

        if (playerDataMap == null) {
            playerDataMap = new HashMap<>();
            mixedCatastrophesData.getMetaData().setPlayerDataMap(playerDataMap);
        }

        for (Player player : mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers()) {
            if (!playerDataMap.containsKey(player.getUniqueId())) {
                initializePlayerData(player);
            } else {
                PlayerData playerData = playerDataMap.get(player.getUniqueId());
                playerData.setPlayerName(player.getName());
            }
        }
    }

    public void initializePlayerData(Player player) {
        HashMap<UUID, PlayerData> playerDataMap = mixedCatastrophesData.getMetaData().getPlayerDataMap();
        UUID playerId = player.getUniqueId();
        PlayerData playerData = playerDataMap.get(playerId);

        if (playerData == null) {
            playerData = new PlayerData(playerId, player.getName());
            playerDataMap.put(playerId, playerData);
        }

        playerData.fillAspects();
        terrorCatastropheManager.initializePlayerData(playerData);
        skyScornCatastropheManager.initializePlayerData(playerData);
    }

    @Override
    public void updateMetaData() {
        terrorCatastropheManager.updateMetaData();
        resolveCatastropheManager.updateMetaData();
        skyScornCatastropheManager.updateMetaData();
    }

    @Override
    public void initializeCauser() {
        terrorCatastropheManager.initializeCauser();
        resolveCatastropheManager.initializeCauser();
        skyScornCatastropheManager.initializeCauser();
    }

    @Override
    public void tick() {
        HashMap<UUID, PlayerData> playerDataMap = mixedCatastrophesData.getMetaData().getPlayerDataMap();

        List<LighthouseData> lighthouseList = mixedCatastrophesData.getRootCatastropheManager().getConstructManager()
                .getLighthouseListIsConstructed(mixedCatastrophesData.getMetaData().getLighthouseDataList());

        for (Player player : mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers()) {
            if (!mixedCatastrophesData.getAffectedWorlds().contains(player.getWorld()))
                continue;

            if (!playerDataMap.containsKey(player.getUniqueId())) {
                initializePlayerData(player);
                mixedCatastrophesData.getEventChangeManager().updateScoreBoard(player);
            }

            PlayerData playerData = playerDataMap.get(player.getUniqueId());
            playerData.setDreamCooldown(Math.max(0, playerData.getDreamCooldown() - 1));
            playerData.setAntiLighthouseTimer(Math.max(0, playerData.getAntiLighthouseTimer() - 1));

            if (playerData.getAntiLighthouseTimer() > 0) {
                if (playerData.getAntiLighthouseTimer() % 10 != 0)
                    continue;

                for (LighthouseData lighthouseData : lighthouseList) {
                    World world = mixedCatastrophesData.getPlugin().getServer().getWorld(lighthouseData.getWorldName());

                    if (player.getWorld() != world)
                        continue;

                    Location lighthouseLocation = lighthouseData.getPosition().toLocation(world);

                    if (lighthouseLocation.distance(player.getLocation()) > lighthouseData.getLevel() * Constants.LighthouseRangeFactor)
                        continue;

                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10 * 20, 0));

                    mixedCatastrophesData.getEventChangeManager()
                            .eventChange(player)
                            .withEventSound(Sound.ENTITY_BLAZE_AMBIENT)
                            .withEventMessage("The Lighthouse burns the Red Eye in your Mind.")
                            .withColor(ChatColor.GOLD)
                            .withTitle(true)
                            .finish()
                            .execute();
                }
            }
        }

        resolveCatastropheManager.tick();
        terrorCatastropheManager.tick();
        skyScornCatastropheManager.tick();
    }

    public TerrorCatastropheManager getTerrorCatastropheManager() {
        return terrorCatastropheManager;
    }

    public ResolveCatastropheManager getResolveCatastropheManager() {
        return resolveCatastropheManager;
    }

    public SkyScornCatastropheManager getSkyScornCatastropheManager() {
        return skyScornCatastropheManager;
    }

    public RiteManager getRiteManager() {
        return riteManager;
    }

    public DreamManager getDreamManager() {
        return dreamManager;
    }
}
