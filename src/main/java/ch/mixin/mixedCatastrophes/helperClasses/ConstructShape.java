package ch.mixin.mixedCatastrophes.helperClasses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;

public class ConstructShape {
    private final RotationSymmetry rotationSymmetry;
    private final HashMap<Coordinate3D, Material> materialMap;
    private final HashMap<Coordinate3D, List<Material>> materialSetMap;

    public ConstructShape(RotationSymmetry rotationSymmetry, HashMap<Coordinate3D, Material> materialMap, HashMap<Coordinate3D, List<Material>> materialSetMap) {
        this.rotationSymmetry = rotationSymmetry;
        this.materialMap = materialMap;
        this.materialSetMap = materialSetMap;
    }

    public ConstructShape(RotationSymmetry rotationSymmetry, HashMap<Coordinate3D, Material> materialMap) {
        this.rotationSymmetry = rotationSymmetry;
        this.materialMap = materialMap;
        this.materialSetMap = new HashMap<>();
    }

    public ShapeCompareResult checkConstructed(Location location) {
        int rotationPossibilities = 0;

        switch (rotationSymmetry) {
            case Degrees90:
                rotationPossibilities = 1;
                break;
            case Degrees180:
                rotationPossibilities = 2;
                break;
            case Degrees360:
                rotationPossibilities = 4;
                break;
        }

        for (int rotation = 0; rotation < rotationPossibilities; rotation++) {
            if (checkConstructed(location, rotation))
                return new ShapeCompareResult(true, rotation);
        }

        return new ShapeCompareResult(false, 0);
    }

    private boolean checkConstructed(Location location, int rotation) {
        Coordinate3D center = Coordinate3D.toCoordinate(location);
        World world = location.getWorld();

        for (Coordinate3D cd3 : materialMap.keySet()) {
            Material material = materialMap.get(cd3);

            if (!center.sum(cd3.rotateY90Clockwise(rotation)).toLocation(world).getBlock().getType().equals(material))
                return false;
        }

        for (Coordinate3D cd3 : materialSetMap.keySet()) {
            List<Material> materialList = materialSetMap.get(cd3);

            if (!materialList.contains(center.sum(cd3.rotateY90Clockwise(rotation)).toLocation(world).getBlock().getType()))
                return false;
        }

        return true;
    }
}
