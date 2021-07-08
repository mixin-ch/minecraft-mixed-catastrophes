package ch.mixin.mixedCatastrophes.catastropheManager.personal.resolve;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

public class ResolveCatastropheManager extends CatastropheManager {
    public ResolveCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
    }

    @Override
    @Deprecated
    public void initializeMetaData() {
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
            int resolve = playerData.getAspect(AspectType.Resolve);
            int loss = (int) Math.ceil(0.01 * resolve);

            if (loss <= 0)
                continue;

            HashMap<AspectType, Integer> changeMap = new HashMap<>();
            changeMap.put(AspectType.Resolve, -loss);

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withAspectChange(changeMap)
                    .execute();
        }
    }

    public void mightShowVirtue(Player actor, double probability) {
        if (new Random().nextDouble() >= probability)
            return;

        showVirtue(actor);
    }

    public void showVirtue(Player actor) {
        if (!mixedCatastrophesData.getAffectedWorlds().contains(actor.getWorld()))
            return;

        PlayerData actorData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(actor.getUniqueId());
        int terror = actorData.getAspects().get(AspectType.Terror);
        int nobility = actorData.getAspects().get(AspectType.Nobility);
        double resolveSpread = 5 * (10 + Math.pow(terror + nobility, 0.5));

        String text = actor.getName() + " shows Virtue";
        ChatColor color = Constants.AspectThemes.get(AspectType.Resolve).getColor();

        for (Player target : mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers()) {
            if (!mixedCatastrophesData.getAffectedWorlds().contains(target.getWorld()))
                continue;

            double distance = target.getLocation().distance(actor.getLocation());

            int resolveGain = (int) Math.floor(resolveSpread - Math.pow(distance * 0.5, 2));

            if (resolveGain <= 0)
                continue;

            HashMap<AspectType, Integer> changeMap = new HashMap<>();
            changeMap.put(AspectType.Resolve, resolveGain);

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(target)
                    .withAspectChange(changeMap)
                    .withEventSound(Sound.AMBIENT_CAVE)
                    .withEventMessage(text)
                    .withColor(color)
                    .withTitle(true)
                    .finish()
                    .execute();
        }
    }
}
