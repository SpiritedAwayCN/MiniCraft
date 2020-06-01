package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class GoldBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_GOLD_BLOCK;

    public GoldBlock(){
        super("goldblock");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}