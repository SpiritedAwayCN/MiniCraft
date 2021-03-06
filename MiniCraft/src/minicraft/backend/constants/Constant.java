package minicraft.backend.constants;

import com.jme3.audio.*;

public class Constant {
    public static final int minX = -128;
    public static final int minZ = -128;
    public static final int maxX = 127;
    public static final int maxZ = 127;
    public static final int minY = 0;
    public static final int maxY = 64;

    public static final int chunkX = 16;
    public static final int chunkZ = 16;


    public static final String saveDir = "./saves";

    public static final String titleMusic = "menu2.ogg";
    public static final String inGameStrinig = "calm3.ogg";

    // 一行5个，方便数数
    public static final String[] BLOCKNAME_STRINGS = {
        "Air", "Stone", "Grass", "Dirt", "Bedrock",
        "Glass","SpruceLog","OakLeaves", "OakPlank", "StoneBrick",
        "Sand", "Cobblestone", "Brick", "Gold", "Iron",
        "Pumpkin", "Melon", "Slime"};
    //方块id常量
    public static final int BLOCK_AIR=0;
    public static final int BLOCK_STONE=1;
    public static final int BLOCK_GRASS=2;
    public static final int BLOCK_DIRT=3;
    public static final int BLOCK_BEDROCK=4;
    public static final int BLOCK_GLASS=5;
    public static final int BLOCK_SPRUCE_LOG=6;
    public static final int BLOCK_OAK_LEAVES=7;
    public static final int BLOCK_OAK_PLANK = 8;
    public static final int BLOCK_STONE_BRICK = 9;
    public static final int BLOCK_SAND = 10;
    public static final int BLOCK_COBBLESTONE = 11;
    public static final int BLOCK_BRICK = 12;
    public static final int BLOCK_GOLD_BLOCK = 13;
    public static final int BLOCK_IRON_BLOCK = 14;
    public static final int BLOCK_PUMPKIN = 15;
    public static final int BLOCK_MELON = 16;
    public static final int BLOCK_SLIME = 17;

    //方块种类数
    public static final int BLOCK_TYPE_NUM=BLOCKNAME_STRINGS.length;
    
    public static final int chunkMinCoordX = (int)Math.floor(minX / chunkX);
    public static final int chunkMaxCoordX = (int)Math.floor(maxX / chunkX);
    public static final int chunkMinCoordZ = (int)Math.floor(minZ / chunkZ);
    public static final int chunkMaxCoordZ = (int)Math.floor(maxZ / chunkZ);
}