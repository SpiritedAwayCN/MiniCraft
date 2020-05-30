package minicraft.backend.map;

import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.utils.*;
import minicraft.frontend.GeometryBlock;

public class Chunk extends Node{
    private ChunkCoord chunkCoordinate;
    private BlockBackend[][][] blocks;
    private DimensionMap map;
    private boolean initialized = false;

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
                    blocks[i][j][k].setShouldBeShown(false);
                    // map.getUpdateBlockSet().add(blocks[i][j][k]);
                }
        this.map.miniCraftApp.getRootNode().detachChild(this);
        this.loadLevel = 0;
    }

    /**
     * 对于透明方块在区块边界导致渲染的情况 还没写
     * 其余情况应该没问题
     */
    public void loadAllBlocksInChunk(){
        if(!initialized){
            updateSurface();
            initialized = true;
        }
        this.map.miniCraftApp.getRootNode().attachChild(this);
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
                // map.getUpdateBlockSet().add(block);
                block.getChunk().attachChildWithDetach(block);
                return;
            }
        }else{
            if(masks[x][y][z]) return;
            block = blocks[x][y][z];
        }
        // System.out.println(block.getBlockCoordinate());
        if(block.getBlockid() != 0){
            block.setShouldBeShown(true);
            // map.getUpdateBlockSet().add(block);
            block.getChunk().attachChildWithDetach(block);
            if(!block.isTransparent()) return;
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
        Chunk chunkEast = map.getChunkByCoord(new ChunkCoord(chunkCoordinate.getX() + 1, chunkCoordinate.getZ()));
        Chunk chunkSouth = map.getChunkByCoord(new ChunkCoord(chunkCoordinate.getX(), chunkCoordinate.getZ() + 1));
        Chunk chunkWest = map.getChunkByCoord(new ChunkCoord(chunkCoordinate.getX() - 1, chunkCoordinate.getZ()));
        Chunk chunkNorth = map.getChunkByCoord(new ChunkCoord(chunkCoordinate.getX(), chunkCoordinate.getZ() - 1));
        for(int i = 0; i < Constant.chunkX; i++)
            for(int j = 0; j < Constant.maxY; j++)
                for(int k = 0; k < Constant.chunkZ; k++){
                    if(j == Constant.maxY - 1 || blocks[i][j][k].getBlockid() == 0){
                        updateRucusive(i, j, k, masks, true);
                    }else{
                        if(i == 0 && chunkWest != null){
                            BlockBackend block = chunkWest.getBlocks()[Constant.chunkX-1][j][k];
                            if(block.getBlockid() == 0 || (block.getShouldBeShown() && block.isTransparent()))
                                updateRucusive(i, j, k, masks, false);
                        }
                        if(i == Constant.chunkX-1 && chunkEast != null){
                            
                            BlockBackend block = chunkEast.getBlocks()[0][j][k];
                            if(block.getBlockid() == 0 || (block.getShouldBeShown() && block.isTransparent()))
                                updateRucusive(i, j, k, masks, false);
                        }
                        if(k == 0 && chunkNorth != null){
                            BlockBackend block = chunkNorth.getBlocks()[i][j][Constant.chunkZ-1];
                            if(block.getBlockid() == 0 || (block.getShouldBeShown() && block.isTransparent())){
                                updateRucusive(i, j, k, masks, false);
                            }
                        }
                        if(k == Constant.chunkZ-1 && chunkSouth != null){
                            BlockBackend block = chunkSouth.getBlocks()[i][j][0];
                            if(block.getBlockid() == 0 || (block.getShouldBeShown() && block.isTransparent()))
                                updateRucusive(i, j, k, masks, false);
                        }
                    }
                }
    }

    public void attachChildWithDetach(BlockBackend block){
        GeometryBlock geometryBlock = map.inChildBlockList.get(block.getBlockCoord());
        if(geometryBlock != null){
            this.detachChild(geometryBlock);
        }
        GeometryBlock geom = new GeometryBlock(block);
        if(block.isTransparent())
            geom.setQueueBucket(Bucket.Transparent);
        this.attachChild(geom);
        map.inChildBlockList.put(block.getBlockCoord(), geom);
    }

    public void detachIfAttached(BlockBackend block){
        GeometryBlock geometryBlock = map.inChildBlockList.get(block.getBlockCoord());
        if(geometryBlock == null) return;
        map.inChildBlockList.remove(block.getBlockCoord());
        this.detachChild(geometryBlock);
    }

}