package ch.mixin.mixedCatastrophes.catastropheSettings;

import ch.mixin.mixedCatastrophes.catastropheSettings.aspect.*;
import org.bukkit.configuration.ConfigurationSection;

public class AspectCatastropheSettings {
    private TerrorCatastropheSettings terror = new TerrorCatastropheSettings();
    private MisfortuneCatastropheSettings misfortune = new MisfortuneCatastropheSettings();
    private NatureConspiracyCatastropheSettings natureConspiracy = new NatureConspiracyCatastropheSettings();
    private CelestialFavorCatastropheSettings celestialFavor = new CelestialFavorCatastropheSettings();
    private GreyhatDebtCatastropheSettings greyhatDebt = new GreyhatDebtCatastropheSettings();
    private NobilityCatastropheSettings nobility = new NobilityCatastropheSettings();

    public AspectCatastropheSettings() {
    }

    public AspectCatastropheSettings(ConfigurationSection configuration) {
        initialize(configuration);
    }

    public void initialize(ConfigurationSection configuration) {
        if (configuration == null)
            return;

        ConfigurationSection terrorSection = configuration.getConfigurationSection("terror");
        terror = new TerrorCatastropheSettings(terrorSection);

        ConfigurationSection misfortuneSection = configuration.getConfigurationSection("misfortune");
        misfortune = new MisfortuneCatastropheSettings(terrorSection);

        ConfigurationSection natureConspiracySection = configuration.getConfigurationSection("natureConspiracy");
        natureConspiracy = new NatureConspiracyCatastropheSettings(terrorSection);

        ConfigurationSection celestialFavorSection = configuration.getConfigurationSection("celestialFavor");
        celestialFavor = new CelestialFavorCatastropheSettings(terrorSection);

        ConfigurationSection greyhatDebtSection = configuration.getConfigurationSection("greyhatDebt");
        greyhatDebt = new GreyhatDebtCatastropheSettings(terrorSection);

        ConfigurationSection nobilitySection = configuration.getConfigurationSection("nobility");
        nobility = new NobilityCatastropheSettings(terrorSection);
    }

    public TerrorCatastropheSettings getTerror() {
        return terror;
    }

    public void setTerror(TerrorCatastropheSettings terror) {
        this.terror = terror;
    }

    public MisfortuneCatastropheSettings getMisfortune() {
        return misfortune;
    }

    public void setMisfortune(MisfortuneCatastropheSettings misfortune) {
        this.misfortune = misfortune;
    }

    public NatureConspiracyCatastropheSettings getNatureConspiracy() {
        return natureConspiracy;
    }

    public void setNatureConspiracy(NatureConspiracyCatastropheSettings natureConspiracy) {
        this.natureConspiracy = natureConspiracy;
    }

    public CelestialFavorCatastropheSettings getCelestialFavor() {
        return celestialFavor;
    }

    public void setCelestialFavor(CelestialFavorCatastropheSettings celestialFavor) {
        this.celestialFavor = celestialFavor;
    }

    public GreyhatDebtCatastropheSettings getGreyhatDebt() {
        return greyhatDebt;
    }

    public void setGreyhatDebt(GreyhatDebtCatastropheSettings greyhatDebt) {
        this.greyhatDebt = greyhatDebt;
    }

    public NobilityCatastropheSettings getNobility() {
        return nobility;
    }

    public void setNobility(NobilityCatastropheSettings nobility) {
        this.nobility = nobility;
    }
}
