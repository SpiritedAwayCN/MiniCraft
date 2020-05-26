package minecraft.backend.map.block;

import minecraft.backend.map.DimensionMap;
import minecraft.backend.utils.BlockCoordinate;

public class AirBlock extends Block{
    private static final int blockID = 0;

    public AirBlock(){
        super("air");
    }

    AirBlock(BlockCoordinate blockCoordinate, DimensionMap map){
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
}