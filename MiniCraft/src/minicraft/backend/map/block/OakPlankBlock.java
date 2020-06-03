package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class OakPlankBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_OAK_PLANK;

    public OakPlankBlock(){
        super("oak_plank");
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
        chunk.getMap().miniCraftApp.audioSounds[4].playInstance();
    }
}