package ch.mixin.mixedCatastrophes.metaData.data;

import ch.mixin.mixedCatastrophes.catastropheManager.global.starSplinter.StarSplinterType;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;

public class StarSplinterRemainsData {
    private StarSplinterType starSplinterType;
    private String worldName;
    private Coordinate3D position;

    public StarSplinterRemainsData(StarSplinterType starSplinterType, String worldName, Coordinate3D position) {
        this.starSplinterType = starSplinterType;
        this.worldName = worldName;
        this.position = position;
    }

    public StarSplinterType getStarSplinterType() {
        return starSplinterType;
    }

    public void setStarSplinterType(StarSplinterType starSplinterType) {
        this.starSplinterType = starSplinterType;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Coordinate3D getPosition() {
        return position;
    }

    public void setPosition(Coordinate3D position) {
        this.position = position;
    }
}
