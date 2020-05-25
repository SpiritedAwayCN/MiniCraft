package minicraft.frontend;

import com.jme3.math.Vector3f;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.util.BufferUtils;

public class Block extends Box {
	private static final float[] GEOMETRY_TEXTURE_DATA_OVERRIDE = {
	        1, 0, 0, 0, 0, 0.5f, 1, 0.5f, // back
	        1, 0, 0, 0, 0, 0.5f, 1, 0.5f, // right
	        1, 0, 0, 0, 0, 0.5f, 1, 0.5f, // front
	        1, 0, 0, 0, 0, 0.5f, 1, 0.5f, // left
	        1, 0.5f, 0, 0.5f, 0, 1, 1, 1, // top
	        1, 0, 0, 0, 0, 0.5f, 1, 0.5f  // bottom
	    };
	
	
	public Block(float x, float y, float z) {
        super(x,y,z);
    }

    
    @Deprecated
    public Block(Vector3f center, float x, float y, float z) {
        super(center, x, y, z);
    }

    
    public Block(Vector3f min, Vector3f max) {
        super(min,max);
    }

    /**
     * Empty constructor for serialization only. Do not use.
     */
    protected Block(){
        super();
    }
	
	@Override
    protected void doUpdateGeometryTextures() {
        if (getBuffer(Type.TexCoord) == null){
            setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(GEOMETRY_TEXTURE_DATA_OVERRIDE));
        }
    }
}
