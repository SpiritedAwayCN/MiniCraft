package minicraft.backend.constants;

public class Constant {
    public static final int minX = -128;
    public static final int minZ = -128;
    public static final int maxX = 127;
    public static final int maxZ = 127;
    public static final int minY = 0;
    public static final int maxY = 64;

    public static final int chunkX = 16;
    public static final int chunkZ = 16;

    public static final int viewChunkDistance = 4;

    // 一行5个，方便数数
    public static final String[] BLOCKNAME_STRINGS = {
        "Air", "Stone", "Grass", "Dirt", "Bedrock",
        "Glass"};
    //共六种方块
    public static final int BLOCK_TYPE_NUM=6;
    
    public static final int chunkMinCoordX = (int)Math.floor(minX / chunkX);
    public static final int chunkMaxCoordX = (int)Math.floor(maxX / chunkX);
    public static final int chunkMinCoordZ = (int)Math.floor(minZ / chunkZ);
    public static final int chunkMaxCoordZ = (int)Math.floor(maxZ / chunkZ);
}