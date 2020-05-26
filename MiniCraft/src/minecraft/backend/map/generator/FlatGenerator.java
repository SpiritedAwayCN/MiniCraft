package minecraft.backend.map.generator;

import minecraft.backend.constants.Constant;

public class FlatGenerator extends Generator {

    @Override
    protected void generateHightMap() {
        heightMap = new int[Constant.maxX + biasX][Constant.maxZ + biasZ];

        for(int i = 0; i < Constant.maxX + biasX; i++)
            for(int j = 0; j < Constant.maxZ + biasZ; j++){
                heightMap[i][j] = 4;
            }

    }
    
}