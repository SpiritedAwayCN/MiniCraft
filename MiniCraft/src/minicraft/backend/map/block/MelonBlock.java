package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class MelonBlock extends BlockBackend {

	private static final int blockID = Constant.BLOCK_MELON;

    public MelonBlock(){
        super("melon");
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
