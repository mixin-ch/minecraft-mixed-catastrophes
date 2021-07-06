package ch.mixin.mixedCatastrophes.main;

import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheSettings.CatastropheSettings;
import ch.mixin.mixedCatastrophes.eventChange.EventChangeManager;
import ch.mixin.mixedCatastrophes.helpInventory.HelpInventoryManager;
import ch.mixin.mixedCatastrophes.helperClasses.Particler;
import ch.mixin.mixedCatastrophes.metaData.MetaData;
import ch.mixin.mixedCatastrophes.mixedAchievements.MixedAchievementsManager;
import org.bukkit.World;

import java.util.List;

public class MixedCatastrophesData {
    private final MixedCatastrophesPlugin plugin;
    private boolean fullyFunctional;
    private MetaData metaData;
    private List<World> affectedWorlds;
    private CatastropheSettings catastropheSettings;
    private EventChangeManager eventChangeManager;
    private RootCatastropheManager rootCatastropheManager;
    private HelpInventoryManager helpInventoryManager;
    private Particler particler;
    private MixedAchievementsManager mixedAchievementsManager;

    public MixedCatastrophesData(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
    }

    public MixedCatastrophesPlugin getPlugin() {
        return plugin;
    }

    public boolean isFullyFunctional() {
        return fullyFunctional;
    }

    public void setFullyFunctional(boolean fullyFunctional) {
        this.fullyFunctional = fullyFunctional;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public List<World> getAffectedWorlds() {
        return affectedWorlds;
    }

    public void setAffectedWorlds(List<World> affectedWorlds) {
        this.affectedWorlds = affectedWorlds;
    }

    public CatastropheSettings getCatastropheSettings() {
        return catastropheSettings;
    }

    public void setCatastropheSettings(CatastropheSettings catastropheSettings) {
        this.catastropheSettings = catastropheSettings;
    }

    public EventChangeManager getEventChangeManager() {
        return eventChangeManager;
    }

    public void setEventChangeManager(EventChangeManager eventChangeManager) {
        this.eventChangeManager = eventChangeManager;
    }

    public RootCatastropheManager getRootCatastropheManager() {
        return rootCatastropheManager;
    }

    public void setRootCatastropheManager(RootCatastropheManager rootCatastropheManager) {
        this.rootCatastropheManager = rootCatastropheManager;
    }

    public HelpInventoryManager getHelpInventoryManager() {
        return helpInventoryManager;
    }

    public void setHelpInventoryManager(HelpInventoryManager helpInventoryManager) {
        this.helpInventoryManager = helpInventoryManager;
    }

    public Particler getParticler() {
        return particler;
    }

    public void setParticler(Particler particler) {
        this.particler = particler;
    }

    public MixedAchievementsManager getMixedAchievementsManager() {
        return mixedAchievementsManager;
    }

    public void setMixedAchievementsManager(MixedAchievementsManager mixedAchievementsManager) {
        this.mixedAchievementsManager = mixedAchievementsManager;
    }
}
