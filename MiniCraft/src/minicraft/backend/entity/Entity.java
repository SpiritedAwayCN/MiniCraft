package minicraft.backend.entity;

import com.jme3.math.Vector3f;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.Chunk;
import minicraft.backend.map.DimensionMap;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.utils.*;

public abstract class Entity {
    protected Vector3f coordinate;
    protected String name;
    protected ChunkCoord chunkCoordinate;
    protected Vector3f naturalV = new Vector3f(0, 0, 0);
    protected boolean onGround;
    protected boolean lowGravelty;
    protected DimensionMap map;
    protected static final float hitBoxWide = (float)0.2;
    protected static final float hitBoxHeight = (float)1.8;

    protected final Vector3f G = new Vector3f(0, (float)-0.04, 0);
    // 受重力
    
    Entity(){}

    public Entity(Vector3f coordinate, String name, DimensionMap map){
        this.coordinate = coordinate;
        this.chunkCoordinate = toChunkCoordinate();
        this.map = map;
    }

    public Entity(EntityForSave es){
        this.chunkCoordinate = es.getChunkCoordinate();
        this.name = es.getName();
        this.coordinate = es.getCoordinate();
        this.onGround = es.getOnGround();
        this.lowGravelty = es.getLowGravelty();
        this.naturalV = es.getNaturalV();
    }

    public String getName() {
        return name;
    }

    public Vector3f getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Vector3f coordinate) {
        this.coordinate = coordinate;
        checkFoot();
    }

    public void updateCoordinate(Vector3f coord){
        int factorX = coord.x > coordinate.x ? 1 : -1;
        int factorZ = coord.z > coordinate.z ? 1 : -1;
        int ox = Math.round(coordinate.x), oz = Math.round(coordinate.z);
        int bx = ox + factorX;
        int bz = oz + factorZ;
        int nx = Math.round(coord.x + hitBoxWide * factorX), nz = Math.round(coord.z + hitBoxWide * factorZ);

        int sy = (int)Math.floor(coord.y), ty = (int)Math.floor(coord.y + hitBoxHeight);
        if(nx != ox){
            for(BlockCoord blockCoord = new BlockCoord(bx, sy, oz); blockCoord.getY() <= ty;
                blockCoord.addXYZ(0, 1, 0)){
                    BlockBackend block = map.getBlockByCoord(blockCoord);
                    if(block == null || block.isFullHitbox()){
                        coord.x = bx - (hitBoxWide + 0.5f) * factorX;
                        break;
                    }
            }
        }
        if(nz != oz){
            for(BlockCoord blockCoord = new BlockCoord(ox, sy, bz); blockCoord.getY() <= ty;
                blockCoord.addXYZ(0, 1, 0)){
                    BlockBackend block = map.getBlockByCoord(blockCoord);
                    if(block == null || block.isFullHitbox()){
                        coord.z = bz - (hitBoxWide + 0.5f) * factorZ;
                        break;
                    }
            }
        }
    }

    public ChunkCoord toChunkCoordinate(){
        return new ChunkCoord((int)Math.floor(coordinate.x / Constant.chunkX), (int)Math.floor(coordinate.z / Constant.chunkZ));
    }

    public BlockCoord toBlockCoordinate(){
        return new BlockCoord((int)Math.round(coordinate.x), (int)Math.round(coordinate.y), (int)Math.round(coordinate.z));
    }

    public ChunkCoord getChunkCoordinate() {
        return chunkCoordinate;
    }

    public void setChunkCoordinate(ChunkCoord chunkCoordinate) {
        this.chunkCoordinate = chunkCoordinate;
    }

    public boolean updateFalling(float dt){
        if(lowGravelty || onGround) return false;
        // System.out.println(dt);
        naturalV.addLocal(G);
        // System.out.println("ny " + naturalV.y);
        if(naturalV.y < -5) naturalV.y = -5;
        int oy = (int)Math.round(coordinate.y), ny = (int)Math.round(coordinate.y + naturalV.y);
        int x = (int)Math.round(coordinate.x), z = (int)Math.round(coordinate.z);
        int uy = (int)Math.floor(coordinate.y + naturalV.y + hitBoxHeight);
        BlockBackend block = map.getBlockByCoord(new BlockCoord(x, uy, z));
        if(naturalV.y > 0 && block != null && block.isFullHitbox()){
            naturalV.y = 0;
            coordinate.y = uy - hitBoxHeight;
            return true;
        }
        
        for(BlockCoord blockCoord = new BlockCoord(x, oy, z); blockCoord.getY() >= ny && blockCoord.getY() > 0;
                blockCoord.addXYZ(0, -1, 0)){
            block = map.getBlockByCoord(blockCoord);
            if(block == null || block.isFullHitbox()){
                onGround = true;
                naturalV.y = 0;
                coordinate.y = blockCoord.getY() + 1;
                // System.out.println(blockCoord);
                return true;
            }
        }
        coordinate.y += naturalV.y;
        // System.out.println("cy " + coordinate.y);
        if(coordinate.y < 0) coordinate.y = 0;
        return true;
    }

    public boolean getLowGravelty(){
        return lowGravelty;
    }

    public boolean getOnGround(){
        return onGround;
    }

    public void setNaturalV(Vector3f naturalV) {
        this.naturalV = naturalV;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void checkFoot(){
        BlockBackend block = map.getBlockByCoord(toBlockCoordinate().addXYZ(0, -1, 0));
        if(block != null && !block.isFullHitbox())
            onGround = false;
    }
    public EntityForSave toSave(){
        return new EntityForSave(this);
    }
    public void setLowGravelty(boolean lowGravelty) {
        this.lowGravelty = lowGravelty;
    }

}