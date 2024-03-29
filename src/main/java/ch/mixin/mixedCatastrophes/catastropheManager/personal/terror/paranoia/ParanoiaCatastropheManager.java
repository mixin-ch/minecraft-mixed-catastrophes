package ch.mixin.mixedCatastrophes.catastropheManager.personal.terror.paranoia;

import ch.mixin.mixedCatastrophes.main.MixedCatastrophesData;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.data.TerrorData;
import ch.mixin.mixedCatastrophes.catastropheManager.CatastropheManager;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Functions;
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

    public ParanoiaCatastropheManager(MixedCatastrophesData mixedCatastrophesData) {
        super(mixedCatastrophesData);
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

    public void tick(Player player, int severity) {
        if (!mixedCatastrophesData.getCatastropheSettings().getAspect().getTerror().isParanoia())
            return;

        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());
        TerrorData terrorData = playerData.getTerrorData();

        int timer = terrorData.getParanoiaTimer();
        timer -= 1 + 2 * severity;

        if (timer <= 0) {
            timer = paranoiaTimer();

            ParanoiaType paranoiaType = getParanoia(playerData.getAspect(AspectType.Terror) + severity * 100);

            if (paranoiaType != null)
                causeParanoia(player, paranoiaType, severity);
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

    private void causeParanoia(Player player, ParanoiaType paranoiaType, int severity) {
        if (mixedCatastrophesData.getCatastropheSettings().getAspect().getResolve().isVirtue()) {
            mixedCatastrophesData.getRootCatastropheManager().getPersonalCatastropheManager().getResolveCatastropheManager().mightShowVirtue(player, 0.05);
        }

        switch (paranoiaType) {
            case Sounds:
                sounds(player);
                break;
            case Insomnia:
                insomnia(player);
                break;
            case Weakness:
                weakness(player, severity);
                break;
            case Paralysis:
                paralysis(player, severity);
                break;
        }
    }

    private void sounds(Player player) {
        Sound sound = soundList.get(new Random().nextInt(soundList.size()));
        player.playSound(player.getLocation(), sound, 10.0f, 1.0f);
    }

    private void insomnia(Player player) {
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int time = 3 * 60;

        if (playerData.getDreamCooldown() < time) {
            playerData.setDreamCooldown(time);
        }

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Nightmares keep you from sleeping.")
                .withCause(AspectType.Terror)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void weakness(Player player, int severity) {
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int extent = playerData.getAspect(AspectType.Terror) + severity * 50 + new Random().nextInt(50);
        int time = (int) Math.floor(60 * Math.pow(0.005 * extent, 0.5));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time * 20, 0));

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("The Dread pulls on your Muscles.")
                .withCause(AspectType.Terror)
                .withTitle(true)
                .finish()
                .execute();
    }

    private void paralysis(Player player, int severity) {
        PlayerData playerData = mixedCatastrophesData.getMetaData().getPlayerDataMap().get(player.getUniqueId());

        int extent = playerData.getAspect(AspectType.Terror) + severity * 50 + new Random().nextInt(50);
        int time = 1 + (int) Math.round(Math.pow(0.05 * extent, 0.5));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, time * 20, 10));
        player.removePotionEffect(PotionEffectType.JUMP);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, time * 20, -5));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, time * 20, 10));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, time * 20, 10));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, time * 20, 0));

        mixedCatastrophesData.getEventChangeManager()
                .eventChange(player)
                .withEventSound(Sound.AMBIENT_CAVE)
                .withEventMessage("An icy Touch paralyzes you.")
                .withCause(AspectType.Terror)
                .withTitle(true)
                .finish()
                .execute();
    }
}
