package ch.mixin.helperClasses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;

public class ConstructShape {
    private final HashMap<Coordinate3D, Material> materialMap;

    public ConstructShape(HashMap<Coordinate3D, Material> materialMap) {
        this.materialMap = materialMap;
    }

    public boolean isConstructed(Location location) {
        Coordinate3D center = Coordinate3D.toCoordinate(location);
        World world = location.getWorld();

        for (Coordinate3D cd3 : materialMap.keySet()) {
            if (!center.sum(cd3).toLocation(world).getBlock().getType().equals(materialMap.get(cd3)))
                return false;
        }

        return true;
    }

    public HashMap<Coordinate3D, Material> getMaterialMap() {
        return materialMap;
    }
}
