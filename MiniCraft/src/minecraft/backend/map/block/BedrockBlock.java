package minecraft.backend.map.block;

public class BedrockBlock extends Block{
    private static final int blockID = 1;

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