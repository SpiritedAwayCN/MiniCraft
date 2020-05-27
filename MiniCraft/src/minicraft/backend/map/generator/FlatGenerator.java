package minicraft.backend.map.generator;

import minicraft.backend.constants.Constant;

public class FlatGenerator extends Generator {

    @Override
    protected void generateHightMap() {
        heightMap = new int[Constant.maxX + biasX + 1][Constant.maxZ + biasZ + 1];

        for(int i = 0; i <= Constant.maxX + biasX; i++)
            for(int j = 0; j <= Constant.maxZ + biasZ; j++){
                heightMap[i][j] = 4;
            }

    }
    
}