package minicraft.backend.map.generator;

import java.util.Random;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.block.*;
import minicraft.backend.map.generator.perlin.Perlin;

public class TerrainGenerator extends Generator {
    public TerrainGenerator(){
        this.random = new Random();
    }

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
    
    @Override
    public void generateBlockMap() {
        super.generateBlockMap();
        generateTrees(random.nextInt(6) + 15, random.nextInt(25) + 40);
    }
    Random random;
    private int rx, rz;
    private final int[] dx = {1, 0, -1, 0}, dz = {0, 1, 0, -1};
    private void randomWalk(int sx, int sz, int att){
        rx = sx; rz = sz;
        for(int i = 0; i < att; i++){
            int dir = random.nextInt(4);
            rx += dx[dir];
            rz += dz[dir];
            if(rx < 0) rx = 0;
            if(rx >= heightMap.length) rx = heightMap.length - 1;
            if(rz < 0) rz = 0;
            if(rz >= heightMap[0].length) rz = heightMap[0].length - 1;
        }
    }

    private final int treeMinDist = 5;
    private void generateTrees(int att, int attEach){
        for(int i = 0; i < att; i++){
            rx = random.nextInt(heightMap.length);
            rz = random.nextInt(heightMap[0].length);
            outer:for(int j = 0; j < attEach; j++){
                int sx = rx, sz = rz;
                randomWalk(sx, sz, 15);
                for(int ii = -treeMinDist; ii <= treeMinDist; ii++)
                    for(int jj = -treeMinDist; jj <=treeMinDist; jj++){
                        int x = rx + ii, z = rz + jj;
                        if(x < 0 || x >= heightMap.length || z < 0 || z >= heightMap[0].length) continue;
                        if(blockMap[x][heightMap[x][z] + 1][z] == SpruceLogBlock.getBlockidStatic()) continue outer;
                    }
                int h = random.nextInt(4) + 4;
                for(int bh = 1; bh <= h + 1; bh++){
                    if(heightMap[rx][rz] >= Constant.maxY) break;
                    if(bh <= h)
                        blockMap[rx][heightMap[rx][rz] + bh][rz] = SpruceLogBlock.getBlockidStatic();
                    if(bh > h - 1){
                        for(int ii = -1; ii <= 1; ii++)
                            for(int jj = -1; jj <= 1; jj++){
                                int lx = rx + ii, lz = rz + jj;
                                if(lx < 0 || lz < 0 || lx >= heightMap.length || lz>=heightMap[0].length) continue;
                                if(blockMap[lx][heightMap[lx][lz] + bh][lz] == 0)
                                    blockMap[lx][heightMap[lx][lz] + bh][lz] = OakLeavesBlock.getBlockidStatic();
                            }
                                
                    }else if(bh > h-3 || (h > 5 && bh > h-4)){
                        for(int ii = -2; ii <= 2; ii++)
                            for(int jj = -2; jj <= 2; jj++){
                                int lx = rx + ii, lz = rz + jj;
                                if(lx < 0 || lz < 0 || lx >= heightMap.length || lz>=heightMap[0].length) continue;
                                if(blockMap[lx][heightMap[lx][lz] + bh][lz] == 0)
                                    blockMap[lx][heightMap[lx][lz] + bh][lz] = OakLeavesBlock.getBlockidStatic();
                            }
                    }
                }
            }
        }
    }
}