package minicraft.backend.map;

import java.util.LinkedList;

import minicraft.backend.constants.Constant;
import minicraft.backend.entity.PlayerBackend;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.map.generator.*;
import minicraft.backend.utils.*;

public class DimensionMap {
    private String name;
    private Chunk[][] mapChunks;
    private final int chunkIndexBiasX;
    private final int chunkIndexBiasZ;

    private PlayerBackend player;
    private LinkedList<BlockBackend> updateBlockList;


    private int[][][] initialBlockMap;

    public DimensionMap(String name){
        this.name = name;
        chunkIndexBiasX = -Constant.minX / Constant.chunkX;
        chunkIndexBiasZ = -Constant.minZ / Constant.chunkZ;

        mapChunks = new Chunk[(Constant.maxX - Constant.minX + 1) / Constant.chunkX][(Constant.maxZ - Constant.minZ + 1) / Constant.chunkZ];
        player = new PlayerBackend();
        updateBlockList = new LinkedList<>();
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
                mapChunks[i][j] = new Chunk(new ChunkCoordinate(i - chunkIndexBiasX, j - chunkIndexBiasZ), initialBlockMap, this);
            }
    }

    public Chunk getChunkByCoordinate(ChunkCoordinate chunkCoordinate){
        return mapChunks[chunkCoordinate.getX() + chunkIndexBiasX][chunkCoordinate.getZ() + chunkIndexBiasZ];
    }

    public BlockBackend getBlockByCoordinate(BlockCoordinate blockCoordinate){
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

    public Chunk[][] getMapChunks() {
        return mapChunks;
    }

    public PlayerBackend getPlayer() {
        return player;
    }

    /**
     * 需要更新的方块列表，注意前端用完后调用clear，后端没有任何删除此列表元素的操作
     * @return 待前端更新的方块列表
     */
    public LinkedList<BlockBackend> getUpdateBlockList() {
        return updateBlockList;
    }

    /**
     * 通过玩家的位置重新刷新整个世界
     * 所有应显示的方块全都加入updateBlockList
     * 注：还没写，因为相当难写(考虑到之后有视距问题)，先用一个简单方法弄着
     */
    @Deprecated
    public void refreshWholeUpdateBlockList(){
        ChunkCoordinate st = player.toChunkCoordinate();

    }

    /** 
     * 注：这是暂时的，最终版会弃用
     */
    public void initializeWholeUpdateBlockListTemp(){
        
    }
}