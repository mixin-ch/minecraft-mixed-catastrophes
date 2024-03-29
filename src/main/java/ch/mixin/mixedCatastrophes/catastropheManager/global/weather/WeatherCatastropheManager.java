package ch.mixin.mixedCatastrophes.catastropheManager.global.weather;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterType;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate2D;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.BlitzardData;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.LighthouseData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

public class WeatherCatastropheManager extends CatastropheManager {
    private static final HashMap<WeatherCatastropheType, Double> catastropheWeights;

    static {
        catastropheWeights = new HashMap<>();
        catastropheWeights.put(WeatherCatastropheType.Nothing, 75.0);
        catastropheWeights.put(WeatherCatastropheType.RadiantSky, 1.0);
        catastropheWeights.put(WeatherCatastropheType.ThunderStorm, 1.0);
        catastropheWeights.put(WeatherCatastropheType.SearingCold, 1.0);
        catastropheWeights.put(WeatherCatastropheType.GravityLoss, 0.35);
        catastropheWeights.put(WeatherCatastropheType.CatsAndDogs, 0.15);
        catastropheWeights.put(WeatherCatastropheType.PersonaShift, 0.35);
        catastropheWeights.put(WeatherCatastropheType.CrimsonSeason, 0.65);
        catastropheWeights.put(WeatherCatastropheType.MeteorShower, 0.50);
    }

    private int weatherTimer;
    private WeatherCatastropheType activeWeather;

    public WeatherCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
    }

    @Override
    public void initializeMetaData() {
        if (mixedCatastrophesData.getMetaData().getWeatherTimer() <= 0) {
            mixedCatastrophesData.getMetaData().setWeatherCatastropheType(WeatherCatastropheType.Nothing);
            mixedCatastrophesData.getMetaData().setWeatherTimer(weatherTimer(WeatherCatastropheType.Nothing));
        }
    }

    @Override
    public void updateMetaData() {
        mixedCatastrophesData.getMetaData().setWeatherTimer(weatherTimer);
        mixedCatastrophesData.getMetaData().setWeatherCatastropheType(activeWeather);
    }

    @Override
    public void initializeCauser() {
        weatherTimer = mixedCatastrophesData.getMetaData().getWeatherTimer();
        activeWeather = mixedCatastrophesData.getMetaData().getWeatherCatastropheType();
    }

    private int weatherTimer(WeatherCatastropheType weatherCatastropheType) {
        switch (weatherCatastropheType) {
            case Nothing:
                return Functions.random(30, 60);
            case RadiantSky:
                return Functions.random(30, 300);
            case ThunderStorm:
                return Functions.random(15, 150);
            case SearingCold:
                return Functions.random(30, 300);
            case GravityLoss:
                return Functions.random(10, 30);
            case CatsAndDogs:
                return Functions.random(5, 15);
            case PersonaShift:
                return Functions.random(30, 300);
            case CrimsonSeason:
                return Functions.random(120, 300);
            case MeteorShower:
                return Functions.random(15, 45);
        }

        return 0;
    }

    @Override
    public void tick() {
        weatherTimer--;

        if (weatherTimer <= 0) {
            WeatherCatastropheType newWeather;

            if (activeWeather == WeatherCatastropheType.Nothing) {
                HashMap<WeatherCatastropheType, Boolean> weatherSettings = mixedCatastrophesData.getCatastropheSettings().getWeather();
                newWeather = Functions.getRandomWithWeights(catastropheWeights);

                while (newWeather != WeatherCatastropheType.Nothing && !weatherSettings.get(newWeather)) {
                    newWeather = Functions.getRandomWithWeights(catastropheWeights);
                }
            } else {
                newWeather = WeatherCatastropheType.Nothing;
            }

            changeWeather(newWeather);
        }

        enforceWeather(activeWeather);
        distributeParticles(activeWeather);
    }

    public void changeWeather(WeatherCatastropheType weatherCatastropheType) {
        stopWeather(activeWeather);
        activeWeather = weatherCatastropheType;
        weatherTimer = weatherTimer(activeWeather);
        startWeather(activeWeather);
    }

    private void startWeather(WeatherCatastropheType weatherCatastropheType) {
        Sound sound = Sound.AMBIENT_CAVE;
        String message = weatherCatastropheType.toString() + " start";
        ChatColor color = Constants.WeatherThemes.get(weatherCatastropheType).getColor();
        boolean playEffect = true;

        switch (weatherCatastropheType) {
            case RadiantSky:
                message = "The sky is scattered with rays of radiance.";
                break;
            case ThunderStorm:
                message = "Erratic lightning licks across the heavens.";
                break;
            case SearingCold:
                message = "The creeping cold tears the flesh.";
                break;
            case GravityLoss:
                message = "The ground can not hold you.";
                break;
            case CatsAndDogs:
                message = "It is raining cats and dogs.";
                break;
            case PersonaShift:
                message = "Do you even know who you are?";
                break;
            case CrimsonSeason:
                message = "The season of crimson has begun.";
                break;
            case MeteorShower:
                message = "Bombardement from the astral.";
                break;
            default:
                playEffect = false;
                break;
        }

        if (playEffect)
            playEffect(sound, message, color);
    }

    private void stopWeather(WeatherCatastropheType weatherCatastropheType) {
        Sound sound = Sound.AMBIENT_CAVE;
        String message = weatherCatastropheType.toString() + " end";
        ChatColor color = Constants.WeatherThemes.get(weatherCatastropheType).getColor();
        boolean playEffect = true;

        switch (weatherCatastropheType) {
            case RadiantSky:
                message = "The sky is no longer radiant.";
                break;
            case ThunderStorm:
                message = "The thunderous roaring ceases.";
                break;
            case SearingCold:
                message = "The icy winds vanish.";
                break;
            case GravityLoss:
                message = "Gravity grips you again.";
                break;
            case CatsAndDogs:
                message = "The last barks pass by.";
                break;
            case PersonaShift:
                message = "I am myself again.";
                break;
            case CrimsonSeason:
                message = "The season of crimson has ended.";
                break;
            case MeteorShower:
                message = "The skies are at rest again.";
                break;
            default:
                playEffect = false;
                break;
        }

        if (playEffect)
            playEffect(sound, message, color);
    }

    private void enforceWeather(WeatherCatastropheType weatherCatastropheType) {
        switch (weatherCatastropheType) {
            case Nothing:
                break;
            case RadiantSky:
                enforceRadiantSky();
                break;
            case ThunderStorm:
                enforceThunderStorm();
                break;
            case SearingCold:
                enforceSearingCold();
                break;
            case GravityLoss:
                enforceGravityLoss();
                break;
            case CatsAndDogs:
                enforceCatsAndDogs();
                break;
            case PersonaShift:
                enforcePersonaShift();
                break;
            case MeteorShower:
                enforceMeteorShower();
                break;
            case CrimsonSeason:
                break;
        }
    }

    private void playEffect(Sound sound, String message, ChatColor chatColor) {
        for (Player player : mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers()) {
            mixedCatastrophesData.getEventChangeManager()
                    .eventChange(player)
                    .withEventSound(sound)
                    .withEventMessage(message)
                    .withColor(chatColor)
                    .withTitle(true)
                    .finish()
                    .execute();
        }
    }

    private void distributeParticles(WeatherCatastropheType weatherCatastropheType) {
        Particle particle = null;

        switch (weatherCatastropheType) {
            case Nothing:
                break;
            case RadiantSky:
                particle = Particle.ASH;
                break;
            case ThunderStorm:
                particle = Particle.CRIT_MAGIC;
                break;
            case SearingCold:
                particle = Particle.WHITE_ASH;
                break;
        }

        if (particle != null) {
            particleChain(particle, 4);
        }
    }

    private void particleChain(Particle particle, int ticks) {
        for (World world : mixedCatastrophesData.getAffectedWorlds()) {
            ArrayList<Location> openLocations = new ArrayList<>();

            for (Player player : world.getPlayers()) {
                openLocations = Functions.merge(openLocations, Functions.getSphereLocation(player.getLocation(), 5));
            }

            for (Location location : openLocations) {
                if (!Constants.Airs.contains(location.getBlock().getType()))
                    continue;

                if (new Random().nextDouble() < 1 / (double) 20) {
                    world.spawnParticle(particle, location, 1);
                }
            }
        }

        int remainingTicks = ticks - 1;

        if (remainingTicks > 0) {
            mixedCatastrophesData.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(mixedCatastrophesData.getPlugin(), () -> particleChain(particle, remainingTicks)
                    , 5);
        }
    }

    private ArrayList<Location> getRandomRoofSpaces(ArrayList<Location> centerLocations, double radius, double amount) {
        ArrayList<Location> spaces = new ArrayList<>();
        Random random = new Random();

        for (Location centerLocation : centerLocations) {
            int AffectedClose = 0;

            for (Location space : spaces)
                if (centerLocation.distance(space) <= radius)
                    AffectedClose++;

            while (random.nextDouble() < (amount - AffectedClose)) {
                Coordinate2D newSpace = Coordinate2D.random().multiply(random.nextDouble() * radius);
                newSpace = newSpace.sum(centerLocation.getX(), centerLocation.getZ());

                Location roof = Functions.absoluteRoof(centerLocation.getWorld(), newSpace);
                if (roof == null)
                    continue;

                Location roofAbove = Functions.offset(roof, 1);
                if (roofAbove == null)
                    continue;

                if (!Constants.Airs.contains(roofAbove.getBlock().getType()))
                    continue;

                spaces.add(roofAbove);
                AffectedClose++;
            }
        }

        return spaces;
    }

    private void enforceRadiantSky() {
        for (World world : mixedCatastrophesData.getAffectedWorlds()) {
            if (Functions.isNight(world))
                continue;

            ArrayList<Location> centerLocations = new ArrayList<>();

            for (Player player : world.getPlayers()) {
                centerLocations.add(player.getLocation());

                if (!Functions.hasShelter(player)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0));

                    if (player.getWorld().getTime() % (2 * 20) < 20)
                        player.setFireTicks(20);
                }
            }

            if (mixedCatastrophesData.getCatastropheSettings().isRadiantSkyMakesFire())
                for (Location space : getRandomRoofSpaces(centerLocations, 20, 1.25))
                    space.getBlock().setType(Material.FIRE);
        }
    }

    private void enforceThunderStorm() {
        HashMap<UUID, PlayerData> pcm = mixedCatastrophesData.getMetaData().getPlayerDataMap();
        ArrayList<Location> targets = new ArrayList<>();
        List<BlitzardData> blitzardList = mixedCatastrophesData.getRootCatastropheManager().getConstructManager()
                .getBlitzardListIsActive(mixedCatastrophesData.getMetaData().getBlitzardDataList());

        for (World world : mixedCatastrophesData.getAffectedWorlds()) {
            ArrayList<Location> centerLocations = new ArrayList<>();

            for (Player player : world.getPlayers()) {
                Location location = player.getLocation();
                centerLocations.add(location);

                if (Functions.hasShelter(player))
                    continue;

                int misfortune = pcm.get(player.getUniqueId()).getAspect(AspectType.Misfortune);
                double probability = misfortune / (40.0 + misfortune);

                if (new Random().nextDouble() < probability)
                    targets.add(rerouteBlitzard(blitzardList, location));

            }

            for (Location space : getRandomRoofSpaces(centerLocations, 20, 1.00))
                targets.add(rerouteBlitzard(blitzardList, space));
        }

        lightningChain(targets, 4);
    }

    private Location rerouteBlitzard(List<BlitzardData> blitzardList, Location baseLocation) {
        BlitzardData strongestBlitzard = mixedCatastrophesData.getRootCatastropheManager().getConstructManager()
                .getStrongestBlitzard(blitzardList, baseLocation);
        double strongestPull = -1;

        if (strongestBlitzard != null) {
            return strongestBlitzard.getPosition().toLocation(baseLocation.getWorld());
        } else {
            return baseLocation;
        }
    }

    private void lightningChain(ArrayList<Location> targets, int ticks) {
        for (int i = 0; i < targets.size(); i++) {
            if (new Random().nextDouble() < 1 / (double) ticks) {
                Location target = targets.get(i);
                targets.remove(target);
                i--;

                target.getWorld().strikeLightning(target.add(0.5, 0, 0.5));
            }
        }

        int remainingTicks = ticks - 1;

        if (remainingTicks > 0) {
            mixedCatastrophesData.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(mixedCatastrophesData.getPlugin(), () -> lightningChain(targets, remainingTicks)
                    , 5);
        }
    }

    private void enforceSearingCold() {
        for (World world : mixedCatastrophesData.getAffectedWorlds()) {

            for (Player player : world.getPlayers()) {
                Location playerLocation = player.getLocation();

                boolean warm = Constants.HotItems.contains(player.getInventory().getItemInMainHand().getType())
                        || Constants.HotItems.contains(player.getInventory().getItemInOffHand().getType());

                if (!warm) {
                    sphere:
                    for (Location location : Functions.getSphereLocation(playerLocation, 7)) {
                        Material material = location.getBlock().getType();
                        double distance = location.distance(playerLocation);

                        switch (material) {
                            case LAVA:
                                if (distance <= 7) {
                                    warm = true;
                                    break sphere;
                                }
                                break;
                            case CAMPFIRE:
                            case SOUL_CAMPFIRE:
                            case FIRE:
                            case SOUL_FIRE:
                            case MAGMA_BLOCK:
                                if (distance <= 5) {
                                    warm = true;
                                    break sphere;
                                }
                                break;
                            case TORCH:
                            case SOUL_TORCH:
                            case WALL_TORCH:
                            case SOUL_WALL_TORCH:
                            case GLOWSTONE:
                                if (distance <= 2) {
                                    warm = true;
                                    break sphere;
                                }
                                break;
                            case BLAST_FURNACE:
                            case FURNACE:
                                Furnace furnace = (Furnace) location.getBlock().getState();
                                if (furnace.getBurnTime() > 0 && distance <= 5) {
                                    warm = true;
                                    break sphere;
                                }
                                break;
                        }
                    }
                }

                if (!warm) {
                    if (Functions.isNight(player.getWorld())) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 3));
                        player.removePotionEffect(PotionEffectType.JUMP);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2 * 20, -5));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 2 * 20, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2 * 20, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 2 * 20, 4));
                        if (player.getWorld().getTime() % (5 * 20) < 20) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 2 * 20, 0));
                        }
                    } else {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 2 * 20, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 2 * 20, 0));
                    }
                }
            }
        }
    }

    private void enforceGravityLoss() {
        for (World world : mixedCatastrophesData.getAffectedWorlds()) {
            for (Player player : world.getPlayers()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2 * 20, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 12 * 20, 0));
            }
        }
    }

    private void enforceCatsAndDogs() {
        ArrayList<Location> targets = new ArrayList<>();

        for (World world : mixedCatastrophesData.getAffectedWorlds()) {
            ArrayList<Location> centerLocations = new ArrayList<>();

            for (Player player : world.getPlayers())
                centerLocations.add(player.getLocation());

            for (Location space : getRandomRoofSpaces(centerLocations, 20, 1.00)) {
                space.setY(world.getMaxHeight() + 64);
                targets.add(space);
            }
        }

        catsAndDogsChain(targets, 4);
    }

    private void catsAndDogsChain(ArrayList<Location> targets, int ticks) {
        for (int i = 0; i < targets.size(); i++) {
            if (new Random().nextDouble() < 1 / (double) ticks) {
                Location target = targets.get(i);
                targets.remove(target);
                i--;

                EntityType entityType;

                if (new Random().nextBoolean()) {
                    entityType = EntityType.WOLF;
                } else {
                    entityType = EntityType.CAT;
                }

                Creature creature = (Creature) target.getWorld().spawnEntity(target, entityType);
                creature.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1000 * 20, 0));
            }
        }

        int remainingTicks = ticks - 1;

        if (remainingTicks > 0) {
            mixedCatastrophesData.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(mixedCatastrophesData.getPlugin(), () -> catsAndDogsChain(targets, remainingTicks)
                    , 5);
        }
    }

    private void enforcePersonaShift() {
        Random random = new Random();

        for (World world : mixedCatastrophesData.getAffectedWorlds()) {
            if (random.nextDouble() >= 1 / 50.0)
                continue;

            ArrayList<Player> affectedPlayers = new ArrayList<>(world.getPlayers());

            if (affectedPlayers.size() < 2)
                continue;

            Collections.shuffle(affectedPlayers);

            for (int i = 0; i < affectedPlayers.size(); i++) {
                if (affectedPlayers.size() > 2 && random.nextDouble() < 0.75f) {
                    affectedPlayers.remove(i);
                    i--;
                }
            }

            Location nextLocation = affectedPlayers.get(affectedPlayers.size() - 1).getLocation();

            for (Player player : affectedPlayers) {
                Location location = player.getLocation();
                player.teleport(nextLocation);
                mixedCatastrophesData.getEventChangeManager()
                        .eventChange(player)
                        .withEventSound(Sound.ENTITY_ENDERMAN_TELEPORT)
                        .withEventMessage("I am someone else.")
                        .withColor(Constants.WeatherThemes.get(WeatherCatastropheType.PersonaShift).getColor())
                        .finish()
                        .execute();
                nextLocation = location;
            }
        }
    }

    private void enforceMeteorShower() {
        int players = 0;

        for (World world : mixedCatastrophesData.getAffectedWorlds())
            players += world.getPlayers().size();

        double weight = 1 + 0.25 * Math.pow(players, 0.5);
        double probability = weight / (5.0 + weight);

        if (new Random().nextDouble() < probability)
            mixedCatastrophesData.getRootCatastropheManager().getStarSplinterCatastropheManager()
                    .causeStarSplinter(StarSplinterType.Cobblestone, false);
    }

    public int getWeatherTimer() {
        return weatherTimer;
    }

    public WeatherCatastropheType getActiveWeather() {
        return activeWeather;
    }
}
