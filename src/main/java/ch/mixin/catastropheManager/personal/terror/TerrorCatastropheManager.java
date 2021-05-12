package ch.mixin.catastropheManager.personal.terror;

import ch.mixin.MetaData.LighthouseData;
import ch.mixin.MetaData.PlayerData;
import ch.mixin.MetaData.TerrorData;
import ch.mixin.catastropheManager.CatastropheManager;
import ch.mixin.catastropheManager.RootCatastropheManager;
import ch.mixin.catastropheManager.personal.terror.assault.AssaultCatastropheManager;
import ch.mixin.catastropheManager.personal.terror.paranoia.ParanoiaCatastropheManager;
import ch.mixin.catastropheManager.personal.terror.stalker.StalkerCatastropheManager;
import ch.mixin.eventChange.aspect.AspectType;
import ch.mixin.helperClasses.Constants;
import ch.mixin.helperClasses.Coordinate3D;
import ch.mixin.helperClasses.Functions;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class TerrorCatastropheManager extends CatastropheManager {
    private final AssaultCatastropheManager assaultCatastropheManager;
    private final StalkerCatastropheManager stalkerCatastropheManager;
    private final ParanoiaCatastropheManager paranoiaCatastropheManager;

    public TerrorCatastropheManager(MixedCatastrophesPlugin plugin, RootCatastropheManager rootCatastropheManager) {
        super(plugin, rootCatastropheManager);
        assaultCatastropheManager = new AssaultCatastropheManager(plugin, rootCatastropheManager);
        stalkerCatastropheManager = new StalkerCatastropheManager(plugin, rootCatastropheManager);
        paranoiaCatastropheManager = new ParanoiaCatastropheManager(plugin, rootCatastropheManager);
    }

    @Override
    @Deprecated
    public void initializeMetaData() {
    }

    public void initializePlayerData(PlayerData playerData) {
        TerrorData terrorData = playerData.getTerrorData();

        if (terrorData == null) {
            terrorData = new TerrorData();
            playerData.setTerrorData(terrorData);
        }

        if (terrorData.getTerrorTimer() <= 0) {
            terrorData.setTerrorTimer(terrorTimer());
        }

        assaultCatastropheManager.initializePlayerData(playerData);
        stalkerCatastropheManager.initializePlayerData(playerData);
        paranoiaCatastropheManager.initializePlayerData(playerData);
    }

    @Override
    public void updateMetaData() {
        assaultCatastropheManager.updateMetaData();
        stalkerCatastropheManager.updateMetaData();
        paranoiaCatastropheManager.updateMetaData();
    }

    @Override
    public void initializeCauser() {
        assaultCatastropheManager.initializeCauser();
        stalkerCatastropheManager.initializeCauser();
        paranoiaCatastropheManager.initializeCauser();
    }

    @Override
    public void tick() {
        HashMap<Location, Integer> lighthouseMap = new HashMap<>();

        for (LighthouseData lighthouseData : metaData.getLightHouseDataList()) {
            World lightHouseWorld = plugin.getServer().getWorld(lighthouseData.getWorldName());

            if (lightHouseWorld == null)
                continue;

            Location lighthouseLocation = lighthouseData.getPosition().toLocation(lightHouseWorld);

            if (!Constants.Lighthouse.isConstructed(lighthouseLocation))
                continue;

            lighthouseMap.put(lighthouseLocation, 10 * lighthouseData.getLevel());
        }

        playerLoop:
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!plugin.getAffectedWorlds().contains(player.getWorld()))
                continue;

            World world = player.getWorld();
            Location playerLocation = player.getLocation();

            for (Location lighthouseLocation : lighthouseMap.keySet()) {
                if (world != lighthouseLocation.getWorld())
                    continue;

                if (lighthouseLocation.distance(playerLocation) > lighthouseMap.get(lighthouseLocation))
                    continue;

                continue playerLoop;
            }

            TerrorData terrorData = metaData.getPlayerDataMap().get(player.getUniqueId()).getTerrorData();

            int timer = terrorData.getTerrorTimer();
            timer--;

            if (timer <= 0) {
                timer = terrorTimer();
                causeTerror(player);
            }

            terrorData.setTerrorTimer(timer);

            assaultCatastropheManager.tick(player);
            stalkerCatastropheManager.tick(player);
            paranoiaCatastropheManager.tick(player);
        }
    }

    private int terrorTimer() {
        return Functions.random(8 * 60, 12 * 60);
    }

    public void causeTerror(Player player) {
        PlayerData playerData = metaData.getPlayerDataMap().get(player.getUniqueId());

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Terror, 8 + new Random().nextInt(4 + 1));
        changeMap.put(AspectType.Secrets, 20 + playerData.getAspect(AspectType.Terror));

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The horrific Whispers grow louder.")
                .withColor(Constants.AspectThemes.get(AspectType.Terror))
                .withTitle(true)
                .finish()
                .execute();

        showHint(player);
    }

    private void showHint(Player player) {
        ArrayList<String> hintList = new ArrayList<>(plugin.getConfig().getStringList("hintList"));
        String hint = hintList.get(new Random().nextInt(hintList.size()));

        plugin.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("They Whisper: " + hint)
                .withColor(Constants.AspectThemes.get(AspectType.Terror))
                .finish()
                .execute();
    }

    public AssaultCatastropheManager getAssaultCatastropheManager() {
        return assaultCatastropheManager;
    }

    public StalkerCatastropheManager getStalkerCatastropheManager() {
        return stalkerCatastropheManager;
    }

    public ParanoiaCatastropheManager getParanoiaCatastropheManager() {
        return paranoiaCatastropheManager;
    }
}
