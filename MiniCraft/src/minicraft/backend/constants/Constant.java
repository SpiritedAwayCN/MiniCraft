package minicraft.backend.constants;

public class Constant {
    public static final int minX = -128;
    public static final int minZ = -128;
    public static final int maxX = 127;
    public static final int maxZ = 127;
    public static final int maxY = 64;

    public static final int chunkX = 16;
    public static final int chunkZ = 16;

    // 一行5个，方便数数
    public static final String[] BLOCKNAME_STRINGS = {
        "Air", "Stone", "Grass", "Dirt", "Bedrock",
        "Glass"};
}