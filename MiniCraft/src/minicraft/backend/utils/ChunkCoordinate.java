package minicraft.backend.utils;

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

    public void addXZ(int addX, int addZ){
        this.x += addX;
        this.z += addZ;
    }

    public void setXZ(int x, int z){
        this.x = x;
        this.z = z;
    }
}