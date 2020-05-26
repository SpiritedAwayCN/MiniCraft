package minicraft.backend.map.block;

public class GrassBlock extends Block{
    private static final int blockID = 2;

    public GrassBlock(){
        super("grass");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}