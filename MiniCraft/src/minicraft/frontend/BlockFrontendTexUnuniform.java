package minicraft.frontend;

import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class BlockFrontendTexUnuniform extends BlockFrontend {
	private static final float[] GEOMETRY_TEXTURE_DATA_UNUNIFORM = {
			1, 0.5f, 0.5f, 0.5f, 0.5f, 1, 1, 1, // back
			1, 0.5f, 0.5f, 0.5f, 0.5f, 1, 1, 1, // right
			1, 0.5f, 0.5f, 0.5f, 0.5f, 1, 1, 1, // front
			1, 0.5f, 0.5f, 0.5f, 0.5f, 1, 1, 1, // left
	        0.5f, 0.5f, 0, 0.5f, 0, 1, 0.5f, 1, // top
	        0.5f, 0, 0, 0, 0, 0.5f, 0.5f, 0.5f,  // bottom
	    };
	
	protected BlockFrontendTexUnuniform(){
		super();
	}
	
	@Override
    protected void doUpdateGeometryTextures() {
        if (getBuffer(Type.TexCoord) == null){
        	setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(GEOMETRY_TEXTURE_DATA_UNUNIFORM));
        }
    }
}
