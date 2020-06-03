package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.*;
import minicraft.backend.utils.*;

public abstract class BlockBackend {
    protected BlockCoord blockCoord;
    protected String name;
    protected Chunk chunk;
    protected boolean shouldBeShown;
    
    protected BlockBackend(String name){
        this.name = name;
        this.shouldBeShown = false;
    }

    protected BlockBackend(BlockCoord blockCoordinate, DimensionMap map, String name){
        this.blockCoord = blockCoordinate;
        this.chunk = map.getChunkByCoord(blockCoordinate.toChunkCoord());
        this.name = name;
        this.shouldBeShown = false;
    }

    public abstract int getBlockid();

    public BlockCoord getBlockCoord() {
        return blockCoord;
    }

    public String getName() {
        return name;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public boolean getShouldBeShown(){
        return shouldBeShown;
    }

    public boolean isFullHitbox(){
        return true;
    }

    public void setShouldBeShown(boolean shouldBeShown) {
        this.shouldBeShown = shouldBeShown;
    }

    public void setBlockCoord(BlockCoord blockCoord) {
        this.blockCoord = blockCoord;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    /**
     * 是否是透明方块(决定是否阻挡渲染)
     * @return 是则true；否则false
     */
    public boolean isTransparent(){
        return false; //透明方块在子类中重载
    }

    /*
     * 返回是否是各面相同的材质，默认为true
     */
    public boolean isTextureUniform() {
    	return true;
    }

    /**
     * 破坏这个方块，要求已经放置，否则调用失败
     * @return 是否成功破坏
     */
    public boolean destoryBlock(){
        if(this.chunk == null) return false;
        BlockBackend block = getBlockInstanceByID(0); // air
        block.blockCoord = this.blockCoord;
        block.chunk = this.chunk;
        if(this.chunk.setBlockInChunk(block) == false)
            return false;
        playDestorySound();
        chunk.getMap().getPlayer().checkFoot();
        // this.chunk = null;
        // this.blockCoord = null;
        return true;
    }

    /**
     * 放置方块，当且仅当这个对象没有被放置(this.blockCoordinate == null)才能被成功执行
     * @param blockCoordinate 方块坐标
     * @param map 维度地图
     * @return 成功则true，否则为false
     */
    public boolean placeAt(BlockCoord blockCoordinate, DimensionMap map){
        if(this.blockCoord != null) return false;
        this.blockCoord = blockCoordinate;
        this.chunk = map.getChunkByCoord(blockCoordinate.toChunkCoord());
        boolean ret = this.chunk.setBlockInChunk(this);
        if(ret) playPlaceSound();
        return ret;
    }



    /**
     * 根据方块id，获得这个方块的一个实例，出现异常则返回null
     * @param blockID 方块编号，
     * @return 得到的Block对象(无参构造，除name外没有任何字段经过赋值)，失败则为null
     */
    public static BlockBackend getBlockInstanceByID(int blockID){
        BlockBackend block = null;
        try {
            block = (BlockBackend)Class.forName("minicraft.backend.map.block." + Constant.BLOCKNAME_STRINGS[blockID] + "Block").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            block = null;
        }
        return block;
    }
    @Override
	public int hashCode() {
		BlockCoord r=this.blockCoord;
		return (r.getX()-Constant.minX)*Constant.maxY*(Constant.maxZ-Constant.minZ)
				+(r.getY()-Constant.minY)*(Constant.maxZ-Constant.minZ)
				+(r.getZ()-Constant.minZ);
    }
    
    public void playPlaceSound(){
        chunk.getMap().miniCraftApp.audioSounds[3].playInstance();
    }

    public void playDestorySound(){
        chunk.getMap().miniCraftApp.audioSounds[3].playInstance();
    }
}