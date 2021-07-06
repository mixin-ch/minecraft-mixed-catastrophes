package ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.assault;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.metaData.constructs.LighthouseData;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.TerrorData;
import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate2D;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.constructs.ScarecrowData;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AssaultCatastropheManager extends CatastropheManager {
    private static final HashMap<AssaultPremise, Double> entitySummonDayWeights;
    private static final HashMap<AssaultPremise, Double> entitySummonNightWeights;

    static {
        entitySummonDayWeights = new HashMap<>();
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.BLAZE
                , "The Ones birthed from the shaping Fire."
                , 1.5
        ), 1.0);
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.CREEPER
                , "A dooming Hiss is their first and last Introduction."
                , 2.5
        ), 1.0);
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.EVOKER
                , "The Summoner has been summoned."
                , 0.35
        ), 1.0);
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.GHAST
                , "An agonizing Scream penetrates the Air."
                , 1
        ), 1.0);
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.HUSK
                , "Dust and decayed Flesh is walking."
                , 8.5
        ), 1.0);
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.MAGMA_CUBE
                , "The leaping liquid Fire."
                , 4
        ), 1.0);
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.SLIME
                , "Green, and greedy for Flesh."
                , 8
        ), 1.0);
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.PILLAGER
                , "To pillage is their only Desire."
                , 2.5
        ), 1.0);
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.VINDICATOR
                , "The Roars of Berserkers are raging."
                , 2
        ), 1.0);
        entitySummonDayWeights.put(new AssaultPremise(
                EntityType.WITCH
                , "The Brewing of vile Potions is an Art Form."
                , 2
        ), 1.0);

        entitySummonNightWeights = new HashMap<>();
        entitySummonNightWeights.put(new AssaultPremise(
                EntityType.CAVE_SPIDER
                , "These Ones are vile and venomous."
                , 3
        ), 1.0);
        entitySummonNightWeights.put(new AssaultPremise(
                EntityType.DROWNED
                , "From the dead Waters they emerge."
                , 8
        ), 1.0);
        entitySummonNightWeights.put(new AssaultPremise(
                EntityType.ENDERMAN
                , "They visit from the End of the World."
                , 2.5
        ), 1.0);
        entitySummonNightWeights.put(new AssaultPremise(
                EntityType.PHANTOM
                , "The Terrors of the Sky seek their Prey."
                , 3
        ), 1.0);
        entitySummonNightWeights.put(new AssaultPremise(
                EntityType.SKELETON
                , "They wake with Bone, Bow and Arrow."
                , 4
        ), 1.0);
        entitySummonNightWeights.put(new AssaultPremise(
                EntityType.SPIDER
                , "Many legged and many eyed they lurk."
                , 5
        ), 1.0);
        entitySummonNightWeights.put(new AssaultPremise(
                EntityType.STRAY
                , "They are the icy Touch of Death"
                , 3
        ), 1.0);
        entitySummonNightWeights.put(new AssaultPremise(
                EntityType.WITHER_SKELETON
                , "Charred and withered they inflict their deadly Curse."
                , 2.5
        ), 1.0);
        entitySummonNightWeights.put(new AssaultPremise(
                EntityType.ZOMBIE
                , "The rotten Flesh is unwilling to rest."
                , 10
        ), 1.0);
    }

    public AssaultCatastropheManager(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor);
    }

    @Override
    public void initializeMetaData() {
    }

    public void initializePlayerData(PlayerData playerData) {
        TerrorData terrorData = playerData.getTerrorData();
        if (terrorData.getAssaultTimer() <= 0) {
            terrorData.setAssaultTimer(assaultTimer());
        }
    }

    @Override
    public void updateMetaData() {

    }

    @Override
    public void initializeCauser() {
    }

    @Override
    @Deprecated
    public void tick() {
    }

    public void tick(Player player, boolean hasScareCrow) {
        if (!mixedCatastrophesManagerAccessor.getCatastropheSettings().getAspect().getTerror().isAssault())
            return;

        PlayerData playerData = metaData.getPlayerDataMap().get(player.getUniqueId());
        TerrorData terrorData = playerData.getTerrorData();

        int timer = terrorData.getAssaultTimer();
        timer--;

        if (hasScareCrow) {
            timer -= 2;
        }

        if (timer <= 0) {
            timer = assaultTimer();

            int terror = playerData.getAspect(AspectType.Terror) + (hasScareCrow ? 100 : 0);
            double modifier = Math.pow(terror, 0.5);
            double probability = (modifier + 1) / (modifier + 100.0);

            if (new Random().nextDouble() < probability) {
                causeAssault(player);
            }
        }

        terrorData.setAssaultTimer(timer);
    }

    private int assaultTimer() {
        return Functions.random(20, 30);
    }

    public void causeAssault(Player player) {
        PlayerData playerData = metaData.getPlayerDataMap().get(player.getUniqueId());

        World world = player.getWorld();
        Location mainPlayerLocation = player.getLocation();
        HashMap<AssaultPremise, Double> possibleAssaults;

        if (Functions.isNight(world)) {
            possibleAssaults = new HashMap<>(entitySummonNightWeights);
        } else {
            possibleAssaults = new HashMap<>(entitySummonDayWeights);
        }

        int terror = playerData.getAspect(AspectType.Terror);
        double modifier = Math.pow(terror * 0.03, 0.75);
        int amount = 0;
        AssaultPremise assaultPremise = null;

        while (amount == 0 && possibleAssaults.size() > 0) {
            assaultPremise = Functions.getRandomWithWeights(possibleAssaults);
            possibleAssaults.remove(assaultPremise);
            amount = (int) Math.floor(modifier * assaultPremise.getAmount());
        }

        if (amount == 0)
            return;

        Coordinate2D center = new Coordinate2D(mainPlayerLocation.getBlockX(), mainPlayerLocation.getBlockZ());
        ArrayList<Coordinate2D> spaces = Functions.getSphere2D(center, 30);
        ArrayList<Location> spawnPoints = new ArrayList<>();
        ArrayList<Location> playerLocations = new ArrayList<>();

        for (Player p : world.getPlayers()) {
            playerLocations.add(p.getLocation());
        }

        HashMap<Location, Integer> lighthouseMap = new HashMap<>();

        for (LighthouseData lighthouseData : metaData.getLighthouseDataList()) {
            World lightHouseWorld = plugin.getServer().getWorld(lighthouseData.getWorldName());

            if (lightHouseWorld == null)
                continue;

            if (world != lightHouseWorld)
                continue;

            Location lighthouseLocation = lighthouseData.getPosition().toLocation(lightHouseWorld);

            if (!Constants.Lighthouse.checkConstructed(lighthouseLocation).isConstructed())
                continue;

            lighthouseMap.put(lighthouseLocation, lighthouseData.getLevel() * Constants.LighthouseRangeFactor);
        }

        spaceLoop:
        for (Coordinate2D space : spaces) {
            Location groundP1 = Functions.offset(Functions.relativeGround(world, space.to3D(mainPlayerLocation.getBlockY())), 1);

            if (groundP1 == null)
                continue;

            if (player.getLocation().distance(groundP1) > 40)
                continue;

            if (Math.abs(player.getLocation().getY() - groundP1.getY()) > 15)
                continue;

            for (Location lighthouseLocation : lighthouseMap.keySet()) {
                int range = lighthouseMap.get(lighthouseLocation);

                if (lighthouseLocation.distance(groundP1) <= range)
                    continue spaceLoop;
            }

            for (Location playerLocation : playerLocations) {
                if (playerLocation.distance(groundP1) < 20)
                    continue spaceLoop;
            }

            spawnPoints.add(groundP1);
        }

        if (spawnPoints.size() == 0)
            return;

        for (int i = 0; i < amount; i++) {
            Location spawnPoint = spawnPoints.get(new Random().nextInt(spawnPoints.size()));
            Mob mob = (Mob) world.spawnEntity(spawnPoint, assaultPremise.getEntityType());
            mob.setTarget(player);
        }

        mixedCatastrophesManagerAccessor.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage(assaultPremise.getMessage())
                .withCause(AspectType.Terror)
                .withTitle(true)
                .finish()
                .execute();
    }
}
