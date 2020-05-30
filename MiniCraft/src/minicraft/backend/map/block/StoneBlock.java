package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class StoneBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_STONE;

    public StoneBlock(){
        super("stone");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}