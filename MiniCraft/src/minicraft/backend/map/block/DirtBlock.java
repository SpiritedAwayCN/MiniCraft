package minicraft.backend.map.block;

import minicraft.backend.constants.Constant;

public class DirtBlock extends BlockBackend{
    private static final int blockID = Constant.BLOCK_DIRT;

    public DirtBlock(){
        super("dirt");
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
        chunk.getMap().miniCraftApp.audioSounds[1].playInstance();
    }
}