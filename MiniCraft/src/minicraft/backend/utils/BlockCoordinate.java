package minicraft.backend.utils;

import minicraft.backend.constants.Constant;

public class BlockCoordinate {
    private int x;
    private int y;
    private int z;

    public BlockCoordinate(){
        x = y = z = 0;
    }

    public BlockCoordinate(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void addXYZ(int addX, int addY, int addZ){
        this.x += addX;
        this.y += addY;
        this.z += addZ;
    }

    public void setXYZ(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ChunkCoordinate toChunkCoordnate(){
        int cx = x , cz = z;
        if(cx < 0) cx -= Constant.chunkX - 1;
        if(cz < 0) cz -= Constant.chunkZ - 1;
        return new ChunkCoordinate(cx / Constant.chunkX, cz / Constant.chunkZ);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }
}