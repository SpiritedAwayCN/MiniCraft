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
        if(!chunkCoordinate.isValid()) return null;
        return mapChunks[chunkCoordinate.getX() + chunkIndexBiasX][chunkCoordinate.getZ() + chunkIndexBiasZ];
    }

    public BlockBackend getBlockByCoordinate(BlockCoordinate blockCoordinate){
        Chunk chunk = getChunkByCoordinate(blockCoordinate.toChunkCoordnate());
        if(chunk == null) return null;
        int bx = blockCoordinate.getX() - chunk.getChunkCoordinate().getX() * Constant.chunkX;
        int by = blockCoordinate.getY();
        int bz = blockCoordinate.getZ() - chunk.getChunkCoordinate().getZ() * Constant.chunkZ;
        if(by < 0 || by >=Constant.maxY) return null;
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
     * 需要更新的方块列表，注意前端用完后必须调用clear，后端没有任何删除此集合元素的操作
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
    public HashSet<BlockBackend> refreshWholeUpdateBlockSet(){
        ChunkCoordinate st = player.toChunkCoordinate();
        int cx = st.getX(), cz = st.getZ();

        //全部设为预加载
        for(int dist = 0; dist < Constant.viewChunkDistance; dist++){
            for(int i = 0; i <= dist; i++){
                int j = dist - i;
                setChunkPreLoad(st.setXZ(cx + i, cz + j));
                setChunkPreLoad(st.setXZ(cx + i, cz - j));
                setChunkPreLoad(st.setXZ(cx - i, cz + j));
                setChunkPreLoad(st.setXZ(cx - i, cz - j));
            }
        }

        //再开始变强加载
        for(int dist = 0; dist < Constant.viewChunkDistance; dist++){
            for(int i = 0; i <= dist; i++){
                int j = dist - i;
                loadChunkByCoord(st.setXZ(cx + i, cz + j));
                loadChunkByCoord(st.setXZ(cx + i, cz - j));
                loadChunkByCoord(st.setXZ(cx - i, cz + j));
                loadChunkByCoord(st.setXZ(cx - i, cz - j));
            }
        }

        return this.updateBlockSet;
    }

    private void setChunkPreLoad(ChunkCoordinate chunkCoordinate){
        Chunk chunk= this.getChunkByCoordinate(chunkCoordinate);
        if(chunk != null)
            chunk.setLoadLevel(1);
    }

    private void loadChunkByCoord(ChunkCoordinate chunkCoordinate){
        Chunk chunk = getChunkByCoordinate(chunkCoordinate);
        if(chunk != null && chunk.getLoadLevel() == 1){
            System.out.printf("%d %d\n", chunkCoordinate.getX(), chunkCoordinate.getZ());
            chunk.loadAllBlocksInChunk();
        }
    }

    /** 
     * 注：这是暂时的，最终版会弃用，有很多可用性不强的地方
     * @return 待更新方块列表
     */
    public HashSet<BlockBackend> initializeWholeUpdateBlockSetTemp(){
        HashSet<BlockBackend> localUpdateSet = new HashSet<>();
        for(int i = Constant.minX; i<=Constant.maxX; i++)
            for(int j = Constant.minZ; j<=Constant.maxZ; j++){
                localUpdateSet.add(getBlockByCoordinate(new BlockCoordinate(i, 4, j)));
            }
        return localUpdateSet;
    }
    
    /**
     * @return 返回毗邻的方块（的列表)，下标越界、空气除外
     */
    @Deprecated
    public HashSet<BlockBackend> updateAdjacentBlockTemp(BlockCoordinate blockCoord){
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
     * @return 返回一个方块是否和空气毗邻
     */
    @Deprecated
    synchronized public Boolean isBlockAdjacentToAir(BlockCoordinate blockCoord) {
    	
    	final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
    	BlockCoordinate r=blockCoord;
    	for(int dir = 0; dir < 6; dir++){
    		BlockCoordinate r2=new BlockCoordinate(r.getX()+dx[dir],
    				r.getY()+dy[dir],
    				r.getZ()+dz[dir]
    				);
    		if(r2.getX()<Constant.minX || r2.getX()>Constant.maxX ||
    				r2.getY()<Constant.minY || r2.getY()>=Constant.maxY ||
    				r2.getZ()<Constant.minZ || r2.getZ()>Constant.maxZ)
    			continue;
	    	BlockBackend b=this.getBlockByCoordinate(r2);
	    	//以上为迭代器
    	
    		if(b.getBlockid()==0) {
    			return true;
    		}
    	}
    	//else
    	return false;
    }
    /**
     * 注：只是暂时，效率与沿用性均不佳
     * 通过方块坐标，得到在该位置放置/破坏后 出现更新的方块
     * @param blockCoordinate 坐标
     * @param isBreak 该位置发生了破坏(true)/放置(false)
     * @return 待更新方块列表（包括当前）
     */
    public HashSet<BlockBackend> updateBlockSetTemp(BlockCoordinate blockCoordinate, boolean isBreak){
        updateBlockSet.clear();
        BlockBackend block = getBlockByCoordinate(blockCoordinate);
        if(block.isTransparent()){
            // 透明就只更新当前这个
            if(block.getBlockid() != 0){
                block.setShouldBeShown(isBreak);
                updateBlockSet.add(block);
            }
        }else if(isBreak){
            block.setShouldBeShown(false);
            updateBlockSet.add(block);
            updateBlockRecusiveBreak(blockCoordinate, true);
        }else{
            int x = blockCoordinate.getX(), y = blockCoordinate.getY(), z = blockCoordinate.getZ();
            final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
            block.setShouldBeShown(true);
            updateBlockSet.add(block);
            for(int dir = 0; dir < 6; dir++){
                int tx = x + dx[dir], ty = y + dy[dir], tz = z + dz[dir];
                updateBlockRecusivePlace(new BlockCoordinate(tx, ty, tz), true);
            }
        }
        return updateBlockSet;
    }


    private void updateBlockRecusiveBreak(BlockCoordinate blockCoordinate, boolean root){
    	
    	BlockBackend block;
    	block = getBlockByCoordinate(blockCoordinate);
        if(block == null || block.getChunk().getLoadLevel() < 2) return;
        if(!root){
            if(block.getBlockid()==0 || updateBlockSet.contains(block) || block.getShouldBeShown()) return;
            block.setShouldBeShown(true);
            updateBlockSet.add(block);
            if(!block.isTransparent()) return;
        }

        int x = blockCoordinate.getX(), y = blockCoordinate.getY(), z = blockCoordinate.getZ();
        final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
        
        for(int dir = 0; dir < 6; dir++){
            int tx = x + dx[dir], ty = y + dy[dir], tz = z + dz[dir];
            updateBlockRecusiveBreak(new BlockCoordinate(tx, ty, tz), false);
        }

    }

    private boolean updateBlockRecusivePlace(BlockCoordinate blockCoordinate, boolean root){
        BlockBackend block;
    	block = getBlockByCoordinate(blockCoordinate);
        if(block == null || block.getChunk().getLoadLevel() < 2) return false;
        if(block.getBlockid() == 0) return true;
        if(!root && (!block.getShouldBeShown() || updateBlockSet.contains(block))) return false;
        if(!root && !block.isTransparent()) return false;

        int x = blockCoordinate.getX(), y = blockCoordinate.getY(), z = blockCoordinate.getZ();
        final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
        
        for(int dir = 0; dir < 6; dir++){
            int tx = x + dx[dir], ty = y + dy[dir], tz = z + dz[dir];
            if(updateBlockRecusivePlace(new BlockCoordinate(tx, ty, tz), false)){
                return true;
            }
        }
        if(block.getShouldBeShown()){
            block.setShouldBeShown(false);
            updateBlockSet.add(block);
        }
        return false;
    }

}