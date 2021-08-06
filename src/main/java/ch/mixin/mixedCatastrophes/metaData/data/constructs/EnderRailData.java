package ch.mixin.mixedCatastrophes.metaData.data.constructs;

import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.ConstructShape;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.helperClasses.ShapeCompareResult;
import ch.mixin.mixedCatastrophes.metaData.EnderRailDirection;
import org.bukkit.World;

public class EnderRailData extends ConstructData {
    private int level;
    private int rotation;
    private EnderRailDirection direction;

    public EnderRailData(Coordinate3D position, String worldName, int level, int rotation, EnderRailDirection direction) {
        super(position, worldName,0);
        this.level = level;
        this.rotation = rotation;
        this.direction = direction;
    }

    public ShapeCompareResult checkConstructed(World world) {
        ConstructShape constructShape = null;

        switch (direction) {
            case Side:
                constructShape = Constants.EnderRail_Side;
                break;
            case Up:
                constructShape = Constants.EnderRail_Up;
                break;
            case Down:
                constructShape = Constants.EnderRail_Down;
                break;
        }

        return constructShape.checkConstructed(position.toLocation(world));
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public EnderRailDirection getDirection() {
        return direction;
    }

    public void setDirection(EnderRailDirection direction) {
        this.direction = direction;
    }
}
