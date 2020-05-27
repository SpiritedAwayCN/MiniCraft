package minicraft.backend.entity;

import com.jme3.math.Vector3f;

import minicraft.backend.constants.Constant;
import minicraft.backend.utils.*;

public abstract class Entity {
    protected Vector3f coordinate;
    protected String name;
    // 受重力
    
    public Entity(){}

    public Entity(Vector3f coordinate, String name){
        this.coordinate = coordinate;
    }

    public String getName() {
        return name;
    }

    public Vector3f getCoordinate() {
        return coordinate;
    }

    public ChunkCoordinate toChunkCoordinate(){
        return new ChunkCoordinate((int)(coordinate.x / Constant.chunkX), (int)(coordinate.z / Constant.chunkZ));
    }

    public BlockCoordinate toBlockCoordinate(){
        return new BlockCoordinate((int)coordinate.x, (int)coordinate.y, (int)coordinate.z);
    }

}