package minicraft.backend.map.block;

public class StoneBlock extends Block{
    private static final int blockID = 1;

    public StoneBlock(){
        super("stone");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}