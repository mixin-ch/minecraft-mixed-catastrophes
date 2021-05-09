package ch.mixin.helperClasses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;

public class ConstructShape {
    private final HashMap<Coordinate3D, Material> materialMap;
    private final HashMap<Coordinate3D, List<Material>> materialSetMap;

    public ConstructShape(HashMap<Coordinate3D, Material> materialMap, HashMap<Coordinate3D, List<Material>> materialSetMap) {
        this.materialMap = materialMap;
        this.materialSetMap = materialSetMap;
    }

    public ConstructShape(HashMap<Coordinate3D, Material> materialMap) {
        this.materialMap = materialMap;
        this.materialSetMap = new HashMap<>();
    }

    public boolean isConstructed(Location location) {
        Coordinate3D center = Coordinate3D.toCoordinate(location);
        World world = location.getWorld();

        for (Coordinate3D cd3 : materialMap.keySet()) {
            if (!center.sum(cd3).toLocation(world).getBlock().getType().equals(materialMap.get(cd3)))
                return false;
        }

        for (Coordinate3D cd3 : materialSetMap.keySet()) {
            if (!materialSetMap.get(cd3).contains(center.sum(cd3).toLocation(world).getBlock().getType()))
                return false;
        }

        return true;
    }

    public HashMap<Coordinate3D, Material> getMaterialMap() {
        return materialMap;
    }
}
