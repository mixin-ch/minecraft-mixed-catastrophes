package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Random;

public class MisdeedListener implements Listener {
    private final MixedCatastrophesData mixedCatastrophesData;

    public MisdeedListener(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
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
            breakWood(player);
        } else if (Constants.Mirrors.contains(material)) {
            breakMirror(player);
        } else if (Constants.Stones.contains(material)) {
            breakStone(player);
        }
    }

    private void breakWood(Player player) {
        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getNatureConspiracy().isCollectable())
            return;

        if (new Random().nextDouble() >= 0.02)
            return;

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Nature_Conspiracy, 1);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Trees conspire against you.")
                .withColor(Constants.AspectThemes.get(AspectType.Nature_Conspiracy).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }

    private void breakMirror(Player player) {
        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getMisfortune().isCollectable())
            return;

        if (new Random().nextDouble() >= 0.05)
            return;

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Misfortune, 7);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("Something Terrible escaped from the Glass.")
                .withColor(Constants.AspectThemes.get(AspectType.Misfortune).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }

    private void breakStone(Player player) {
        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getMisfortune().isCollectable())
            return;

        if (new Random().nextDouble() >= 0.005)
            return;

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Misfortune, 1);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You unearthed something Terrible from the Stone.")
                .withColor(Constants.AspectThemes.get(AspectType.Misfortune).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }

    @EventHandler
    public void killAnimal(EntityDeathEvent event) {
        if (!mixedCatastrophesData.isFullyFunctional())
            return;

        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getNatureConspiracy().isCollectable())
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

        if (new Random().nextDouble() >= 0.05)
            return;

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Nature_Conspiracy, 1);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Animals conspire against you.")
                .withColor(Constants.AspectThemes.get(AspectType.Nature_Conspiracy).getColor())
                .withTitle(true)
                .finish()
                .execute();
    }
}
