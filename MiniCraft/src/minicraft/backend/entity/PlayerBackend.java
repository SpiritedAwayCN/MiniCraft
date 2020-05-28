package minicraft.backend.entity;

import com.jme3.math.Vector3f;

public class PlayerBackend extends Entity{
    public PlayerBackend(){
        super(new Vector3f(0, 0, 0), "Steve");
    }

    public PlayerBackend(Vector3f coord, String name){
        super(coord, name);
    }

    @Override
    public void setCoordinate(Vector3f coordinate) {
        super.setCoordinate(coordinate);
        
    }
}