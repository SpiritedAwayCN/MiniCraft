package backend.map.block;

import backend.map.*;
import backend.utils.*;

public abstract class Block {
    protected BlockCoordinate blockCoordinate;
    protected String name;
    protected Chunk chunk;

    protected Block(BlockCoordinate blockCoordinate, DimensionMap map, String name){
        this.blockCoordinate = blockCoordinate;
        this.chunk = map.getChunkByCoordinate(blockCoordinate.toChunkCoordnate());
        this.name = name;
    }

    public BlockCoordinate getBlockCoordinate() {
        return blockCoordinate;
    }

    public String getName() {
        return name;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void destoryBlock(){
        // TODO: 方块被打掉后要干嘛
    }
}