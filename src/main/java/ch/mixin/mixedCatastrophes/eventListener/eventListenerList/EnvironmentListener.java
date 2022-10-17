package ch.mixin.mixedCatastrophes.eventListener.eventListenerList;

import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class EnvironmentListener implements Listener {
    private final MixedCatastrophesData mixedCatastrophesData;

    public EnvironmentListener(MixedCatastrophesData mixedCatastrophesData) {
        this.mixedCatastrophesData = mixedCatastrophesData;
    }

    @EventHandler
    public void preventNaturalIronGolem(CreatureSpawnEvent event) {
        if (!mixedCatastrophesData.getPlugin().PluginFlawless)
            return;

        if (!mixedCatastrophesData.getCatastropheSettings().isPreventNaturalIronGolem())
            return;

        if (!event.getEntityType().equals(EntityType.IRON_GOLEM))
            return;

        if (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void hallowedLoot(EntityDeathEvent event) {
        if (!mixedCatastrophesData.getPlugin().PluginFlawless)
            return;

        NamespacedKey key = new NamespacedKey(mixedCatastrophesData.getPlugin(), "hallowed");

        if (event.getEntity().getPersistentDataContainer().get(key, PersistentDataType.SHORT) == null)
            return;

        Player player = event.getEntity().getKiller();

        if (player == null)
            return;

        HashMap<AspectType, Integer> changeMap = new HashMap<>();
        changeMap.put(AspectType.Secrets, 15);

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withAspectChange(changeMap)
                .withEventSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
                .withEventMessage("You killed a Hallowed One")
                .withColor(Constants.AspectThemes.get(AspectType.Secrets).getColor())
                .finish()
                .execute();
    }
}
