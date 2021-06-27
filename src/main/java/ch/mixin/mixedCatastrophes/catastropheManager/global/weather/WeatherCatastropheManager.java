package ch.mixin.mixedCatastrophes.catastropheManager.global.weather;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesManagerAccessor;
import ch.mixin.mixedCatastrophes.metaData.constructs.BlitzardData;
import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate2D;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    }

    private int weatherTimer;
    private WeatherCatastropheType activeWeather;

    public WeatherCatastropheManager(MixedCatastrophesManagerAccessor mixedCatastrophesManagerAccessor) {
        super(mixedCatastrophesManagerAccessor);
    }

    @Override
    public void initializeMetaData() {
        if (metaData.getWeatherTimer() <= 0) {
            metaData.setWeatherCatastropheType(WeatherCatastropheType.Nothing);
            metaData.setWeatherTimer(weatherTimer(WeatherCatastropheType.Nothing));
        }
    }

    @Override
    public void updateMetaData() {
        metaData.setWeatherTimer(weatherTimer);
        metaData.setWeatherCatastropheType(activeWeather);
    }

    @Override
    public void initializeCauser() {
        weatherTimer = metaData.getWeatherTimer();
        activeWeather = metaData.getWeatherCatastropheType();
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
        }

        return 0;
    }

    @Override
    public void tick() {
        weatherTimer--;

        if (weatherTimer <= 0) {
            WeatherCatastropheType newWeather;

            if (activeWeather == WeatherCatastropheType.Nothing) {
                newWeather = Functions.getRandomWithWeights(catastropheWeights);
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
        ChatColor color = Constants.WeatherThemes.get(weatherCatastropheType).getColor();

        switch (weatherCatastropheType) {
            case Nothing:
                break;
            case RadiantSky:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "The Sky is scattered with Rays of Radiance."
                        , color);
                break;
            case ThunderStorm:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "Erratic Lightning licks across the Heavens."
                        , color);
                break;
            case SearingCold:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "The creeping Cold tears the Flesh."
                        , color);
                break;
            case GravityLoss:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "The Ground can not hold you."
                        , color);
                break;
            case CatsAndDogs:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "It is raining Cats and Dogs."
                        , color);
                break;
        }
    }

    private void stopWeather(WeatherCatastropheType weatherCatastropheType) {
        ChatColor color = Constants.WeatherThemes.get(weatherCatastropheType).getColor();

        switch (weatherCatastropheType) {
            case Nothing:
                break;
            case RadiantSky:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "The Sky is no longer radiant."
                        , color);
                break;
            case ThunderStorm:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "The thunderous Roaring ceases."
                        , color);
                break;
            case SearingCold:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "The icy Winds vanish."
                        , color);
                break;
            case GravityLoss:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "Gravity grips you again."
                        , color);
                break;
            case CatsAndDogs:
                playEffect(
                        Sound.AMBIENT_CAVE
                        , "The last Barks pass by."
                        , color);
                break;
        }
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
        }
    }

    private void playEffect(Sound sound, String message, ChatColor chatColor) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            mixedCatastrophesManagerAccessor.getEventChangeManager()
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
        for (World world : mixedCatastrophesManagerAccessor.getAffectedWorlds()) {
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
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> particleChain(particle, remainingTicks)
                    , 5);
        }
    }

    private void enforceRadiantSky() {
        for (World world : mixedCatastrophesManagerAccessor.getAffectedWorlds()) {
            ArrayList<Coordinate2D> spaces = new ArrayList<>();

            for (Player player : world.getPlayers()) {
                if (!Functions.isNight(player.getWorld())) {
                    Location playerLocation = player.getLocation();
                    Coordinate2D center = new Coordinate2D(playerLocation.getBlockX(), playerLocation.getBlockZ());
                    spaces = Functions.merge(spaces, Functions.getSphere2D(center, 20));

                    if (!Functions.hasShelter(player)) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 0));

                        if (player.getWorld().getTime() % (3 * 20) < 20) {
                            player.setFireTicks(20);
                        }
                    }
                }
            }

            for (Coordinate2D space : spaces) {
                Location roof = Functions.absoluteRoof(world, space);
                if (roof == null)
                    continue;

                Location roofAbove = Functions.offset(roof, 1);
                if (roofAbove == null)
                    continue;

                if (new Random().nextDouble() < 1 / (double) 1000) {
                    if (Constants.Airs.contains(roofAbove.getBlock().getType())) {
                        roofAbove.getBlock().setType(Material.FIRE);
                    }
                }
            }
        }
    }

    private void enforceThunderStorm() {
        HashMap<UUID, PlayerData> pcm = metaData.getPlayerDataMap();
        ArrayList<Location> targets = new ArrayList<>();
        HashMap<Location, Integer> blitzardMap = new HashMap<>();

        for (BlitzardData blitzardData : metaData.getBlitzardDataList()) {
            World world = plugin.getServer().getWorld(blitzardData.getWorldName());

            if (world == null)
                continue;

            Location location = blitzardData.getPosition().toLocation(world);

            if (!Constants.Blitzard.checkConstructed(location).isConstructed())
                continue;

            blitzardMap.put(location, blitzardData.getLevel() * Constants.BlitzardRangeFactor);
        }

        for (World world : mixedCatastrophesManagerAccessor.getAffectedWorlds()) {
            ArrayList<Coordinate2D> spaces = new ArrayList<>();

            for (Player player : world.getPlayers()) {
                Location location = player.getLocation();
                Coordinate2D center = Coordinate2D.convert(location);
                spaces = Functions.merge(spaces, Functions.getSphere2D(center, 20));

                if (Functions.hasShelter(player))
                    continue;

                int misfortune = pcm.get(player.getUniqueId()).getAspect(AspectType.Misfortune);
                double probability = misfortune / (20.0 + misfortune);

                if (new Random().nextDouble() < probability) {
                    targets.add(rerouteBlitzard(blitzardMap, location));
                }
            }

            for (Coordinate2D space : spaces) {
                if (new Random().nextDouble() < 1 / (double) 1250) {
                    Location roofAbove = Functions.offset(Functions.absoluteRoof(world, space), 1);

                    if (roofAbove == null)
                        continue;

                    targets.add(rerouteBlitzard(blitzardMap, roofAbove));
                }
            }
        }

        lightningChain(targets, 4);
    }

    private Location rerouteBlitzard(HashMap<Location, Integer> blitzardMap, Location baseLocation) {
        Location strongestBlitzard = null;
        double strongestPull = -1;

        for (Location blitzardLocation : blitzardMap.keySet()) {
            if (blitzardLocation.getWorld() != baseLocation.getWorld())
                continue;

            double pull = blitzardMap.get(blitzardLocation) - blitzardLocation.distance(baseLocation);

            if (pull < 0)
                continue;

            if (pull <= strongestPull)
                continue;

            strongestPull = pull;
            strongestBlitzard = blitzardLocation;
        }

        if (strongestBlitzard != null) {
            return Functions.absoluteRoof(strongestBlitzard.getWorld(), Coordinate2D.convert(strongestBlitzard));
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
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> lightningChain(targets, remainingTicks)
                    , 5);
        }
    }

    private void enforceSearingCold() {
        for (World world : mixedCatastrophesManagerAccessor.getAffectedWorlds()) {
            ArrayList<Coordinate2D> spaces = new ArrayList<>();

            for (Player player : world.getPlayers()) {
                Location playerLocation = player.getLocation();
                Coordinate2D center = new Coordinate2D(playerLocation.getBlockX(), playerLocation.getBlockZ());
                spaces = Functions.merge(spaces, Functions.getSphere2D(center, 20));

                boolean warm = Constants.HotItems.contains(player.getInventory().getItemInMainHand().getType())
                        || Constants.HotItems.contains(player.getInventory().getItemInOffHand().getType());

                if (!warm) {
                    sphere:
                    for (Location location : Functions.getSphereLocation(player.getLocation(), 7)) {
                        Material material = location.getBlock().getType();
                        double distance = location.distance(player.getLocation());

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

            for (Coordinate2D space : spaces) {
                Location roof = Functions.absoluteRoof(world, space);
                if (roof == null)
                    continue;

                Location roofAbove = Functions.offset(roof, 1);

                if (new Random().nextDouble() < 1 / (double) 250) {
                    if (roof.getBlock().getType() == Material.WATER) {
                        ArrayList<Location> neighbours = new ArrayList<>();
                        neighbours.add(Functions.offset(roof, 1, 0, 0));
                        neighbours.add(Functions.offset(roof, -1, 0, 0));
                        neighbours.add(Functions.offset(roof, 0, 0, 1));
                        neighbours.add(Functions.offset(roof, 0, 0, -1));

                        for (Location neighbour : neighbours) {
                            if (neighbour.getBlock().getType() != Material.WATER) {
                                roof.getBlock().setType(Material.ICE);
                                break;
                            }
                        }
                    } else if (roofAbove != null && Constants.Airs.contains(roofAbove.getBlock().getType())) {
                        roofAbove.getBlock().setType(Material.SNOW);
                    }
                }
            }
        }
    }

    private void enforceGravityLoss() {
        for (World world : mixedCatastrophesManagerAccessor.getAffectedWorlds()) {
            for (Player player : world.getPlayers()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 2 * 20, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 12 * 20, 0));
            }
        }
    }

    private void enforceCatsAndDogs() {
        ArrayList<Location> targets = new ArrayList<>();

        for (World world : mixedCatastrophesManagerAccessor.getAffectedWorlds()) {
            ArrayList<Coordinate2D> spaces = new ArrayList<>();

            for (Player player : world.getPlayers()) {
                Location playerLocation = player.getLocation();
                Coordinate2D center = new Coordinate2D(playerLocation.getBlockX(), playerLocation.getBlockZ());
                spaces = Functions.merge(spaces, Functions.getSphere2D(center, 20));
            }

            for (Coordinate2D space : spaces) {
                if (new Random().nextDouble() < 1 / (double) 1250) {
                    targets.add(space.to3D(world.getMaxHeight() + 64).toLocation(world));
                }
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
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> catsAndDogsChain(targets, remainingTicks)
                    , 5);
        }
    }
}
