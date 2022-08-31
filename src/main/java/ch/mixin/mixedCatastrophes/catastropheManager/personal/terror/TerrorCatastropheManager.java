package ch.mixin.mixedCatastrophes.catastropheManager.personal.terror;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.assault.AssaultCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.paranoia.ParanoiaCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.stalker.StalkerCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheSettings.aspect.terror.WhispersCatastropheSettings;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.data.TerrorData;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.LighthouseData;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.ScarecrowData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TerrorCatastropheManager extends CatastropheManager {
    private final AssaultCatastropheManager assaultCatastropheManager;
    private final StalkerCatastropheManager stalkerCatastropheManager;
    private final ParanoiaCatastropheManager paranoiaCatastropheManager;

    public TerrorCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
        assaultCatastropheManager = new AssaultCatastropheManager(mixedCatastrophesData);
        stalkerCatastropheManager = new StalkerCatastropheManager(mixedCatastrophesData);
        paranoiaCatastropheManager = new ParanoiaCatastropheManager(mixedCatastrophesData);
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
        List<LighthouseData> lighthouseList = getActiveLighthouseList();
        List<ScarecrowData> scarecrowList = getActiveScarecrowList();

        boolean isCromsonSeason = mixedCatastrophesData.getRootCatastropheManager().getWeatherCatastropheManager().getActiveWeather() == WeatherCatastropheType.CrimsonSeason;
        WhispersCatastropheSettings whispersSettings = mixedCatastrophesData.getCatastropheSettings().getAspect().getTerror().getWhispers();

        playerLoop:
        for (Player player : mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers()) {
            if (!mixedCatastrophesData.getAffectedWorlds().contains(player.getWorld()))
                continue;

            World world = player.getWorld();
            Location playerLocation = player.getLocation();

            for (LighthouseData lighthouseData : lighthouseList) {
                World lightHouseWorld = mixedCatastrophesData.getPlugin().getServer().getWorld(lighthouseData.getWorldName());

                if (world != lightHouseWorld)
                    continue;

                Location lighthouseLocation = lighthouseData.getPosition().toLocation(world);

                if (lighthouseLocation.distance(playerLocation) > lighthouseData.getLevel() * Constants.LighthouseRangeFactor)
                    continue;

                continue playerLoop;
            }

            TerrorData terrorData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId()).getTerrorData();
            ScarecrowData strongestScarecrow = mixedCatastrophesData.getRootCatastropheManager().getConstructManager().getStrongestScarecrow(scarecrowList, playerLocation);
            boolean hasScareCrow = strongestScarecrow != null;

            if (whispersSettings.isActive()) {
                int timer = terrorData.getTerrorTimer();
                timer--;

                if (hasScareCrow)
                    timer -= 2;

                if (isCromsonSeason)
                    timer -= 2;

                if (timer <= 0) {
                    timer = terrorTimer();
                    executeHorrificWhispers(player, strongestScarecrow, isCromsonSeason);
                }

                terrorData.setTerrorTimer(timer);
            }

            int severity = (hasScareCrow ? 1 : 0) + (isCromsonSeason ? 1 : 0);

            assaultCatastropheManager.tick(player, severity);
            stalkerCatastropheManager.tick(player, severity);
            paranoiaCatastropheManager.tick(player, severity);
        }
    }

    private int terrorTimer() {
        WhispersCatastropheSettings whispersSettings = mixedCatastrophesData.getCatastropheSettings().getAspect().getTerror().getWhispers();
        return Functions.random(whispersSettings.getOccurrenceIntervalMin(), whispersSettings.getOccurrenceIntervalMax());
    }

    public void triggerHorrificWhispers(Player player) {
        executeHorrificWhispers(player
                , mixedCatastrophesData.getRootCatastropheManager().getConstructManager().getStrongestScarecrow(getActiveScarecrowList(), player.getLocation())
                , mixedCatastrophesData.getRootCatastropheManager().getWeatherCatastropheManager().getActiveWeather() == WeatherCatastropheType.CrimsonSeason
        );
    }

    private void executeHorrificWhispers(Player player, ScarecrowData scarecrowData, boolean IsCrimsonSeason) {
        WhispersCatastropheSettings whispersSettings = mixedCatastrophesData.getCatastropheSettings().getAspect().getTerror().getWhispers();
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int terrorPlus = whispersSettings.getTerrorGainMin() + new Random().nextInt(whispersSettings.getTerrorGainMax() - whispersSettings.getTerrorGainMin() + 1);
        int secretsPlus = (int) Math.round(whispersSettings.getSecretsGainFlat() + whispersSettings.getSecretsGainTerrorMultiplier() * playerData.getAspect(AspectType.Terror));

        if (IsCrimsonSeason) {
            terrorPlus += 10;
            secretsPlus += 10;
        }

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
            mixedCatastrophesData.getRootCatastropheManager().getConstructManager().constructChanged(scarecrowData);
        }

        int nobility = playerData.getAspect(AspectType.Nobility);
        double nobilityMultiplier = 1.0 + 0.005 * nobility;
        double nobilityFlat = 0.2 * nobility;
        secretsPlus = (int) Math.round(secretsPlus * nobilityMultiplier + nobilityFlat);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Terror, terrorPlus);
        changeMap.put(AspectType.Secrets, secretsPlus);

        if (mixedCatastrophesData.getCatastropheSettings().getAspect().getResolve().isVirtue()) {
            mixedCatastrophesData.getRootCatastropheManager().getPersonalCatastropheManager().getResolveCatastropheManager().mightShowVirtue(player, 0.05);
        }

        mixedCatastrophesData.getEventChangeManager()
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
        ArrayList<String> hintList = new ArrayList<>(mixedCatastrophesData.getPlugin().getConfig().getStringList("hintList"));
        String hint = hintList.get(new Random().nextInt(hintList.size()));

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("They Whisper: " + hint)
                .withColor(Constants.AspectThemes.get(AspectType.Terror).getColor())
                .finish()
                .execute();
    }

    private List<ScarecrowData> getActiveScarecrowList() {
        return mixedCatastrophesData.getRootCatastropheManager().getConstructManager().getScarecrowListIsConstructed(mixedCatastrophesData.getMetaData().getScarecrowDataList());
    }

    private List<LighthouseData> getActiveLighthouseList() {
        return mixedCatastrophesData.getRootCatastropheManager().getConstructManager().getLighthouseListIsConstructed(mixedCatastrophesData.getMetaData().getLighthouseDataList());
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
