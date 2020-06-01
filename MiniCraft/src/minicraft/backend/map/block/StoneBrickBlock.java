package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class StoneBrickBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_STONE_BRICK;

    public StoneBrickBlock(){
        super("stone_brick");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}