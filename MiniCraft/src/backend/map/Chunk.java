package backend.map;

import backend.constants.Constant;
import backend.map.block.Block;
import backend.utils.*;

public class Chunk {
    private ChunkCoordinate chunkCoordinate;
    private Block[][][] blocks;

    public Chunk(ChunkCoordinate chunkCoordinate){
        this.chunkCoordinate = chunkCoordinate;
        blocks = new Block[Constant.chunkX][Constant.maxY][Constant.chunkZ];
    }

    public ChunkCoordinate getChunkCoordinate() {
        return chunkCoordinate;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }
}