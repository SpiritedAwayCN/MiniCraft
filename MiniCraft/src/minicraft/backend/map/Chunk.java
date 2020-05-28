package minicraft.backend.map;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.utils.*;

public class Chunk {
    private ChunkCoord chunkCoordinate;
    private BlockBackend[][][] blocks;
    private DimensionMap map;

    private final int biasX, biasZ;

    private int loadLevel = 0;  //0 不加载 1 准备加载  2已加载

    public Chunk(ChunkCoord chunkCoordinate, int[][][] initialBlockMap, DimensionMap map){
        this.chunkCoordinate = chunkCoordinate;
        blocks = new BlockBackend[Constant.chunkX][Constant.maxY][Constant.chunkZ];

        biasX = chunkCoordinate.getX() * Constant.chunkX;
        biasZ = chunkCoordinate.getZ() * Constant.chunkZ;

        for(int i = 0; i < Constant.chunkX; i++)
            for(int j = 0; j < Constant.maxY; j++)
                for(int k = 0; k < Constant.chunkZ; k++){
                    this.blocks[i][j][k] = BlockBackend.getBlockInstanceByID(initialBlockMap[i + biasX - Constant.minX][j][k + biasZ - Constant.minZ]);
                    this.blocks[i][j][k].setChunk(this);
                    this.blocks[i][j][k].setBlockCoord(new BlockCoord(i + biasX, j, k + biasZ));

                }
        
        this.loadLevel = 1;  //目前全部强加载
        this.map = map;
    }

    public ChunkCoord getChunkCoordinate() {
        return chunkCoordinate;
    }

    /**
     * 在这个区块中放置一个已经确定坐标的方块，将根据其方块坐标推断其在此区块的相对坐标，
     * 如果不在此区块内，则放置失败
     * @param block 方块对象，要求坐标字段不为null
     * @return 成功放置为true，失败则false
     */
    public boolean setBlockInChunk(BlockBackend block){
        if (block.getBlockCoord() == null) return false;
        int cx = block.getBlockCoord().getX() - this.chunkCoordinate.getX() * Constant.chunkX;
        int cy = block.getBlockCoord().getY();
        int cz = block.getBlockCoord().getZ() - this.chunkCoordinate.getZ() * Constant.chunkZ;

        if(cx < 0 || cx >= Constant.chunkX || cy < 0 || cy >= Constant.maxY || cz < 0 || cz >= Constant.chunkZ)
            return false;
        blocks[cx][cy][cz] = block;
        return true;
    }

    public BlockBackend[][][] getBlocks() {
        return blocks;
    }

    public int getLoadLevel() {
        return loadLevel;
    }

    public void setLoadLevel(int loadLevel) {
        this.loadLevel = loadLevel;
    }

    public DimensionMap getMap() {
        return map;
    }

    /**
     * 卸载这个区块
     */
    public void unloadAllBlocksInChunk(){
        for(int i = 0; i < Constant.chunkX; i++)
            for(int j = 0; j < Constant.maxY; j++)
                for(int k = 0; k < Constant.chunkZ; k++){
                    if(blocks[i][j][k].getShouldBeShown()){
                        blocks[i][j][k].setShouldBeShown(false);
                        map.getUpdateBlockSet().add(blocks[i][j][k]);
                    }
                }
        this.loadLevel = 0;
    }

    /**
     * 对于透明方块在区块边界导致渲染的情况 还没写
     * 其余情况应该没问题
     */
    public void loadAllBlocksInChunk(){
        updateSurface();

        //TODO 区块边界透明方块处理

        this.loadLevel = 2;
    }

    private boolean indexValid(int x, int z){
        return x >=0 && x <Constant.chunkX && z >=0 && z < Constant.chunkZ;
    }

    private void updateRucusive(int x, int y, int z, boolean[][][] masks, boolean root){
        BlockBackend block;
        boolean flag = indexValid(x, z);
        if(y < 0 || y >= Constant.maxY) return;
        if(!flag){
            block = map.getBlockByCoord(new BlockCoord(x+biasX, y, z+biasZ));
            // 当且仅当相邻区块中 有透明且未被渲染的透明方块才继续
            if(block == null || block.getChunk().getLoadLevel() < 2 || block.getBlockid()==0) return;
            if(!(block.isTransparent() && block.getShouldBeShown() == false)){
                block.setShouldBeShown(true);
                map.getUpdateBlockSet().add(block);
                return;
            }
        }else{
            if(masks[x][y][z]) return;
            block = blocks[x][y][z];
        }
        // System.out.println(block.getBlockCoordinate());
        if(block.getBlockid() != 0){
            block.setShouldBeShown(true);
            map.getUpdateBlockSet().add(block);
            return;
        }
        if(!root && (block.getBlockid()==0 || !block.isTransparent())) return;
        if(flag) masks[x][y][z] = true;
        final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
    
        for(int dir = 0; dir < 6; dir++){
            int tx = x + dx[dir], ty = y + dy[dir], tz = z + dz[dir];
            updateRucusive(tx, ty, tz, masks, false);
        }
    }

    private void updateSurface(){
        boolean[][][] masks = new boolean[Constant.chunkX][Constant.maxY][Constant.chunkZ];
        for(int i = 0; i < Constant.chunkX; i++)
            for(int j = 0; j < Constant.maxY; j++)
                for(int k = 0; k < Constant.chunkZ; k++){
                    if(j == Constant.maxY - 1 || blocks[i][j][k].getBlockid() == 0)
                        updateRucusive(i, j, k, masks, true);
                }
    }


}