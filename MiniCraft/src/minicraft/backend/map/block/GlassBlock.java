package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class GlassBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_GLASS;

    public GlassBlock(){
        super("glass");
    }

    @Override
    public boolean isTransparent() {
        return true;
    }
    
    

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
    
    @Override
    public void playDestorySound() {
        chunk.getMap().miniCraftApp.audioSounds[0].playInstance();
    }
}