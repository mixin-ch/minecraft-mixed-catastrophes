package ch.mixin.mixedCatastrophes.metaData.data;

import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private UUID playerId;
    private String playerName;
    private int dreamCooldown;
    private int antiLighthouseTimer;
    private int skyScornTimer;
    private HashMap<AspectType, Integer> aspects = new HashMap<>();
    private TerrorData terrorData = new TerrorData();

    public PlayerData(UUID playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
        fillAspects();
    }

    public void fillAspects(){
        for (AspectType aspectType : AspectType.values()) {
            if (!aspects.containsKey(aspectType)) {
                aspects.put(aspectType, 0);
            }
        }
    }

    public int getAspect(AspectType aspectType) {
        return aspects.get(aspectType);
    }

    public void setAspect(AspectType aspectType, int value) {
        aspects.put(aspectType, value);
    }

    public void addAspect(AspectType aspectType, int value) {
        aspects.put(aspectType, aspects.get(aspectType) + value);
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getDreamCooldown() {
        return dreamCooldown;
    }

    public void setDreamCooldown(int dreamCooldown) {
        this.dreamCooldown = dreamCooldown;
    }

    public int getAntiLighthouseTimer() {
        return antiLighthouseTimer;
    }

    public void setAntiLighthouseTimer(int antiLighthouseTimer) {
        this.antiLighthouseTimer = antiLighthouseTimer;
    }

    public int getSkyScornTimer() {
        return skyScornTimer;
    }

    public void setSkyScornTimer(int skyScornTimer) {
        this.skyScornTimer = skyScornTimer;
    }

    public HashMap<AspectType, Integer> getAspects() {
        return aspects;
    }

    public void setAspects(HashMap<AspectType, Integer> aspects) {
        this.aspects = aspects;
    }

    public TerrorData getTerrorData() {
        return terrorData;
    }

    public void setTerrorData(TerrorData terrorData) {
        this.terrorData = terrorData;
    }
}
