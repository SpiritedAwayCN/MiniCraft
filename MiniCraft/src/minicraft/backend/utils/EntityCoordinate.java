package minicraft.backend.utils;

public class EntityCoordinate {
    private double x;
    private double y;
    private double z;

    public EntityCoordinate(){
        x = y = z = 0;
    }

    public EntityCoordinate(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void addXYZ(double addX, double addY, double addZ){
        this.x += addX;
        this.y += addY;
        this.z += addZ;
    }

    public void setXYZ(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}