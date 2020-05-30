package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class OakLeavesBlock extends BlockBackend {

	private static final int blockID = Constant.BLOCK_OAK_LEAVES;

    public OakLeavesBlock(){
        super("oak_leaves");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }
    
    @Override
    public boolean isTransparent() {
    	return true;
    }
    
    public static int getBlockidStatic() {
        return blockID;
    }


}
