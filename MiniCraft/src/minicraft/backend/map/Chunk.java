package minicraft.backend.map;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.block.Block;
import minicraft.backend.utils.*;

public class Chunk {
    private ChunkCoordinate chunkCoordinate;
    private Block[][][] blocks;

    public Chunk(ChunkCoordinate chunkCoordinate, int[][][] initialBlockMap){
        this.chunkCoordinate = chunkCoordinate;
        blocks = new Block[Constant.chunkX][Constant.maxY][Constant.chunkZ];

        int biasX = chunkCoordinate.getX() * Constant.chunkX;
        int biasZ = chunkCoordinate.getZ() * Constant.chunkZ;

        for(int i = 0; i < Constant.chunkX; i++)
            for(int j = 0; j < Constant.maxY; j++)
                for(int k = 0; k < Constant.chunkZ; k++){
                    this.blocks[i][j][k] = Block.getBlockInstanceByID(initialBlockMap[i + biasX - Constant.minX][j][k + biasZ - Constant.minZ]);
                    this.blocks[i][j][k].setChunk(this);
                    this.blocks[i][j][k].setBlockCoordinate(new BlockCoordinate(i + biasX, j, k + biasZ));

                }

    }

    public ChunkCoordinate getChunkCoordinate() {
        return chunkCoordinate;
    }

    /**
     * 在这个区块中放置一个已经确定坐标的方块，将根据其方块坐标推断其在此区块的相对坐标，
     * 如果不在此区块内，则放置失败
     * @param block 方块对象，要求坐标字段不为null
     * @return 成功放置为true，失败则false
     */
    public boolean setBlockInChunk(Block block){
        if (block.getBlockCoordinate() == null) return false;
        int cx = block.getBlockCoordinate().getX() - this.chunkCoordinate.getX() * Constant.chunkX;
        int cy = block.getBlockCoordinate().getY();
        int cz = block.getBlockCoordinate().getZ() - this.chunkCoordinate.getZ() * Constant.chunkZ;

        if(cx < 0 || cx >= Constant.chunkX || cy < 0 || cy >= Constant.maxY || cz < 0 || cz >= Constant.chunkZ)
            return false;
        blocks[cx][cy][cz] = block;
        return true;
    }

    public Block[][][] getBlocks() {
        return blocks;
    }
}