package minicraft.backend.map.generator.perlin;

import org.junit.Test;

public class PerlinTest {
    @Test
    public void showPerlinOutput(){
        Perlin perlin = new Perlin(128, 128, false);
        double[][] perlinArray = perlin.getPerlinNoise();

        for(int i = 0; i < perlinArray.length; i++){
            for(int j = 0; j < perlinArray[i].length; j++){
                System.out.printf("%d ", (int)(perlinArray[i][j] * 14));
            }
            System.out.println();
        }
    }
}