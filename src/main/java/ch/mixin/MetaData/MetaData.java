package ch.mixin.MetaData;

import com.google.gson.Gson;
import ch.mixin.main.MixedCatastrophesPlugin;
import ch.mixin.catastropheManager.global.weather.WeatherCatastropheType;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MetaData {
    private boolean isActive;

    private int timeDistortionTimer;
    private int weatherTimer;
    private WeatherCatastropheType weatherCatastropheType;
    private int starSplinterTimer;
    private HashMap<UUID, PlayerData> playerDataMap;
    private List<GreenWellData> greenWellDataList;
    private List<BlitzardData> blitzardDataList;

    public MetaData() {
        save();
    }

    public void save() {
        MixedCatastrophesPlugin.writeFile(MixedCatastrophesPlugin.METADATA_FILE, new Gson().toJson(this));
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTimeDistortionTimer() {
        return timeDistortionTimer;
    }

    public void setTimeDistortionTimer(int timeDistortionTimer) {
        this.timeDistortionTimer = timeDistortionTimer;
    }

    public int getWeatherTimer() {
        return weatherTimer;
    }

    public void setWeatherTimer(int weatherTimer) {
        this.weatherTimer = weatherTimer;
    }

    public WeatherCatastropheType getWeatherCatastropheType() {
        return weatherCatastropheType;
    }

    public void setWeatherCatastropheType(WeatherCatastropheType weatherCatastropheType) {
        this.weatherCatastropheType = weatherCatastropheType;
    }

    public int getStarSplinterTimer() {
        return starSplinterTimer;
    }

    public void setStarSplinterTimer(int starSplinterTimer) {
        this.starSplinterTimer = starSplinterTimer;
    }

    public HashMap<UUID, PlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public void setPlayerDataMap(HashMap<UUID, PlayerData> playerDataMap) {
        this.playerDataMap = playerDataMap;
    }

    public List<GreenWellData> getGreenWellDataList() {
        return greenWellDataList;
    }

    public void setGreenWellDataList(List<GreenWellData> greenWellDataList) {
        this.greenWellDataList = greenWellDataList;
    }

    public List<BlitzardData> getBlitzardDataList() {
        return blitzardDataList;
    }

    public void setBlitzardDataList(List<BlitzardData> blitzardDataList) {
        this.blitzardDataList = blitzardDataList;
    }
}
