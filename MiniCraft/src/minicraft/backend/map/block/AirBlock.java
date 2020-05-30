package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.DimensionMap;
import minicraft.backend.utils.BlockCoord;

public class AirBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_AIR;

    public AirBlock(){
        super("air");
    }

    AirBlock(BlockCoord blockCoordinate, DimensionMap map){
        super(blockCoordinate, map, "air");
    }

    @Override
    public boolean destoryBlock() {
        return true; //空气被打掉，什么都不做
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }

    @Override
    public boolean getShouldBeShown() {
        return false;
    }
}