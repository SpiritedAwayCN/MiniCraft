package minecraft.backend.map;

import minecraft.backend.constants.Constant;
import minecraft.backend.map.block.Block;
import minecraft.backend.map.generator.*;
import minecraft.backend.utils.*;

public class DimensionMap {
    private String name;
    private Chunk[][] mapChunks;
    private final int chunkIndexBiasX;
    private final int chunkIndexBiasZ;


    private int[][][] initialBlockMap;

    public DimensionMap(String name){
        this.name = name;
        chunkIndexBiasX = -Constant.minX / Constant.chunkX;
        chunkIndexBiasZ = -Constant.minZ / Constant.chunkZ;

        mapChunks = new Chunk[(Constant.maxX - Constant.minX) / Constant.chunkX][(Constant.maxZ - Constant.minZ) / Constant.chunkZ];

    }

    /**
     * 生成一个全新的地图
     * @param flat 是否超平坦（现在只能超平坦）
     */
    public void generateFromGenerator(boolean flat){
        Generator generator = new FlatGenerator();
        generator.generateBlockMap();
        initialBlockMap = generator.getBlockMap();

        for(int i = 0; i < mapChunks.length; i++)
            for(int j = 0; j < mapChunks[i].length; j++){
                mapChunks[i][j] = new Chunk(new ChunkCoordinate(i - chunkIndexBiasX, j - chunkIndexBiasZ), initialBlockMap);
            }
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