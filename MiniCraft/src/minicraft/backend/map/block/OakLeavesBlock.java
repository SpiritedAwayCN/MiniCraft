package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class OakLeavesBlock extends BlockBackend {

	private static final int blockID = Constant.BLOCK_OAK_LEAVES;
	private static boolean isLeavesTransparent=true;
    public OakLeavesBlock(){
        super("oak_leaves");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }
    public static void setTransparent(boolean arg) {
    	isLeavesTransparent=arg;
    }
    @Override
    public boolean isTransparent() {
    	return isLeavesTransparent;
    }
    
    public static int getBlockidStatic() {
        return blockID;
    }


}
