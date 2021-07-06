package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class NeedListener implements Listener {
    private final MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor;

    public NeedListener(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        this.mixedCatastrophesManagerAccessor = mixedCatastrophesManagerAccessor;
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent event) {
        if (!mixedCatastrophesManagerAccessor.getCatastropheSettings().getAspect().getCelestialFavor().isStarMercy())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.VOID))
            return;

        Location location = player.getLocation();

        if (location.getY() >= 0)
            return;

        PlayerData playerData = mixedCatastrophesManagerAccessor.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Celestial_Favor) <= 0)
            return;

        event.setCancelled(true);

        player.teleport(location.clone().add(0, -2 - location.getY(), 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 20, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 30 * 20, 0));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Celestial_Favor, -1);

        mixedCatastrophesManagerAccessor.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Stars have Mercy on you.")
                .withCause(AspectType.Celestial_Favor)
                .withTitle(true)
                .finish()
                .execute();
    }
}