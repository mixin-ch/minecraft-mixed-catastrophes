package ch.mixin.mixedCatastrophes.catastropheManager.personal.terror;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.assault.AssaultCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.paranoia.ParanoiaCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.stalker.StalkerCatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
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
import java.util.List;
import java.util.Random;

public class TerrorCatastropheManager extends CatastropheManager {
    private final AssaultCatastropheManager assaultCatastropheManager;
    private final StalkerCatastropheManager stalkerCatastropheManager;
    private final ParanoiaCatastropheManager paranoiaCatastropheManager;

    public TerrorCatastropheManager(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor);
        assaultCatastropheManager = new AssaultCatastropheManager(mixedCatastrophesManagerAccessor);
        stalkerCatastropheManager = new StalkerCatastropheManager(mixedCatastrophesManagerAccessor);
        paranoiaCatastropheManager = new ParanoiaCatastropheManager(mixedCatastrophesManagerAccessor);
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
        List<ScarecrowData> scarecrowList = getActiveScarecrowList();

        for (LighthouseData lighthouseData : metaData.getLighthouseDataList()) {
            World lightHouseWorld = plugin.getServer().getWorld(lighthouseData.getWorldName());

            if (lightHouseWorld == null)
                continue;

            Location lighthouseLocation = lighthouseData.getPosition().toLocation(lightHouseWorld);

            if (!Constants.Lighthouse.checkConstructed(lighthouseLocation).isConstructed())
                continue;

            lighthouseMap.put(lighthouseLocation, lighthouseData.getLevel() * Constants.LighthouseRangeFactor);
        }

        playerLoop:
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (!mixedCatastrophesManagerAccessor.getAffectedWorlds().contains(player.getWorld()))
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
            ScarecrowData strongestScarecrow = mixedCatastrophesManagerAccessor.getRootCatastropheManager().getConstructManager().getStrongestScarecrow(scarecrowList, playerLocation);
            boolean hasScareCrow = strongestScarecrow != null;


            if (mixedCatastrophesManagerAccessor.getCatastropheSettings().getAspect().getTerror().isWhispers()) {
                int timer = terrorData.getTerrorTimer();
                timer--;

                if (hasScareCrow) {
                    timer -= 2;
                }

                if (timer <= 0) {
                    timer = terrorTimer();
                    executeHorrificWhispers(player, strongestScarecrow);
                }

                terrorData.setTerrorTimer(timer);
            }

            assaultCatastropheManager.tick(player, hasScareCrow);
            stalkerCatastropheManager.tick(player, hasScareCrow);
            paranoiaCatastropheManager.tick(player, hasScareCrow);
        }
    }

    private int terrorTimer() {
        return Functions.random(8 * 60, 12 * 60);
    }

    public void triggerHorrificWhispers(Player player) {
        executeHorrificWhispers(player, mixedCatastrophesManagerAccessor.getRootCatastropheManager().getConstructManager().getStrongestScarecrow(getActiveScarecrowList(), player.getLocation()));
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

        int nobility = playerData.getAspect(AspectType.Nobility);
        double nobilityMultiplier = 1.0 + 0.005 * nobility;
        double nobilityFlat = 0.2 * nobility;
        secretsPlus = (int) Math.round(secretsPlus * nobilityMultiplier + nobilityFlat);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Terror, terrorPlus);
        changeMap.put(AspectType.Secrets, secretsPlus);

        mixedCatastrophesManagerAccessor.getEventChangeManager()
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

        mixedCatastrophesManagerAccessor.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("They Whisper: " + hint)
                .withColor(Constants.AspectThemes.get(AspectType.Terror).getColor())
                .finish()
                .execute();
    }

    private List<ScarecrowData> getActiveScarecrowList() {
        return mixedCatastrophesManagerAccessor.getRootCatastropheManager().getConstructManager().getScarecrowListIsConstructed(metaData.getScarecrowDataList());
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
