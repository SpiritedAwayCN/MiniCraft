package minicraft.backend.map.block;

public class DirtBlock extends Block{
    private static final int blockID = 1;

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