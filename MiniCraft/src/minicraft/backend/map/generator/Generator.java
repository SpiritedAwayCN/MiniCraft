package minicraft.backend.map.generator;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.block.*;

public abstract class Generator {
    protected int[][] heightMap;
    protected int[][][] blockMap;
    protected int biasX = -Constant.minX;
    protected int biasZ = -Constant.minZ;


    protected abstract void generateHightMap();

    protected void fixHeightMap(){
        for(int i = 0; i < heightMap.length; i++)
            for(int j = 0; j < heightMap[i].length; j++){
                if(heightMap[i][j] >= Constant.maxY) heightMap[i][j] = Constant.maxY - 1;
                if(heightMap[i][j] < 0) heightMap[i][j] = 0;
            }
    }

    public void generateBlockMap(){
        generateHightMap();
        fixHeightMap();

        blockMap = new int[Constant.maxX + biasX + 1][Constant.maxY][Constant.maxZ + biasZ + 1];

        for(int i = 0; i <= Constant.maxX + biasX; i++)
            for(int j = 0; j <= Constant.maxZ + biasZ; j++){
                blockMap[i][0][j] = BedrockBlock.getBlockidStatic(); //bedrock
                int k = 1;
                for(; k < heightMap[i][j] - 3; k++)
                    blockMap[i][k][j] = StoneBlock.getBlockidStatic();
                for(; k < heightMap[i][j]; k++)
                    blockMap[i][k][j] = DirtBlock.getBlockidStatic(); //dirt
                blockMap[i][k][j] = GrassBlock.getBlockidStatic(); //grass
                for(k++; k < Constant.maxY; k++)
                    blockMap[i][k][j] = AirBlock.getBlockidStatic(); //air
            }

    }

    public int[][][] getBlockMap() {
        return blockMap;
    }
}