package minicraft.backend.entity;

import com.jme3.math.Vector3f;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.Chunk;
import minicraft.backend.utils.*;

public abstract class Entity {
    protected Vector3f coordinate;
    protected String name;
    protected ChunkCoord chunkCoordinate;
    // 受重力
    
    Entity(){}

    public Entity(Vector3f coordinate, String name){
        this.coordinate = coordinate;
        this.chunkCoordinate = toChunkCoordinate();
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

    public ChunkCoord toChunkCoordinate(){
        return new ChunkCoord((int)Math.floor(coordinate.x / Constant.chunkX), (int)Math.floor(coordinate.z / Constant.chunkZ));
    }

    public BlockCoord toBlockCoordinate(){
        return new BlockCoord((int)Math.floor(coordinate.x), (int)Math.floor(coordinate.y), (int)Math.floor(coordinate.z));
    }

    public ChunkCoord getChunkCoordinate() {
        return chunkCoordinate;
    }

    public void setChunkCoordinate(ChunkCoord chunkCoordinate) {
        this.chunkCoordinate = chunkCoordinate;
    }

}