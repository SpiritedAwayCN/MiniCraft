package backend.map.block;

import backend.map.DimensionMap;
import backend.utils.BlockCoordinate;

public class AirBlock extends Block{
    public static final int blockID = 0;

    AirBlock(BlockCoordinate blockCoordinate, DimensionMap map){
        super(blockCoordinate, map, "air");
    }

    @Override
    public void destoryBlock() {
        return; //空气被打掉，什么都不做
    }
}