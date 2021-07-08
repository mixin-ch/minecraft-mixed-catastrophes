package ch.mixin.mixedCatastrophes.catastropheSettings;

import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.mixedCatastrophes.catastropheSettings.aspect.*;
import org.bukkit.configuration.ConfigurationSection;

public class AspectCatastropheSettings {
    private TerrorCatastropheSettings terror = new TerrorCatastropheSettings();
    private MisfortuneCatastropheSettings misfortune = new MisfortuneCatastropheSettings();
    private NatureConspiracyCatastropheSettings natureConspiracy = new NatureConspiracyCatastropheSettings();
    private CelestialFavorCatastropheSettings celestialFavor = new CelestialFavorCatastropheSettings();
    private GreyhatDebtCatastropheSettings greyhatDebt = new GreyhatDebtCatastropheSettings();
    private ResolveCatastropheSettings resolve = new ResolveCatastropheSettings();

    public void initialize(ConfigurationSection superSection) {
        ConfigurationSection aspectSection = superSection.getConfigurationSection("aspect");

        if (aspectSection == null)
            return;

        terror.initialize(aspectSection);
        misfortune.initialize( aspectSection);
        natureConspiracy.initialize(aspectSection);
        celestialFavor.initialize(aspectSection);
        greyhatDebt.initialize(aspectSection);
        resolve.initialize(aspectSection);
    }

    public void fillConfig(ConfigurationSection superSection) {
        ConfigurationSection aspectSection = superSection.createSection("aspect");

        terror.fillConfig(aspectSection);
        misfortune.fillConfig( aspectSection);
        natureConspiracy.fillConfig(aspectSection);
        celestialFavor.fillConfig(aspectSection);
        greyhatDebt.fillConfig(aspectSection);
        resolve.fillConfig(aspectSection);
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
