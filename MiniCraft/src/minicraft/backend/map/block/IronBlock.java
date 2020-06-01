package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class IronBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_IRON_BLOCK;

    public IronBlock(){
        super("ironblock");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}