package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class AttackListener implements Listener {
    private final MixedCatastrophesData mixedCatastrophesData;

    public AttackListener(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        if (!mixedCatastrophesData.getAffectedWorlds().contains(event.getEntity().getWorld()))
            return;

        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();

        if (damager instanceof Player) {
            playerHitEntityMelee(event);
        } else if (damager instanceof Arrow && (((Arrow) damager).getShooter() instanceof Player)) {
            playerHitEntityArrow(event);
        } else if (damagee instanceof Player) {
            if (!((Player) damagee).isBlocking())
                return;

            if (damager instanceof LivingEntity) {
                playerBlockEntityMelee(event);
            } else if (damager instanceof Projectile && (((Projectile) damager).getShooter() instanceof LivingEntity)) {
                playerBlockEntityProjectile(event);
            }
        }
    }

    @EventHandler
    public void entityShootBow(EntityShootBowEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        Entity entity = event.getEntity();

        if (!(entity instanceof Player))
            return;

        if (!mixedCatastrophesData.getAffectedWorlds().contains(entity.getWorld()))
            return;

        Player player = (Player) entity;
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (tryPlayerMiss(playerData)) {
            if (tryPlayerCrit(playerData)) {
                mixedCatastrophesData.getEventChangeManager()
                        .eventChange(player)
                        .withEventSound(Sound.ENTITY_ITEM_BREAK)
                        .withEventMessage("A Misfire avoided.")
                        .withCause(AspectType.Nobility)
                        .finish()
                        .execute();
            } else {
                event.setCancelled(true);

                mixedCatastrophesData.getEventChangeManager()
                        .eventChange(player)
                        .withEventSound(Sound.ENTITY_ITEM_BREAK)
                        .withEventMessage("An unlucky Misfire.")
                        .withCause(AspectType.Misfortune)
                        .finish()
                        .execute();
            }
        }
    }

    private boolean tryPlayerCrit(PlayerData playerData) {
        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getNobility().isCritAttack())
            return false;

        int nobility = playerData.getAspect(AspectType.Nobility);
        double probability = (nobility) / (nobility + 100.0);
        return new Random().nextDouble() < probability;
    }

    private boolean tryPlayerMiss(PlayerData playerData) {
        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getMisfortune().isMissAttack())
            return false;

        int misfortune = playerData.getAspect(AspectType.Misfortune);
        double probability = (misfortune) / (misfortune + 30.0);
        probability /= 2.0;
        return new Random().nextDouble() < probability;
    }

    private void playerHitEntityMelee(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Player player = (Player) event.getDamager();
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (tryPlayerCrit(playerData)) {
            event.setDamage(event.getDamage() * 2);
            Vector direction = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            entity.setVelocity(direction.multiply(1.5));

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventSound(Sound.BLOCK_ANVIL_PLACE)
                    .withEventMessage("An exceptional Hit.")
                    .withCause(AspectType.Nobility)
                    .finish()
                    .execute();
        } else if (tryPlayerMiss(playerData)) {
            event.setCancelled(true);

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventSound(Sound.ENTITY_ITEM_BREAK)
                    .withEventMessage("An unlucky Miss.")
                    .withCause(AspectType.Misfortune)
                    .finish()
                    .execute();
        }
    }

    private void playerHitEntityArrow(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();
        Player player = (Player) arrow.getShooter();
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (tryPlayerCrit(playerData)) {
            event.setDamage(event.getDamage() * 2);
            Vector direction = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            entity.setVelocity(direction.multiply(1.5));

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventSound(Sound.BLOCK_ANVIL_PLACE)
                    .withEventMessage("An exceptional Shot.")
                    .withCause(AspectType.Nobility)
                    .finish()
                    .execute();
        }
    }

    private void playerBlockEntityMelee(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getEntity();
        LivingEntity damager = (LivingEntity) event.getDamager();
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (tryPlayerCrit(playerData)) {
            damager.damage(1);
            Vector direction = damager.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            damager.setVelocity(direction.multiply(1.5));

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventSound(Sound.BLOCK_ANVIL_PLACE)
                    .withEventMessage("An exceptional Block.")
                    .withCause(AspectType.Nobility)
                    .finish()
                    .execute();
        }
    }

    private void playerBlockEntityProjectile(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getEntity();
        Projectile projectile = (Projectile) event.getDamager();
        LivingEntity shooter = (LivingEntity) projectile.getShooter();
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        if (tryPlayerCrit(playerData)) {
            shooter.damage(1);
            Vector direction = shooter.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
            shooter.setVelocity(direction.multiply(1.5));

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventSound(Sound.BLOCK_ANVIL_PLACE)
                    .withEventMessage("An exceptional Block.")
                    .withCause(AspectType.Nobility)
                    .finish()
                    .execute();
        }
    }
}
