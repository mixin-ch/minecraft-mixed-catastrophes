package ch.mixin.mixedCatastrophes.catastropheSettings;

import ch.mixin.mixedCatastrophes.catastropheSettings.aspect.*;
import org.bukkit.configuration.ConfigurationSection;

public class AspectCatastropheSettings {
    private TerrorCatastropheSettings terror = new TerrorCatastropheSettings();
    private MisfortuneCatastropheSettings misfortune = new MisfortuneCatastropheSettings();
    private NatureConspiracyCatastropheSettings natureConspiracy = new NatureConspiracyCatastropheSettings();
    private CelestialFavorCatastropheSettings celestialFavor = new CelestialFavorCatastropheSettings();
    private GreyhatDebtCatastropheSettings greyhatDebt = new GreyhatDebtCatastropheSettings();
    private ResolveCatastropheSettings resolve = new ResolveCatastropheSettings();

    public void initialize(ConfigurationSection configuration) {
        if (configuration == null)
            return;

        terror.initialize(configuration.getConfigurationSection("terror"));
        misfortune.initialize( configuration.getConfigurationSection("misfortune"));
        natureConspiracy.initialize(configuration.getConfigurationSection("natureConspiracy"));
        celestialFavor.initialize(configuration.getConfigurationSection("celestialFavor"));
        greyhatDebt.initialize(configuration.getConfigurationSection("greyhatDebt"));
        resolve.initialize(configuration.getConfigurationSection("resolve"));
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

    public ResolveCatastropheSettings getResolve() {
        return resolve;
    }

    public void setResolve(ResolveCatastropheSettings resolve) {
        this.resolve = resolve;
    }
}
