package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class CobblestoneBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_COBBLESTONE;

    public CobblestoneBlock(){
        super("cobblestone");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}