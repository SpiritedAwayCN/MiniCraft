package minecraft.backend.map.block;

import minecraft.backend.constants.Constant;
import minecraft.backend.map.*;
import minecraft.backend.utils.*;

public abstract class Block {
    protected BlockCoordinate blockCoordinate;
    protected String name;
    protected Chunk chunk;

    protected Block(String name){
        this.name = name;
    }

    protected Block(BlockCoordinate blockCoordinate, DimensionMap map, String name){
        this.blockCoordinate = blockCoordinate;
        this.chunk = map.getChunkByCoordinate(blockCoordinate.toChunkCoordnate());
        this.name = name;
    }

    public abstract int getBlockid();

    public BlockCoordinate getBlockCoordinate() {
        return blockCoordinate;
    }

    public String getName() {
        return name;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Deprecated
    public void setBlockCoordinate(BlockCoordinate blockCoordinate) {
        this.blockCoordinate = blockCoordinate;
    }

    @Deprecated
    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }


    /**
     * 破坏这个方块，要求已经放置，否则调用失败
     * @return 是否成功破坏
     */
    public boolean destoryBlock(){
        if(this.chunk == null) return false;
        Block block = getBlockInstanceByID(0); // air
        block.blockCoordinate = this.blockCoordinate;
        block.chunk = this.chunk;

        if(this.chunk.setBlockInChunk(block) == false)
            return false;
        
        this.chunk = null;
        this.blockCoordinate = null;
        return true;
    }

    /**
     * 放置方块，当且仅当这个对象没有被放置(this.blockCoordinate == null)才能被成功执行
     * @param blockCoordinate 方块坐标
     * @param map 维度地图
     * @return 成功则true，否则为false
     */
    public boolean placeAt(BlockCoordinate blockCoordinate, DimensionMap map){
        if(this.blockCoordinate != null) return false;
        this.blockCoordinate = blockCoordinate;
        this.chunk = map.getChunkByCoordinate(blockCoordinate.toChunkCoordnate());

        return this.chunk.setBlockInChunk(this);
    }



    /**
     * 根据方块id，获得这个方块的一个实例，出现异常则返回null
     * @param blockID 方块编号，
     * @return 得到的Block对象(无参构造，除name外没有任何字段经过赋值)，失败则为null
     */
    public static Block getBlockInstanceByID(int blockID){
        Block block = null;
        try {
            block = (Block)Class.forName("minecraft.backend.map.block." + Constant.BLOCKNAME_STRINGS[blockID] + "Block").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            block = null;
        }
        return block;
    }
}