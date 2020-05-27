package minicraft.backend.map.block;

public class DirtBlock extends BlockBackend{
    private static final int blockID = 3;

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
}