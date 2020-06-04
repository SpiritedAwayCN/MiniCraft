package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class GrassBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_GRASS;

    public GrassBlock(){
        super("grass");
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
        chunk.getMap().miniCraftApp.audioSounds[1].playInstance();
    }
    @Override
    public void playPlaceSound() {
        chunk.getMap().miniCraftApp.audioSounds[1].playInstance();
    }
}