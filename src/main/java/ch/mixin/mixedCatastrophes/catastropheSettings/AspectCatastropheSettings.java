package ch.mixin.mixedCatastrophes.catastropheSettings;

import ch.mixin.mixedCatastrophes.catastropheSettings.aspect.*;
import org.bukkit.configuration.ConfigurationSection;

public class AspectCatastropheSettings {
    private TerrorCatastropheSettings terror;
    private MisfortuneCatastropheSettings misfortune;
    private NatureConspiracyCatastropheSettings natureConspiracy;
    private CelestialFavorCatastropheSettings celestialFavor;
    private GreyhatDebtCatastropheSettings greyhatDebt;
    private NobilityCatastropheSettings nobility;

    public void initialize(ConfigurationSection configuration) {
        ConfigurationSection terrorSection = configuration.getConfigurationSection("terror");
        if (terrorSection != null) {
            terror.initialize(terrorSection);
        }

        ConfigurationSection misfortuneSection = configuration.getConfigurationSection("misfortune");
        if (misfortuneSection != null) {
            misfortune.initialize(misfortuneSection);
        }

        ConfigurationSection natureConspiracySection = configuration.getConfigurationSection("natureConspiracy");
        if (natureConspiracySection != null) {
            natureConspiracy.initialize(natureConspiracySection);
        }

        ConfigurationSection celestialFavorSection = configuration.getConfigurationSection("celestialFavor");
        if (celestialFavorSection != null) {
            celestialFavor.initialize(celestialFavorSection);
        }

        ConfigurationSection greyhatDebtSection = configuration.getConfigurationSection("greyhatDebt");
        if (greyhatDebtSection != null) {
            greyhatDebt.initialize(greyhatDebtSection);
        }

        ConfigurationSection nobilitySection = configuration.getConfigurationSection("nobility");
        if (nobilitySection != null) {
            nobility.initialize(nobilitySection);
        }
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
