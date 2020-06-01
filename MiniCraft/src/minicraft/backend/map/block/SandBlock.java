package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class SandBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_SAND;

    public SandBlock(){
        super("sand");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}