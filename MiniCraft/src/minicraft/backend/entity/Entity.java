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

    public void setCoordinate(Vector3f coordinate) {
        this.coordinate = coordinate;
    }

    public ChunkCoordinate toChunkCoordinate(){
        return new ChunkCoordinate((int)Math.floor(coordinate.x / Constant.chunkX), (int)Math.floor(coordinate.z / Constant.chunkZ));
    }

    public BlockCoordinate toBlockCoordinate(){
        return new BlockCoordinate((int)Math.floor(coordinate.x), (int)Math.floor(coordinate.y), (int)Math.floor(coordinate.z));
    }

    

    

}