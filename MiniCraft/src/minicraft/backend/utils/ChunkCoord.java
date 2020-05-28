package minicraft.backend.utils;

import minicraft.backend.constants.Constant;

public class ChunkCoord {
    private int x;
    private int z;

    public ChunkCoord(){
        x = z = 0;
    }
    public ChunkCoord(int x, int z){
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public ChunkCoord addXZ(int addX, int addZ){
        this.x += addX;
        this.z += addZ;
        return this;
    }

    public ChunkCoord setXZ(int x, int z){
        this.x = x;
        this.z = z;
        return this;
    }
    public boolean isValid(){
        return x >= Constant.chunkMinCoordX && x <= Constant.chunkMaxCoordX
            && z >= Constant.chunkMinCoordZ && z <= Constant.chunkMaxCoordZ;
    }
}