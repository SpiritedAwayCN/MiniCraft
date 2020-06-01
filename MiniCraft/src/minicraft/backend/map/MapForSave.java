package minicraft.backend.map;

import minicraft.backend.constants.Constant;
import minicraft.backend.entity.EntityForSave;

public class MapForSave {
    private int[][][] blockMap;
    private EntityForSave player;

    public MapForSave(){}

    public MapForSave(DimensionMap map){
        blockMap = new int[Constant.maxX - Constant.minX + 1][Constant.maxY][Constant.maxZ - Constant.minZ + 1];
        for(int i = 0; i < map.getMapChunks().length; i++)
            for(int j = 0; j < map.getMapChunks()[0].length; j++){
                Chunk chunk = map.getMapChunks()[i][j];
                for(int ii = 0; ii<Constant.chunkX; ii++)
                    for(int jj = 0; jj<Constant.maxY; jj++)
                        for(int kk = 0; kk<Constant.chunkZ; kk++){
                            blockMap[i * Constant.chunkX + ii][jj][j * Constant.chunkZ + kk] = chunk.getBlocks()[ii][jj][kk].getBlockid();
                        }
            }
        player = new EntityForSave(map.getPlayer());
    }

    public void setBlockMap(int[][][] blockMap) {
        this.blockMap = blockMap;
    }
    public void setPlayer(EntityForSave player) {
        this.player = player;
    }
    public int[][][] getBlockMap() {
        return blockMap;
    }
    public EntityForSave getPlayer() {
        return player;
    }
}