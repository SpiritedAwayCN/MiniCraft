package minicraft.backend.entity;

import com.jme3.math.Vector3f;

import minicraft.backend.utils.ChunkCoord;

public class EntityForSave {
    protected Vector3f coordinate;
    protected String name;
    protected ChunkCoord chunkCoordinate;
    protected Vector3f naturalV;
    protected boolean onGround;
    protected boolean lowGravelty;

    public EntityForSave(){}
    public EntityForSave(Entity e){
        this.coordinate = e.getCoordinate();
        this.name = e.getName();
        this.chunkCoordinate = e.getChunkCoordinate();
        this.naturalV = e.naturalV;
        this.onGround = e.getOnGround();
        this.lowGravelty = e.getLowGravelty();
    }

    public ChunkCoord getChunkCoordinate() {
        return chunkCoordinate;
    }
    public Vector3f getCoordinate() {
        return coordinate;
    }
    public String getName() {
        return name;
    }
    public Vector3f getNaturalV() {
        return naturalV;
    }
    public boolean getLowGravelty(){
        return lowGravelty;
    }
    public boolean getOnGround(){
        return onGround;
    }
    public void setChunkCoordinate(ChunkCoord chunkCoordinate) {
        this.chunkCoordinate = chunkCoordinate;
    }
    public void setCoordinate(Vector3f coordinate) {
        this.coordinate = coordinate;
    }
    public void setLowGravelty(boolean lowGravelty) {
        this.lowGravelty = lowGravelty;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNaturalV(Vector3f naturalV) {
        this.naturalV = naturalV;
    }
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}