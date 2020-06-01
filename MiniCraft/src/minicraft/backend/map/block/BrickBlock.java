package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class BrickBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_BRICK;

    public BrickBlock(){
        super("brick");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}