package minicraft.backend.utils;

import minicraft.backend.constants.Constant;

public class BlockCoord {
    private int x;
    private int y;
    private int z;

    public BlockCoord(){
        x = y = z = 0;
    }

    public BlockCoord(int x, int y, int z){
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

    public BlockCoord addXYZ(int addX, int addY, int addZ){
        this.x += addX;
        this.y += addY;
        this.z += addZ;
        return this;
    }

    public BlockCoord setXYZ(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public ChunkCoord toChunkCoord(){
        int cx = x , cz = z;
        if(cx < 0) cx -= Constant.chunkX - 1;
        if(cz < 0) cz -= Constant.chunkZ - 1;
        return new ChunkCoord(cx / Constant.chunkX, cz / Constant.chunkZ);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    @Override
    public boolean equals(Object obj) {
        BlockCoord bc = (BlockCoord)obj;
        return x == bc.x && y == bc.y && z == bc.z;
    }
}