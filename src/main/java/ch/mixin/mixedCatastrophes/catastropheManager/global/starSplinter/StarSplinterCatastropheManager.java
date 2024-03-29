package ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter;

import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate2D;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.StarSplinterRemainsData;
import net.md_5.bungee.api.ChatColor;
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
    public static final HashMap<StarSplinterType, Double> starSplinterWeightMap;
    public static final HashMap<StarSplinterType, StarSplinterPremise> starSplinterPremiseMap;

    static {
        starSplinterWeightMap = new HashMap<>();
        starSplinterWeightMap.put(StarSplinterType.Cobblestone, 0.0);
        starSplinterWeightMap.put(StarSplinterType.Iron, 4.0);
        starSplinterWeightMap.put(StarSplinterType.Gold, 4.0);
        starSplinterWeightMap.put(StarSplinterType.Diamond, 1.0);
        starSplinterWeightMap.put(StarSplinterType.Emerald, 1.0);
        starSplinterWeightMap.put(StarSplinterType.Redstone, 2.0);
        starSplinterWeightMap.put(StarSplinterType.Lapis_Lazuli, 2.0);
        starSplinterWeightMap.put(StarSplinterType.Quartz, 4.0);
        starSplinterWeightMap.put(StarSplinterType.Glowstone, 2.0);
        starSplinterWeightMap.put(StarSplinterType.Flint, 2.0);
        starSplinterWeightMap.put(StarSplinterType.Blaze, 1.0);
        starSplinterWeightMap.put(StarSplinterType.Ender_Pearl, 1.0);
        starSplinterWeightMap.put(StarSplinterType.Obsidian, 1.0);
        starSplinterWeightMap.put(StarSplinterType.Clay, 2.0);
        starSplinterWeightMap.put(StarSplinterType.Feather, 1.0);

        starSplinterPremiseMap = new HashMap<>();
        starSplinterPremiseMap.put(StarSplinterType.Cobblestone, new StarSplinterPremise(
                "Cobblestone", Material.COBBLESTONE, 4.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Iron, new StarSplinterPremise(
                "Iron", Material.IRON_NUGGET, 9.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Gold, new StarSplinterPremise(
                "Gold", Material.GOLD_NUGGET, 9.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Diamond, new StarSplinterPremise(
                "Diamond", Material.DIAMOND, 1.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Netherite, new StarSplinterPremise(
                "Netherite", Material.NETHERITE_SCRAP, 4.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Emerald, new StarSplinterPremise(
                "Emerald", Material.EMERALD, 1.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Redstone, new StarSplinterPremise(
                "Redstone", Material.REDSTONE, 1.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Lapis_Lazuli, new StarSplinterPremise(
                "Lapis Lazuli", Material.LAPIS_LAZULI, 1.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Quartz, new StarSplinterPremise(
                "Quartz", Material.QUARTZ, 4.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Glowstone, new StarSplinterPremise(
                "Glowstone", Material.GLOWSTONE_DUST, 4.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Flint, new StarSplinterPremise(
                "Flint", Material.FLINT, 1.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Blaze, new StarSplinterPremise(
                "Blaze", Material.BLAZE_ROD, 1.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Ender_Pearl, new StarSplinterPremise(
                "Ender Pearl", Material.ENDER_PEARL, 1.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Obsidian, new StarSplinterPremise(
                "Obsidian", Material.OBSIDIAN, 1.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Clay, new StarSplinterPremise(
                "Clay", Material.CLAY_BALL, 4.0
        ));
        starSplinterPremiseMap.put(StarSplinterType.Feather, new StarSplinterPremise(
                "Feather", Material.FEATHER, 1.0
        ));

    }

    private int starSplinterTimer;

    public StarSplinterCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
    }

    @Override
    public void initializeMetaData() {
        if (mixedCatastrophesData.getMetaData().getStarSplinterTimer() <= 0) {
            mixedCatastrophesData.getMetaData().setStarSplinterTimer(starSplinterTimer());
        }
        if (mixedCatastrophesData.getMetaData().getStarSplinterRemainsDataList() == null) {
            mixedCatastrophesData.getMetaData().setStarSplinterRemainsDataList(new ArrayList<>());
        }
    }

    @Override
    public void updateMetaData() {

    }

    @Override
    public void initializeCauser() {
        starSplinterTimer = mixedCatastrophesData.getMetaData().getStarSplinterTimer();
    }

    private int starSplinterTimer() {
        return Functions.random(10, 20);
    }

    @Override
    public void tick() {
        if (!mixedCatastrophesData.getCatastropheSettings().isStarSplinter())
            return;

        starSplinterTimer--;

        if (starSplinterTimer <= 0) {
            starSplinterTimer = starSplinterTimer();

            double probability = 15 / mixedCatastrophesData.getCatastropheSettings().getStarSplinterInterval();

            if (new Random().nextDouble() < probability)
                causeStarSplinter(true);
        }
    }

    public void causeStarSplinter(boolean spectacular) {
        StarSplinterType starSplinterType = Functions.getRandomWithWeights(starSplinterWeightMap);
        causeStarSplinter(starSplinterType, spectacular);
    }

    public void causeStarSplinter(Player player, boolean spectacular) {
        StarSplinterType starSplinterType = Functions.getRandomWithWeights(starSplinterWeightMap);
        causeStarSplinter(player, starSplinterType, spectacular);
    }

    public void causeStarSplinter(StarSplinterType starSplinterType, boolean spectacular) {
        ArrayList<Player> playerList = new ArrayList<>();

        for (Player p : mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers()) {
            if (mixedCatastrophesData.getAffectedWorlds().contains(p.getWorld())) {
                playerList.add(p);
            }
        }

        if (playerList.size() == 0)
            return;

        Player player = playerList.get(new Random().nextInt(playerList.size()));
        causeStarSplinter(player, starSplinterType, spectacular);
    }

    public void causeStarSplinter(Player player, StarSplinterType starSplinterType, boolean spectacular) {
        StarSplinterPremise starSplinterPremise = starSplinterPremiseMap.get(starSplinterType);
        World world = player.getWorld();
        List<Location> locations = new ArrayList<>();
        ArrayList<Player> playerList = new ArrayList<>();

        for (Player p : mixedCatastrophesData.getPlugin().getServer().getOnlinePlayers()) {
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

        mixedCatastrophesData.getMetaData().getStarSplinterRemainsDataList().add(new StarSplinterRemainsData(starSplinterType, location.getWorld().getName(), Coordinate3D.toCoordinate(location)));

        world.strikeLightning(location);
        world.createExplosion(location, 4, true, true);

        mixedCatastrophesData.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(mixedCatastrophesData.getPlugin(), () -> spawnSplinterRemains(starSplinterPremise, location)
                , 20 * 1);

        if (!spectacular)
            return;

        fireworkChain(location, 30);

        for (Player p : playerList) {
            p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 10.0f, 1.0f);
            mixedCatastrophesData.getEventChangeManager()
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
            mixedCatastrophesData.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(mixedCatastrophesData.getPlugin(), () -> fireworkChain(location, finalAmount)
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

        mixedCatastrophesData.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(mixedCatastrophesData.getPlugin(), () -> resetItemLores(items)
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
