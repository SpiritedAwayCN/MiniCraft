package minicraft.backend.utils;

import minicraft.backend.constants.Constant;

public class ChunkCoordinate {
    private int x;
    private int z;

    public ChunkCoordinate(){
        x = z = 0;
    }
    public ChunkCoordinate(int x, int z){
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

    public ChunkCoordinate addXZ(int addX, int addZ){
        this.x += addX;
        this.z += addZ;
        return this;
    }

    public ChunkCoordinate setXZ(int x, int z){
        this.x = x;
        this.z = z;
        return this;
    }
    public boolean isValid(){
        return x >= Constant.chunkMinCoordX && x <= Constant.chunkMaxCoordX
            && z >= Constant.chunkMinCoordZ && z <= Constant.chunkMaxCoordZ;
    }
}