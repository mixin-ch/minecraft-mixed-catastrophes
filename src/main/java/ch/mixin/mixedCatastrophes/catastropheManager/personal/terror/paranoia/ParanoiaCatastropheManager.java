package ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.paranoia;

import ch.mixin.mixedCatastrophes.metaData.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.TerrorData;
import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.catastropheManager.RootCatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Random;

public class ParanoiaCatastropheManager extends CatastropheManager {
    private final static ArrayList<ParanoiaType> paranoiaOrder;
    private final static ArrayList<Sound> soundList;

    static {
        paranoiaOrder = new ArrayList<>();
        paranoiaOrder.add(ParanoiaType.Paralysis);
        paranoiaOrder.add(ParanoiaType.Weakness);
        paranoiaOrder.add(ParanoiaType.Vulnerability);
        paranoiaOrder.add(ParanoiaType.Insomnia);
        paranoiaOrder.add(ParanoiaType.Sounds);

        soundList = new ArrayList<>();
        soundList.add(Sound.ENTITY_ZOMBIE_AMBIENT);
        soundList.add(Sound.ENTITY_CREEPER_PRIMED);
        soundList.add(Sound.ENTITY_CREEPER_HURT);
        soundList.add(Sound.ENTITY_ENDERMAN_STARE);
        soundList.add(Sound.ENTITY_ENDERMAN_SCREAM);
        soundList.add(Sound.ENTITY_BLAZE_AMBIENT);
        soundList.add(Sound.ENTITY_GHAST_WARN);
        soundList.add(Sound.ENTITY_GHAST_HURT);
        soundList.add(Sound.ENTITY_ARROW_SHOOT);
    }

    public ParanoiaCatastropheManager(MixedCatastrophesPlugin plugin, RootCatastropheManager rootCatastropheManager) {
        super(plugin, rootCatastropheManager);
    }

    @Override
    public void initializeMetaData() {
    }

    public void initializePlayerData(PlayerData playerData) {
        TerrorData terrorData = playerData.getTerrorData();
        if (terrorData.getParanoiaTimer() <= 0) {
            terrorData.setParanoiaTimer(paranoiaTimer());
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

    public void tick(Player player) {
        PlayerData playerData = metaData.getPlayerDataMap().get(player.getUniqueId());
        TerrorData terrorData = playerData.getTerrorData();

        int timer = terrorData.getParanoiaTimer();
        timer--;

        if (timer <= 0) {
            timer = paranoiaTimer();

            ParanoiaType paranoiaType = getParanoia(playerData.getAspect(AspectType.Terror));

            if (paranoiaType != null) {
                causeParanoia(player, paranoiaType);
            }
        }

        terrorData.setParanoiaTimer(timer);
    }

    private int paranoiaTimer() {
        return Functions.random(20, 40);
    }

    private ParanoiaType getParanoia(int terror) {
        ArrayList<ParanoiaType> paranoiaTypeList = new ArrayList<>();

        if (check(terror, 0, 1, 200))
            paranoiaTypeList.add(ParanoiaType.Sounds);

        if (check(terror, 65, 1, 1200))
            paranoiaTypeList.add(ParanoiaType.Insomnia);

        if (check(terror, 135, 1, 1200))
            paranoiaTypeList.add(ParanoiaType.Weakness);

        if (check(terror, 100, 1, 1200))
            paranoiaTypeList.add(ParanoiaType.Vulnerability);

        if (check(terror, 35, 1, 1200))
            paranoiaTypeList.add(ParanoiaType.Paralysis);

        for (ParanoiaType paranoiaType : paranoiaOrder) {
            if (paranoiaTypeList.contains(paranoiaType))
                return paranoiaType;
        }

        return null;
    }

    private boolean check(int terror, int start, double multiplier, double half) {
        terror -= start;

        if (terror <= 0)
            return false;

        double probability = multiplier * (terror) / (terror + half);
        return new Random().nextDouble() < probability;
    }

    private void causeParanoia(Player player, ParanoiaType paranoiaType) {
        switch (paranoiaType) {
            case Sounds:
                sounds(player);
                break;
            case Insomnia:
                insomnia(player);
                break;
            case Weakness:
                weakness(player);
                break;
            case Vulnerability:
                vulnerability(player);
                break;
            case Paralysis:
                paralysis(player);
                break;
        }
    }

    private void sounds(Player player) {
        Sound sound = soundList.get(new Random().nextInt(soundList.size()));
        player.playSound(player.getLocation(), sound, 10.0f, 1.0f);
    }

    private void insomnia(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int time = 3 * 60;

        if (playerData.getDreamCooldown() < time) {
            playerData.setDreamCooldown(time);
        }

        plugin.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Nightmares keep you from sleeping.")
                .withCause(AspectType.Terror)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void weakness(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int extent = playerData.getAspect(AspectType.Terror) + new Random().nextInt(50);
        int time = 2 * 60;
        int amplitude = (int) Math.floor(Math.pow(0.005 * extent, 0.5));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time * 20, amplitude));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time * 20, amplitude));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time * 20, amplitude));

        plugin.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Dread pulls on your Muscles.")
                .withCause(AspectType.Terror)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void vulnerability(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int extent = playerData.getAspect(AspectType.Terror) + new Random().nextInt(50);
        int time = 2 * 60;
        int amplitude = (int) Math.floor(Math.pow(0.01 * extent, 0.5));

        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, time * 20, -(amplitude + 1)));

        plugin.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("You feel so vulnerable.")
                .withCause(AspectType.Terror)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void paralysis(Player player) {
        PlayerData playerData = plugin.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int extent = playerData.getAspect(AspectType.Terror) + new Random().nextInt(50);
        int time = 1 + (int) Math.round(Math.pow(0.05 * extent, 0.5));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time * 20, 10));
        player.removePotionEffect(PotionEffectType.JUMP);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time * 20, -5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time * 20, 10));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time * 20, 10));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time * 20, 0));

        plugin.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("An icy Touch paralyzes you.")
                .withCause(AspectType.Terror)
                .withTitle(true)
                .finish()
                .execute();
    }
}
