package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Random;

public class ConsequenceListener implements Listener {
    private final MixedCatastrophesData mixedCatastrophesData;

    public ConsequenceListener(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    @EventHandler
    public void eat(PlayerItemConsumeEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getNatureConspiracy().isRavenousFood())
            return;

        Player player = event.getPlayer();

        if (!mixedCatastrophesData.getAffectedWorlds().contains(player.getWorld()))
            return;

        if (event.getItem().getType() == Material.MILK_BUCKET) {
            drinkMilk(player);
            return;
        }

        int natureConspiracy = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId()).getAspect(AspectType.Nature_Conspiracy);
        double probability = (natureConspiracy) / (natureConspiracy + 20.0);
        probability /= 5.0;

        if (new Random().nextDouble() < probability) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (10 + natureConspiracy) * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, (10 + natureConspiracy) * 20, 9));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 3 * 20, 0));

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventSound(Sound.BLOCK_BREWING_STAND_BREW)
                    .withEventMessage("Your Food is eating you from the Inside.")
                    .withCause(AspectType.Nature_Conspiracy)
                    .withTitle(true)
                    .finish()
                    .execute();
        }
    }

    @EventHandler
    public void elytraBoost(PlayerInteractEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getHand() != EquipmentSlot.HAND)
            return;

        boolean isCollectable = mixedCatastrophesData.getCatastropheSettings().getAspect().getSkyScorn().isCollectable();
        boolean isTearFlesh = mixedCatastrophesData.getCatastropheSettings().getAspect().getSkyScorn().isTearFlesh();

        if (!isCollectable && !isTearFlesh)
            return;

        Player player = event.getPlayer();

        if (!mixedCatastrophesData.getAffectedWorlds().contains(player.getWorld()))
            return;

        ItemStack handItem = event.getItem();

        if (handItem == null)
            return;

        if (handItem.getType() != Material.FIREWORK_ROCKET)
            return;

        if (!player.isGliding())
            return;

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        int skyScorn = playerData.getAspect(AspectType.SkyScorn);
        double probability = 0.5 * (skyScorn) / (10.0 + skyScorn);

        HashMap<AspectType, Integer> changeMap = new HashMap<>();

        if (isCollectable) {
            changeMap.put(AspectType.SkyScorn, 1);
        }

        if (new Random().nextDouble() < probability && isTearFlesh) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (10) * 20, (int) Math.floor(skyScorn * 0.2)));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, (10) * 20, skyScorn));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 3 * 20, 0));

            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventSound(Sound.AMBIENT_CAVE)
                    .withAspectChange(changeMap)
                    .withEventMessage("The Sky fractures you Body.")
                    .withCause(AspectType.SkyScorn)
                    .withTitle(true)
                    .finish()
                    .execute();
        } else if (isCollectable) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withAspectChange(changeMap)
                    .withEventSound(Sound.AMBIENT_CAVE)
                    .withEventMessage("The Sky dislikes your Hubris of flying.")
                    .withColor(Constants.AspectThemes.get(AspectType.SkyScorn).getColor())
                    .withTitle(true)
                    .finish()
                    .execute();
        }
    }

    private void drinkMilk(Player player) {
        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getNatureConspiracy().isCollectable())
            return;

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Nature_Conspiracy, 1);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.BLOCK_BREWING_STAND_BREW)
                .withEventMessage("The Green Ones have claimed this Milk.")
                .withColor(Constants.AspectThemes.get(AspectType.Nature_Conspiracy).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }

    @EventHandler
    public void harvestBlock(BlockBreakEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        Player player = event.getPlayer();

        if (!mixedCatastrophesData.getAffectedWorlds().contains(player.getWorld()))
            return;

        Material material = event.getBlock().getType();

        if (Constants.Logs.contains(material)) {
            breakWood(player, event.getBlock().getLocation());
        }
    }

    private void breakWood(Player player, Location targetLocation) {
        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getNatureConspiracy().isTheHorde())
            return;

        int natureConspiracy = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId()).getAspect(AspectType.Nature_Conspiracy);
        double probability = natureConspiracy / (natureConspiracy + 5.0);
        probability /= 25.0;

        if (new Random().nextDouble() >= probability)
            return;

        summonKillerRabbits(player, targetLocation);
    }

    @EventHandler
    public void killAnimal(EntityDeathEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getNatureConspiracy().isTheHorde())
            return;

        LivingEntity livingEntity = event.getEntity();

        if (!(livingEntity instanceof Animals))
            return;

        if (livingEntity instanceof Rabbit) {
            if (((Rabbit) livingEntity).getRabbitType() == Rabbit.Type.THE_KILLER_BUNNY) {
                return;
            }
        }

        Player player = livingEntity.getKiller();

        if (player == null)
            return;

        if (!mixedCatastrophesData.getAffectedWorlds().contains(player.getWorld()))
            return;

        int natureConspiracy = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId()).getAspect(AspectType.Nature_Conspiracy);
        double probability = natureConspiracy / (natureConspiracy + 5.0);
        probability /= 10.0;

        if (new Random().nextDouble() >= probability)
            return;

        summonKillerRabbits(player, livingEntity.getLocation());
    }

    private void summonKillerRabbits(Player player, Location targetLocation) {
        World world = player.getWorld();
        int natureConspiracy = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId()).getAspect(AspectType.Nature_Conspiracy);

        String name = "The Horde";
        int amount = (int) Math.floor(3 + natureConspiracy * 0.25);

        for (int i = 0; i < amount; i++) {
            Rabbit rabbit = (Rabbit) world.spawnEntity(targetLocation, EntityType.RABBIT);
            rabbit.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
            rabbit.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(5);
            rabbit.setHealth(5);
            rabbit.setCustomName(name);
            rabbit.setCustomNameVisible(true);
            rabbit.setTarget(player);
        }

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage(name + " enacts its Revenge.")
                .withCause(AspectType.Nature_Conspiracy)
                .withTitle(true)
                .finish()
                .execute();
    }
}
