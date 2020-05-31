package minicraft.backend.entity;

import com.jme3.math.Vector3f;

import minicraft.backend.map.DimensionMap;

public class PlayerBackend extends Entity{
    public PlayerBackend(DimensionMap map){
        super(new Vector3f(0, 0, 0), "Steve", map);
    }

    public PlayerBackend(Vector3f coord, String name, DimensionMap map){
        super(coord, name, map);
    }
}