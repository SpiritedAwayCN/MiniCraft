package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class BedrockBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_BEDROCK;

    public BedrockBlock(){
        super("bedrock");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}