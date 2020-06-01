package minicraft.backend.map;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;

import com.jme3.math.Vector3f;
import com.alibaba.fastjson.JSON;

import minicraft.backend.constants.Constant;
import minicraft.backend.entity.PlayerBackend;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.map.generator.*;
import minicraft.backend.utils.*;
import minicraft.frontend.GeometryBlock;
import minicraft.frontend.MiniCraftApp;

public class DimensionMap {
    public MiniCraftApp miniCraftApp;
    private String name;
    private Chunk[][] mapChunks;
    private final int chunkIndexBiasX;
    private final int chunkIndexBiasZ;

    private PlayerBackend player;
    public ConcurrentHashMap<BlockCoord, GeometryBlock> inChildBlockList = new ConcurrentHashMap<>();
    
    
    private int[][][] initialBlockMap;
    
    public DimensionMap(String name){
        this.name = name;
        chunkIndexBiasX = -Constant.minX / Constant.chunkX;
        chunkIndexBiasZ = -Constant.minZ / Constant.chunkZ;

        mapChunks = new Chunk[(Constant.maxX - Constant.minX + 1) / Constant.chunkX][(Constant.maxZ - Constant.minZ + 1) / Constant.chunkZ];
        player = new PlayerBackend(this);
    }

    public DimensionMap(String name, MiniCraftApp app){
        this(name);
        this.miniCraftApp = app;
    }

    /**
     * 生成一个全新的地图
     * @param flat 是否超平坦（现在只能超平坦）
     */
    public void generateFromGenerator(boolean flat){
        Generator generator;
        if(flat)
            generator = new FlatGenerator();
        else
            generator = new TerrainGenerator();
        generator.generateBlockMap();
        initialBlockMap = generator.getBlockMap();
        
        for(int i = 0; i < mapChunks.length; i++)
            for(int j = 0; j < mapChunks[i].length; j++){
                mapChunks[i][j] = new Chunk(new ChunkCoord(i - chunkIndexBiasX, j - chunkIndexBiasZ), initialBlockMap, this);
            }
        int iy = Constant.maxY;
        for(; iy > 0; iy--){
            if(initialBlockMap[-Constant.minX][iy - 1][-Constant.minZ] != 0) break;
        }
        System.out.println(iy);
        player.setCoordinate(new Vector3f(0, (float)0.5 + iy, 0));
    }

    /**
     * 从存档读取世界，DimensionMap.name字段将被存档覆盖
     * @param worldName 世界名称(只要名称，不需要'./saves/'，但需要'.json')
     * @throws Exception 读取失败的异常
     */
    public void generateFromSave(String worldName) throws Exception{
        String fileName = "./saves/" + worldName;
        MapForSave mapForSave = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        mapForSave = JSON.parseObject(br.readLine(), MapForSave.class);
        br.close();

        initialBlockMap = mapForSave.getBlockMap();
        for(int i = 0; i < mapChunks.length; i++)
            for(int j = 0; j < mapChunks[i].length; j++){
                mapChunks[i][j] = new Chunk(new ChunkCoord(i - chunkIndexBiasX, j - chunkIndexBiasZ), initialBlockMap, this);
            }
        player = new PlayerBackend(mapForSave.getPlayer(), this);
        
        name = worldName.replaceAll("\\.json$", "");
    }

    public Chunk getChunkByCoord(ChunkCoord chunkCoord){
        if(!chunkCoord.isValid()) return null;
        return mapChunks[chunkCoord.getX() + chunkIndexBiasX][chunkCoord.getZ() + chunkIndexBiasZ];
    }
  

    public BlockBackend getBlockByCoord(BlockCoord blockCoord){
        Chunk chunk = getChunkByCoord(blockCoord.toChunkCoord());
        if(chunk == null) return null;
        int bx = blockCoord.getX() - chunk.getChunkCoordinate().getX() * Constant.chunkX;
        int by = blockCoord.getY();
        int bz = blockCoord.getZ() - chunk.getChunkCoordinate().getZ() * Constant.chunkZ;
        if(by < 0 || by >=Constant.maxY) return null;
        return chunk.getBlocks()[bx][by][bz];
    }
    public BlockBackend getBlockByVector3f(Vector3f r) {
    	return this.getBlockByCoord(
				new BlockCoord(
					(int)Math.round(r.x),
					(int)Math.round(r.y),
					(int)Math.round(r.z)));//四舍五入
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
     * 通过玩家的位置刷新整个世界（目前暂不会卸载已加载的）
     */
    public void refreshWholeUpdateBlockSet(){
        this.miniCraftApp.getRootNode().detachAllChildren(); 
        System.gc();
        
        ChunkCoord st = player.toChunkCoordinate();
        int cx = st.getX(), cz = st.getZ();
        
        //全部设为预加载
        for(int i = -Constant.viewChunkDistance; i <= Constant.viewChunkDistance; i++){
            for(int j = -Constant.viewChunkDistance; j <= Constant.viewChunkDistance; j++){
                setChunkPreLoad(st.setXZ(cx + i, cz + j));
            }
        }

        //再开始变强加载
        for(int i = -Constant.viewChunkDistance; i <= Constant.viewChunkDistance; i++){
            for(int j = -Constant.viewChunkDistance; j <= Constant.viewChunkDistance; j++){
                loadChunkByCoord(st.setXZ(cx + i, cz + j));
            }
        }
    }

    private void setChunkPreLoad(ChunkCoord chunkCoordinate){
        Chunk chunk= this.getChunkByCoord(chunkCoordinate);
        if(chunk != null)
            chunk.setLoadLevel(1);
    }

    private void setChunkPreLoadIfNot(ChunkCoord chunkCoordinate){
        Chunk chunk= this.getChunkByCoord(chunkCoordinate);
        if(chunk != null && chunk.getLoadLevel() == 0)
            chunk.setLoadLevel(1);
    }

    private void loadChunkByCoord(ChunkCoord chunkCoordinate){
        Chunk chunk = getChunkByCoord(chunkCoordinate);
        if(chunk != null && chunk.getLoadLevel() == 1){
            // System.out.printf("load : %d %d\n", chunkCoordinate.getX(), chunkCoordinate.getZ());
            chunk.loadAllBlocksInChunk();
        }
    }

    /** 
     * 注：这是暂时的，最终版会弃用，有很多可用性不强的地方
     * 更新：已弃用，请改用refreshWholeUpdateBlockSet()
     * @return 待更新方块列表
     */
    @Deprecated
    public HashSet<BlockBackend> initializeWholeUpdateBlockSetTemp(){
        HashSet<BlockBackend> localUpdateSet = new HashSet<>();
        for(int i = Constant.minX; i<=Constant.maxX; i++)
            for(int j = Constant.minZ; j<=Constant.maxZ; j++){
                localUpdateSet.add(getBlockByCoord(new BlockCoord(i, 4, j)));
            }
        return localUpdateSet;
    }
    
    /**
     * 请用updateBlockSetTemp()
     * @return 返回毗邻的方块（的列表)，下标越界、空气除外
     */
    @Deprecated
    public HashSet<BlockBackend> updateAdjacentBlockTemp(BlockCoord blockCoord){
    	final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
    	for(int dir = 0; dir < 6; dir++){
    		BlockCoord r=new BlockCoord(blockCoord.getX()+dx[dir],
    				blockCoord.getY()+dy[dir],
    				blockCoord.getZ()+dz[dir]
    				);
    		if(r.getX()<Constant.minX || r.getX()>Constant.maxX ||
    				r.getY()<Constant.minY || r.getY()>=Constant.maxY ||
    				r.getZ()<Constant.minZ || r.getZ()>Constant.maxZ)
    			continue;
	    	BlockBackend b=this.getBlockByCoord(r);
	    	if(b.getBlockid()==0)//空气不算
				continue;
			// updateBlockSet.add(b);
	    }
    	
        return null;
    }
    /**
     * 请用updateBlockSetTemp()
     * @return 返回一个方块是否和空气毗邻
     */
    @Deprecated
    synchronized public Boolean isBlockAdjacentToAir(BlockCoord blockCoord) {
    	
    	final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
    	BlockCoord r=blockCoord;
    	for(int dir = 0; dir < 6; dir++){
    		BlockCoord r2=new BlockCoord(r.getX()+dx[dir],
    				r.getY()+dy[dir],
    				r.getZ()+dz[dir]
    				);
    		if(r2.getX()<Constant.minX || r2.getX()>Constant.maxX ||
    				r2.getY()<Constant.minY || r2.getY()>=Constant.maxY ||
    				r2.getZ()<Constant.minZ || r2.getZ()>Constant.maxZ)
    			continue;
	    	BlockBackend b=this.getBlockByCoord(r2);
	    	//以上为迭代器
    	
    		if(b.getBlockid()==0) {
    			return true;
    		}
    	}
    	//else
    	return false;
    }

    /**
     * 注：这是最新版的函数，建议使用这个，如果有BUG立刻联系
     * 通过方块坐标，得到在该位置放置/破坏后 出现更新的方块
     * @param blockCoordinate 坐标
     * @param isBreak 该位置发生了破坏(true)/放置(false)
     */
    public void updateBlockSetTemp(BlockCoord blockCoordinate, boolean isBreak){
        //由前端修改
    	//updateBlockSet.clear();
        BlockBackend block = getBlockByCoord(blockCoordinate);
        if(block.isTransparent()){
            // 透明就只更新当前这个
            if(block.getBlockid() != 0){ //只有place才执行到这
                block.setShouldBeShown(true);
                block.getChunk().attachChildWithDetach(block);
                // updateBlockSet.add(block);
            }
        }else if(isBreak){
            block.setShouldBeShown(false);
            // updateBlockSet.add(block);
            block.getChunk().detachIfAttached(block);
            HashSet<BlockBackend> vis = new HashSet<>();
            updateBlockRecusiveBreak(blockCoordinate, true, vis);
        }else{
            int x = blockCoordinate.getX(), y = blockCoordinate.getY(), z = blockCoordinate.getZ();
            final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
            block.setShouldBeShown(true);
            // updateBlockSet.add(block);
            block.getChunk().attachChildWithDetach(block);
            HashSet<BlockBackend> vis = new HashSet<>();
            for(int dir = 0; dir < 6; dir++){
                int tx = x + dx[dir], ty = y + dy[dir], tz = z + dz[dir];
                updateBlockRecusivePlace(new BlockCoord(tx, ty, tz), true, vis);
            }
        }
    }


    private void updateBlockRecusiveBreak(BlockCoord blockCoordinate, boolean root, HashSet<BlockBackend> vis){
    	
    	BlockBackend block;
    	block = getBlockByCoord(blockCoordinate);
        if(block == null) return;
        if(!root){
            if(block.getBlockid()==0 || vis.contains(block) || block.getShouldBeShown()) return;
            block.setShouldBeShown(true);
            vis.add(block);
            block.getChunk().attachChildWithDetach(block);
            if(!block.isTransparent()) return;
        }

        int x = blockCoordinate.getX(), y = blockCoordinate.getY(), z = blockCoordinate.getZ();
        final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
        
        for(int dir = 0; dir < 6; dir++){
            int tx = x + dx[dir], ty = y + dy[dir], tz = z + dz[dir];
            updateBlockRecusiveBreak(new BlockCoord(tx, ty, tz), false, vis);
        }
    }

    private boolean updateBlockRecusivePlace(BlockCoord blockCoordinate, boolean root, HashSet<BlockBackend> vis){
        BlockBackend block;
    	block = getBlockByCoord(blockCoordinate);
        if(block == null || block.getChunk().getLoadLevel() < 2) return false;
        if(block.getBlockid() == 0) return true;
        if(!root && (!block.getShouldBeShown() || vis.contains(block))) return false;
        if(!root && !block.isTransparent()){
            return false;
        } 
        vis.add(block);
        int x = blockCoordinate.getX(), y = blockCoordinate.getY(), z = blockCoordinate.getZ();
        final int dx[] = {1, 0, -1, 0, 0, 0}, dz[] = {0, 1, 0, -1, 0, 0}, dy[]={0, 0, 0, 0, 1, -1};
        
        for(int dir = 0; dir < 6; dir++){
            int tx = x + dx[dir], ty = y + dy[dir], tz = z + dz[dir];
            if(updateBlockRecusivePlace(new BlockCoord(tx, ty, tz), false, vis)){
                return true;
            }
        }
        if(block.getShouldBeShown()){
            block.setShouldBeShown(false);
            // updateBlockSet.add(block);
            block.getChunk().detachIfAttached(block);
        }
        return false;
    }

    /**
     * 玩家移动
     * @param coord 新的坐标
     * @return 是否导致了区块的加载/卸载，若为true，通常意味着updateSet有更新
     */
    public boolean movePlayerTo(Vector3f coord){
        player.updateCoordinate(coord);
        player.setCoordinate(coord);
        ChunkCoord newcoord = player.toChunkCoordinate(), oldcoord = player.getChunkCoordinate();
        int ox = oldcoord.getX(), oz = oldcoord.getZ();
        int nx = newcoord.getX(), nz = newcoord.getZ();
        if(ox == nx && oz == nz) return false;
        // System.out.printf("old=(%d,%d) new=(%d,%d)\n", ox,oz,nx,nz);
        //StarSky修改
        //updateBlockSet.clear();
        ChunkCoord st = new ChunkCoord();
        for(int i = -Constant.viewChunkDistance; i <= Constant.viewChunkDistance; i++){
            for(int j = -Constant.viewChunkDistance; j <= Constant.viewChunkDistance; j++){
                unloadIfTooFar(st.setXZ(ox + i, oz + j), nx, nz);
            }
        }
        
        for(int i = -Constant.viewChunkDistance; i <= Constant.viewChunkDistance; i++){
            for(int j = -Constant.viewChunkDistance; j <= Constant.viewChunkDistance; j++){
                setChunkPreLoadIfNot(st.setXZ(nx + i, nz + j));
            }
        }

        for(int i = -Constant.viewChunkDistance; i <= Constant.viewChunkDistance; i++){
            for(int j = -Constant.viewChunkDistance; j <= Constant.viewChunkDistance; j++){
                loadChunkByCoord(st.setXZ(nx + i, nz + j));
            }
        }
        player.setChunkCoordinate(newcoord);
        // System.out.println(updateBlockSet.size());
        return true;
    }

    private void unloadIfTooFar(ChunkCoord oldCoordinate, int nx, int nz){
        if(Math.max(Math.abs(oldCoordinate.getX() - nx), Math.abs(oldCoordinate.getZ() - nz)) <= Constant.viewChunkDistance)
            return;
        Chunk chunk = getChunkByCoord(oldCoordinate);
        if(chunk != null && chunk.getLoadLevel() > 0){
            // System.out.printf("unload : %d %d\n", oldCoordinate.getX(), oldCoordinate.getZ());
            chunk.unloadAllBlocksInChunk();
        }
    }

    public void saveMap(){
        new Thread(){{
            long st = System.currentTimeMillis();
            saveProcess();
            long ed = System.currentTimeMillis();
            System.out.println("Saved in " + (ed-st) + "ms.");
        }}.run();
    }

    private synchronized void saveProcess(){
        MapForSave mapForSave = new MapForSave(this);
        File dir = new File(Constant.saveDir);
        if(!dir.exists()) dir.mkdirs();
        String fileName = Constant.saveDir + "/" + this.name + ".json";
        try (OutputStream out = new FileOutputStream(fileName)) {
            String output = JSON.toJSONString(mapForSave);
            out.write(output.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}