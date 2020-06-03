package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class SlimeBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_SLIME;

    public SlimeBlock(){
        super("slime_block");
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
    public void playPlaceSound() {
        chunk.getMap().miniCraftApp.audioSounds[5].playInstance();
    }
    
    @Override
    public void playDestorySound() {
        chunk.getMap().miniCraftApp.audioSounds[5].playInstance();
    }
}