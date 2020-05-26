package minecraft.backend.map.generator;

import minecraft.backend.constants.Constant;
import minecraft.backend.map.block.*;

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

        blockMap = new int[Constant.maxX + biasX][Constant.maxY][Constant.maxZ + biasZ];

        for(int i = 0; i < Constant.maxX + biasX; i++)
            for(int j = 0; j < Constant.maxZ + biasZ; j++){
                blockMap[i][0][j] = 4; //bedrock
                int k = 1;
                for(; k < heightMap[i][j] - 3; k++)
                    blockMap[i][k][j] = 1; //stone
                for(; k < heightMap[i][j]; k++)
                    blockMap[i][k][j] = 3; //dirt
                blockMap[i][k][j] = 2; //grass
                for(k++; k < Constant.maxY; k++)
                    blockMap[i][k][j] = AirBlock.getBlockidStatic();
            }

    }

    public int[][][] getBlockMap() {
        return blockMap;
    }
}