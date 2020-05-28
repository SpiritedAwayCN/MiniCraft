package minicraft.backend.map;

import java.util.HashSet;

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
    private HashSet<BlockBackend> updateBlockSet;


    private int[][][] initialBlockMap;

    public DimensionMap(String name){
        this.name = name;
        chunkIndexBiasX = -Constant.minX / Constant.chunkX;
        chunkIndexBiasZ = -Constant.minZ / Constant.chunkZ;

        mapChunks = new Chunk[(Constant.maxX - Constant.minX + 1) / Constant.chunkX][(Constant.maxZ - Constant.minZ + 1) / Constant.chunkZ];
        player = new PlayerBackend();
        updateBlockSet = new HashSet<>();
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
     * @return 待前端更新的方块列表，但不会有字段表明具体是该显示还是删除，应结合上下文推断
     */
    public HashSet<BlockBackend> getUpdateBlockSet() {
        return updateBlockSet;
    }

    /**
     * 通过玩家的位置重新刷新整个世界
     * 所有应显示的方块全都加入updateBlockSet
     * 注：还没写，因为相当难写(考虑到之后有视距问题)，先用一个简单方法弄着
     */
    @Deprecated
    public void refreshWholeUpdateBlockSet(){
        ChunkCoordinate st = player.toChunkCoordinate();

    }

    /** 
     * 注：这是暂时的，最终版会弃用，有很多可用性不强的地方
     * @return 待更新方块列表
     */
    public HashSet<BlockBackend> initializeWholeUpdateBlockSetTemp(){
        for(int i = Constant.minX; i<=Constant.maxX; i++)
            for(int j = Constant.minZ; j<=Constant.maxZ; j++){
                updateBlockSet.add(getBlockByCoordinate(new BlockCoordinate(i, 4, j)));
            }
        return updateBlockSet;
    }
    
    /**
     * @return 返回毗邻的方块（的列表)，下标越界、空气除外
     */
    public HashSet<BlockBackend> updateAdjacentBlokTemp(BlockCoordinate blockCoord){
    	final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
    	for(int dir = 0; dir < 6; dir++){
    		BlockCoordinate r=new BlockCoordinate(blockCoord.getX()+dx[dir],
    				blockCoord.getY()+dy[dir],
    				blockCoord.getZ()+dz[dir]
    				);
    		if(r.getX()<Constant.minX || r.getX()>Constant.maxX ||
    				r.getY()<Constant.minY || r.getY()>=Constant.maxY ||
    				r.getZ()<Constant.minZ || r.getZ()>Constant.maxZ)
    			continue;
	    	BlockBackend b=this.getBlockByCoordinate(r);
	    	if(b.getBlockid()==0)//空气不算
				continue;
			updateBlockSet.add(b);
	    }
    	
        return updateBlockSet;
    }
    /**
     * 注：只是暂时，效率与沿用性均不佳
     * 通过方块坐标，得到在该位置放置/破坏后 出现更新的方块(放置/)
     * @return 待更新方块列表（包括当前）
     */
    public HashSet<BlockBackend> updateBlockSetTemp(BlockCoordinate blockCoordinate){
        if(getBlockByCoordinate(blockCoordinate).isTransparent()){
            // 透明就只更新当前这个
            updateBlockSet.add(getBlockByCoordinate(blockCoordinate));
        }else{
            updateBlockRecusive(blockCoordinate);
        }
        return updateBlockSet;
    }
    
    private void updateBlockRecusive(BlockCoordinate blockCoordinate){
        BlockBackend block = getBlockByCoordinate(blockCoordinate);
        if(block.getBlockid()==0 || updateBlockSet.contains(block)) return;
        updateBlockSet.add(block);
        if(!block.isTransparent()) return;

        int x = blockCoordinate.getX(), y = blockCoordinate.getY(), z = blockCoordinate.getZ();
        final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
        
        for(int dir = 0; dir < 6; dir++){
            int tx = x + dx[dir], ty = y + dy[dir], tz = z + dz[dir];
            updateBlockRecusive(new BlockCoordinate(tx, ty, tz));
        }

    }

}