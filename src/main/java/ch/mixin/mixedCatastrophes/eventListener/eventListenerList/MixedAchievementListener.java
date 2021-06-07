package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedAchievements.event.AchievementCompletedEvent;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class MixedAchievementListener implements Listener {
    private final MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor;

    public MixedAchievementListener(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        this.mixedCatastrophesManagerAccessor = mixedCatastrophesManagerAccessor;
    }

    @EventHandler
    public void entityDamageEvent(AchievementCompletedEvent event) {
        if (!event.getSetId().equals(MixedCatastrophesPlugin.PLUGIN_NAME))
            return;

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Nobility, 1);

        mixedCatastrophesManagerAccessor.getEventChangeManager()
                .eventChange(event.getPlayer())
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .execute();
    }
}