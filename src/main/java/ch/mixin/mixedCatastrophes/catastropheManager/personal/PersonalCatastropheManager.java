package ch.mixin.mixedCatastrophes.catastropheManager.personal;

import ch.mixin.mixedCatastrophes.metaData.constructs.LighthouseData;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.dream.DreamManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.rite.RiteManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.TerrorCatastropheManager;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        HashMap<Location, Integer> lighthouseMap = new HashMap<>();

        for (LighthouseData lighthouseData : metaData.getLighthouseDataList()) {
            World lightHouseWorld = plugin.getServer().getWorld(lighthouseData.getWorldName());

            if (lightHouseWorld == null)
                continue;

            Location lighthouseLocation = lighthouseData.getPosition().toLocation(lightHouseWorld);

            if (!Constants.Lighthouse.checkConstructed(lighthouseLocation).isConstructed())
                continue;

            lighthouseMap.put(lighthouseLocation, 10 * lighthouseData.getLevel());
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!plugin.getAffectedWorlds().contains(player.getWorld()))
                continue;

            if (!playerDataMap.containsKey(player.getUniqueId())) {
                initializePlayerData(player);
                plugin.getEventChangeManager().updateScoreBoard(player);
            }

            PlayerData playerData = playerDataMap.get(player.getUniqueId());
            playerData.setDreamCooldown(Math.max(0, playerData.getDreamCooldown() - 1));
            playerData.setAntiLighthouseTimer(Math.max(0, playerData.getAntiLighthouseTimer() - 1));

            if (playerData.getAntiLighthouseTimer() > 0) {
                if (playerData.getAntiLighthouseTimer() % 10 != 0)
                    continue;

                for (Location lighthouseLocation : lighthouseMap.keySet()) {
                    if (player.getWorld() != lighthouseLocation.getWorld())
                        continue;

                    if (lighthouseLocation.distance(player.getLocation()) > lighthouseMap.get(lighthouseLocation))
                        continue;

                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10 * 20, 0));

                    plugin.getEventChangeManager()
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
