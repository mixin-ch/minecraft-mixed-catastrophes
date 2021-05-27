package ch.mixin.mixedCatastrophes.catastropheManager.global.weather;

public enum WeatherCatastropheType {
    Nothing,
    RadiantSky,
    ThunderStorm,
    SearingCold,
    GravityLoss,
    CatsAndDogs,
    ;

    public static WeatherCatastropheType convert(String string) {
        for (WeatherCatastropheType weatherCatastropheType : WeatherCatastropheType.values()) {
            if (weatherCatastropheType.toString().equalsIgnoreCase(string)) {
                return weatherCatastropheType;
            }
        }

        return null;
    }
}
