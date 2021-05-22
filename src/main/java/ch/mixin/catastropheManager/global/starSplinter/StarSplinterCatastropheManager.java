package ch.mixin.catastropheManager.global.starSplinter;

import ch.mixin.catastropheManager.CatastropheManager;
import ch.mixin.catastropheManager.RootCatastropheManager;
import ch.mixin.helperClasses.Coordinate2D;
import ch.mixin.helperClasses.Functions;
import ch.mixin.main.MixedCatastrophesPlugin;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class StarSplinterCatastropheManager extends CatastropheManager {
    private static final HashMap<StarSplinterPremise, Double> starSplinterWeights;

    static {
        starSplinterWeights = new HashMap<>();
        starSplinterWeights.put(new StarSplinterPremise(
                "Iron", Material.IRON_NUGGET, 9.0
        ), 4.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Gold", Material.GOLD_NUGGET, 9.0
        ), 4.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Diamond", Material.DIAMOND, 1.0
        ), 1.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Netherite", Material.NETHERITE_SCRAP, 4.0
        ), 1.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Emerald", Material.EMERALD, 1.0
        ), 1.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Redstone", Material.REDSTONE, 1.0
        ), 2.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Lapis Lazuli", Material.LAPIS_LAZULI, 1.0
        ), 2.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Quartz", Material.QUARTZ, 4.0
        ), 4.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Glowstone", Material.GLOWSTONE_DUST, 4.0
        ), 2.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Flint", Material.FLINT, 1.0
        ), 2.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Blaze", Material.BLAZE_POWDER, 1.0
        ), 1.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Ender Pearl", Material.ENDER_PEARL, 1.0
        ), 1.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Obsidian", Material.OBSIDIAN, 1.0
        ), 1.0);
        starSplinterWeights.put(new StarSplinterPremise(
                "Clay", Material.CLAY_BALL, 4.0
        ), 2.0);
    }

    private int starSplinterTimer;

    public StarSplinterCatastropheManager(MixedCatastrophesPlugin plugin, RootCatastropheManager rootCatastropheManager) {
        super(plugin, rootCatastropheManager);
    }

    @Override
    public void initializeMetaData() {
        if (metaData.getStarSplinterTimer() <= 0) {
            metaData.setStarSplinterTimer(starSplinterTimer());
        }
    }

    @Override
    public void updateMetaData() {

    }

    @Override
    public void initializeCauser() {
        starSplinterTimer = metaData.getStarSplinterTimer();
    }

    private int starSplinterTimer() {
        return Functions.random(20, 40);
    }

    @Override
    public void tick() {
        starSplinterTimer--;

        if (starSplinterTimer <= 0) {
            starSplinterTimer = starSplinterTimer();
            ArrayList<Player> playerList = new ArrayList<>();

            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (plugin.getAffectedWorlds().contains(p.getWorld())) {
                    playerList.add(p);
                }
            }

            double multiplier = Math.pow(playerList.size(), 0.5);
            double probability = multiplier / (200.0 + multiplier);

            if (new Random().nextDouble() < probability) {
                causeStarSplinter();
            }
        }
    }

    public void causeStarSplinter() {
        ArrayList<Player> playerList = new ArrayList<>();

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (plugin.getAffectedWorlds().contains(p.getWorld())) {
                playerList.add(p);
            }
        }

        if (playerList.size() == 0)
            return;

        Player player = playerList.get(new Random().nextInt(playerList.size()));
        causeStarSplinter(player);
    }

    public void causeStarSplinter(Player player) {
        StarSplinterPremise starSplinterPremise = Functions.getRandomWithWeights(starSplinterWeights);
        World world = player.getWorld();
        List<Location> locations = new ArrayList<>();
        ArrayList<Player> playerList = new ArrayList<>();

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (world == p.getWorld()) {
                playerList.add(p);
            }
        }

        for (Coordinate2D space : Functions.getSphere2D(Coordinate2D.convert(player.getLocation()), 50)) {
            Location roof = Functions.absoluteRoof(world, space);
            if (roof == null)
                continue;

            Location roofAbove = Functions.offset(roof, 1);
            if (roofAbove == null)
                continue;

            locations.add(roofAbove);
        }

        Location location = locations.get(new Random().nextInt(locations.size()));

        world.strikeLightning(location);
        world.createExplosion(location, 4, true, true);

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> spawnSplinterRemains(starSplinterPremise, location)
                , 20 * 1);

        fireworkChain(location, 30);

        for (Player p : playerList) {
            p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 10.0f, 1.0f);
            plugin.getEventChangeManager()
                    .eventChange(p)
                    .withEventMessage("A Star-Splinter of " + starSplinterPremise.getName() + ".")
                    .withColor(ChatColor.AQUA)
                    .withTitle(true)
                    .finish()
                    .execute();
        }
    }

    private void fireworkChain(Location location, int amount) {
        spawnFirework(location, new Random().nextInt(20), Color.WHITE);
        amount--;

        if (amount > 0) {
            int finalAmount = amount;
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> fireworkChain(location, finalAmount)
                    , 20);
        }
    }

    private void spawnFirework(Location location, int power, Color color) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(power);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(false).build());
        fw.setFireworkMeta(fwm);
    }

    private void spawnSplinterRemains(StarSplinterPremise starSplinterPremise, Location location) {
        World world = location.getWorld();
        Material material = starSplinterPremise.getMaterial();
        int amount = (int) Math.round(100 * starSplinterPremise.getMultiplier());
        ArrayList<Item> items = new ArrayList<>();

        world.spawnEntity(location, EntityType.ENDER_CRYSTAL);

        for (int i = 0; i < amount; i++) {
            Item item = world.dropItem(location, new ItemStack(material, 1));
            item.setPickupDelay(20 * 100);

            Vector vector = Vector.getRandom();
            vector.setX(vector.getX() * 2 - 1);
            vector.setY(Math.abs(vector.getY()) * 2);
            vector.setZ(vector.getZ() * 2 - 1);
            item.setVelocity(vector.clone().multiply(2));

            ItemStack itemStack = item.getItemStack();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(Collections.singletonList(Integer.toString(i)));
            itemStack.setItemMeta(itemMeta);

            items.add(item);
        }

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> resetItemLores(items)
                , 20 * 5);
    }

    private void resetItemLores(ArrayList<Item> items) {
        for (Item item : items) {
            ItemStack itemStack = item.getItemStack();
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta == null)
                return;

            itemMeta.setLore(new ArrayList<>());
            item.setPickupDelay(0);
            itemStack.setItemMeta(itemMeta);
        }
    }
}
