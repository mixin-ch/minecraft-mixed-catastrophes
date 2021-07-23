package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class NeedListener implements Listener {
    private final MixedCatastrophesData mixedCatastrophesData;
    private final HashMap<UUID, LocalDateTime> playerVoidTimeMap = new HashMap<>();

    public NeedListener(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    @EventHandler
    public void entityDamageEvent(EntityDamageEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getCelestialFavor().isStarMercy())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.VOID))
            return;

        Location location = player.getLocation();

        if (location.getY() >= 0)
            return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastVoid = playerVoidTimeMap.get(player.getUniqueId());

        if (pastVoid != null && Duration.between(pastVoid, now).getSeconds() <= 5) {
            event.setCancelled(true);
            return;
        }

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (playerData.getAspect(AspectType.Celestial_Favor) <= 0)
            return;

        event.setCancelled(true);
        playerVoidTimeMap.put(player.getUniqueId(), now);

        player.teleport(location.clone().add(0, -2 - location.getY(), 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 20, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 30 * 20, 0));

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Celestial_Favor, -1);

        mixedCatastrophesData.getEventChangeManager()
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