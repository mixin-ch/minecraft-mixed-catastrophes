package ch.mixin.mixedCatastrophes.catastropheManager.personal.terror;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.assault.AssaultCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.paranoia.ParanoiaCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.stalker.StalkerCatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.TerrorData;
import ch.mixin.mixedCatastrophes.metaData.constructs.LighthouseData;
import ch.mixin.mixedCatastrophes.metaData.constructs.ScarecrowData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
        HashMap<ScarecrowData, Location> scarecrowMap = makeActiveScarecrowMap();

        for (LighthouseData lighthouseData : metaData.getLighthouseDataList()) {
            World lightHouseWorld = plugin.getServer().getWorld(lighthouseData.getWorldName());

            if (lightHouseWorld == null)
                continue;

            Location lighthouseLocation = lighthouseData.getPosition().toLocation(lightHouseWorld);

            if (!Constants.Lighthouse.checkConstructed(lighthouseLocation).isConstructed())
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
                ScarecrowData strongestScarecrow = getStrongestScarecrow(scarecrowMap, player);
                executeHorrificWhispers(player, strongestScarecrow);
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

    public void triggerHorrificWhispers(Player player) {
        executeHorrificWhispers(player, getStrongestScarecrow(makeActiveScarecrowMap(), player));
    }

    private void executeHorrificWhispers(Player player, ScarecrowData scarecrowData) {
        PlayerData playerData = metaData.getPlayerDataMap().get(player.getUniqueId());

        int terrorPlus = 8 + new Random().nextInt(4 + 1);
        int secretsPlus = 20 + playerData.getAspect(AspectType.Terror);
        String text = "The horrific Whispers grow louder.";
        ChatColor color = Constants.AspectThemes.get(AspectType.Terror).getColor();

        if (scarecrowData != null) {
            int playerTerror = playerData.getAspect(AspectType.Terror);
            int lackingTerror = scarecrowData.getCollectedTerror() - playerTerror;
            int scarecrowExtra = (int) Math.max(0, Math.floor(lackingTerror * 0.2));
            terrorPlus += 10 + scarecrowExtra;
            secretsPlus += 10 + scarecrowExtra;
            text = "The Screams of the Scarecrow grow louder.";
            color = Constants.ConstructThemes.get(ConstructType.Scarecrow).getColor();
            int collectedTerror = (int) Math.ceil(0.9 * scarecrowData.getCollectedTerror() + 0.1 * playerData.getAspect(AspectType.Terror));
            scarecrowData.setCollectedTerror(collectedTerror);
        }

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Terror, terrorPlus);
        changeMap.put(AspectType.Secrets, secretsPlus);

        plugin.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage(text)
                .withColor(color)
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
                .withColor(Constants.AspectThemes.get(AspectType.Terror).getColor())
                .finish()
                .execute();
    }

    private HashMap<ScarecrowData, Location> makeActiveScarecrowMap() {
        HashMap<ScarecrowData, Location> scarecrowMap = new HashMap<>();

        for (ScarecrowData scarecrowData : metaData.getScarecrowDataList()) {
            World scarecrowWorld = plugin.getServer().getWorld(scarecrowData.getWorldName());

            if (scarecrowWorld == null)
                continue;

            Location scarecrowLocation = scarecrowData.getPosition().toLocation(scarecrowWorld);

            if (!Constants.Scarecrow.checkConstructed(scarecrowLocation).isConstructed())
                continue;

            scarecrowMap.put(scarecrowData, scarecrowLocation);
        }

        return scarecrowMap;
    }

    private ScarecrowData getStrongestScarecrow(HashMap<ScarecrowData, Location> scarecrowMap, Player player) {
        ScarecrowData strongestScarecrow = null;
        double strongestPull = -1;

        for (ScarecrowData scarecrowData : scarecrowMap.keySet()) {
            Location scarecrowLocation = scarecrowMap.get(scarecrowData);

            if (player.getWorld() != scarecrowLocation.getWorld())
                continue;

            double pull = 50 - scarecrowLocation.distance(player.getLocation());

            if (pull < 0)
                continue;

            if (pull <= strongestPull)
                continue;

            strongestPull = pull;
            strongestScarecrow = scarecrowData;
        }

        return strongestScarecrow;
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
