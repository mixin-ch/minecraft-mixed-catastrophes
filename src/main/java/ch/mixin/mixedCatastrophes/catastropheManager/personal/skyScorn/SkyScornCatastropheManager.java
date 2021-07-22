package ch.mixin.mixedCatastrophes.catastropheManager.personal.skyScorn;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SkyScornCatastropheManager extends CatastropheManager {
    public SkyScornCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
    }

    @Override
    @Deprecated
    public void initializeMetaData() {
    }

    public void initializePlayerData(PlayerData playerData) {
        int skyScornTimer = playerData.getSkyScornTimer();

        if (skyScornTimer <= 0) {
            skyScornTimer = skyScornTimer();
        }
    }

    @Override
    public void updateMetaData() {
    }

    @Override
    public void initializeCauser() {
    }

    @Override
    public void tick() {
        for (Player player : mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers()) {
            if (!mixedCatastrophesData.getAffectedWorlds().contains(player.getWorld()))
                continue;

            PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

            if (playerData.getAspect(AspectType.SkyScorn) <= 0)
                continue;

            int timer = playerData.getSkyScornTimer();
            timer--;

            if (timer <= 0) {
                timer = skyScornTimer();

                HashMap<AspectType, Integer> changeMap = new HashMap<>();
                changeMap.put(AspectType.SkyScorn, -1);

                mixedCatastrophesData.getEventChangeManager()
                        .eventChange(player)
                        .withAspectChange(changeMap)
                        .execute();
            }

            playerData.setSkyScornTimer(timer);
        }
    }

    private int skyScornTimer() {
        return 5 * 60;
    }
}
