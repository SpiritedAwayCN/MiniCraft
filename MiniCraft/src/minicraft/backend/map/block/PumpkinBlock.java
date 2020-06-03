package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class PumpkinBlock extends BlockBackend {

	private static final int blockID = Constant.BLOCK_PUMPKIN;

    public PumpkinBlock(){
        super("pumpkin");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }
    
    @Override
    public boolean isTextureUniform() {
    	return false;
    }
    
    public static int getBlockidStatic() {
        return blockID;
    }
    
    @Override
    public void playDestorySound() {
        chunk.getMap().miniCraftApp.audioSounds[4].playInstance();
    }

}
