package backend.map;

import backend.constants.Constant;
import backend.map.block.Block;
import backend.utils.*;

public class DimensionMap {
    private String name;
    private Chunk[][] mapChunks;
    private int chunkIndexBiasX;
    private int chunkIndexBiasZ;

    DimensionMap(){
        this.name = "word";
        chunkIndexBiasX = -Constant.minX / Constant.chunkX;
        chunkIndexBiasZ = -Constant.minZ / Constant.chunkZ;

        mapChunks = new Chunk[(Constant.maxX - Constant.minX) / Constant.chunkX][(Constant.maxZ - Constant.minZ) / Constant.chunkZ];

    }

    public Chunk getChunkByCoordinate(ChunkCoordinate chunkCoordinate){
        return mapChunks[chunkCoordinate.getX() + chunkIndexBiasX][chunkCoordinate.getZ() + chunkIndexBiasZ];
    }

    public Block getBlockByCoordinate(BlockCoordinate blockCoordinate){
        Chunk chunk = getChunkByCoordinate(blockCoordinate.toChunkCoordnate());
        int bx = blockCoordinate.getX() - chunk.getChunkCoordinate().getX() * Constant.chunkX;
        int by = blockCoordinate.getY();
        int bz = blockCoordinate.getZ() - chunk.getChunkCoordinate().getZ() * Constant.chunkZ;
        return chunk.getBlocks()[bx][by][bz];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}