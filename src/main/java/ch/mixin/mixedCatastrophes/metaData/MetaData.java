package ch.mixin.mixedCatastrophes.metaData;

import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import ch.mixin.mixedCatastrophes.metaData.data.constructs.*;
import ch.mixin.mixedCatastrophes.metaData.data.PlayerData;
import ch.mixin.mixedCatastrophes.metaData.data.StarSplinterRemainsData;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MetaData {
    private boolean isActive;

    private int timeDistortionTimer;
    private int weatherTimer;
    private WeatherCatastropheType weatherCatastropheType;
    private int starSplinterTimer;
    private List<StarSplinterRemainsData> starSplinterRemainsDataList;
    private HashMap<UUID, PlayerData> playerDataMap;
    private List<GreenWellData> greenWellDataList;
    private List<BlitzardData> blitzardDataList;
    private List<LighthouseData> lighthouseDataList;
    private List<BlazeReactorData> blazeReactorDataList;
    private List<ScarecrowData> scarecrowDataList;
    private List<EnderRailData> enderRailDataList;
    private List<SeaPointData> seaPointDataList;

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

    public List<StarSplinterRemainsData> getStarSplinterRemainsDataList() {
        return starSplinterRemainsDataList;
    }

    public void setStarSplinterRemainsDataList(List<StarSplinterRemainsData> starSplinterRemainsDataList) {
        this.starSplinterRemainsDataList = starSplinterRemainsDataList;
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

    public List<LighthouseData> getLighthouseDataList() {
        return lighthouseDataList;
    }

    public void setLighthouseDataList(List<LighthouseData> lighthouseDataList) {
        this.lighthouseDataList = lighthouseDataList;
    }

    public List<BlazeReactorData> getBlazeReactorDataList() {
        return blazeReactorDataList;
    }

    public void setBlazeReactorDataList(List<BlazeReactorData> blazeReactorDataList) {
        this.blazeReactorDataList = blazeReactorDataList;
    }

    public List<ScarecrowData> getScarecrowDataList() {
        return scarecrowDataList;
    }

    public void setScarecrowDataList(List<ScarecrowData> scarecrowDataList) {
        this.scarecrowDataList = scarecrowDataList;
    }

    public List<EnderRailData> getEnderRailDataList() {
        return enderRailDataList;
    }

    public void setEnderRailDataList(List<EnderRailData> enderRailDataList) {
        this.enderRailDataList = enderRailDataList;
    }

    public List<SeaPointData> getSeaPointDataList() {
        return seaPointDataList;
    }

    public void setSeaPointDataList(List<SeaPointData> seaPointDataList) {
        this.seaPointDataList = seaPointDataList;
    }
}
