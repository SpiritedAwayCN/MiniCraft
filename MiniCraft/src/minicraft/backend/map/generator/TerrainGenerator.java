package minicraft.backend.map.generator;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.generator.perlin.Perlin;

public class TerrainGenerator extends Generator {

    @Override
    protected void generateHightMap() {
        heightMap = new int[Constant.maxX + biasX + 1][Constant.maxZ + biasZ + 1];
        Perlin perlin = new Perlin(Constant.maxX + biasX + 1, Constant.maxZ + biasZ + 1, false);
        double[][] noise = perlin.getPerlinNoise();
        for(int i = 0; i < noise.length; i++)
            for(int j = 0; j < noise[i].length; j++){
                double factor = 1;
                if(noise[i][j] > 0.8) factor += Math.min(26 * (noise[i][j] - 0.8), 4) ;
                heightMap[i][j] = (int)(noise[i][j] * 10 * factor) + 4;
            }
        
    }
    
}