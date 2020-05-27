package minicraft.backend.map.block;

public class GlassBlock extends BlockBackend{
    private static final int blockID = 5;

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
}