package minicraft.backend.map.block;

public class BedrockBlock extends Block{
    private static final int blockID = 4;

    public BedrockBlock(){
        super("bedrock");
    }

    @Override
    public int getBlockid() {
        return blockID;
    }

    public static int getBlockidStatic() {
        return blockID;
    }
}