package minicraft.frontend;

import minicraft.backend.utils.*;
import minicraft.backend.constants.Constant;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.util.BufferUtils;

public class BlockFrontend extends Box {
	//各面材质不相等的方块的纹理坐标数据，如草方块
	private static final float[] GEOMETRY_TEXTURE_DATA_UNUNIFORM = {
			1, 0.5f, 0.5f, 0.5f, 0.5f, 1, 1, 1, // back
			1, 0.5f, 0.5f, 0.5f, 0.5f, 1, 1, 1, // right
			1, 0.5f, 0.5f, 0.5f, 0.5f, 1, 1, 1, // front
			1, 0.5f, 0.5f, 0.5f, 0.5f, 1, 1, 1, // left
	        0.5f, 0.5f, 0, 0.5f, 0, 1, 0.5f, 1, // top
	        0.5f, 0, 0, 0, 0, 0.5f, 0.5f, 0.5f,  // bottom
	    };
	private static final float[] GEOMETRY_TEXTURE_DATA_UNIFORM = {
	        1, 0, 0, 0, 0, 1, 1, 1, // back
	        1, 0, 0, 0, 0, 1, 1, 1, // right
	        1, 0, 0, 0, 0, 1, 1, 1, // front
	        1, 0, 0, 0, 0, 1, 1, 1, // left
	        1, 0, 0, 0, 0, 1, 1, 1, // top
	        1, 0, 0, 0, 0, 1, 1, 1  // bottom
	    };
	private static final int[] BLOCK_ID_TEXTURE_UNUNIFORM= {
			2
	};
	private boolean BLOCK_TEXTURE_UNIFORM=true;
	private boolean BLOCK_IS_EMPTY=true;
	
	public BlockFrontend(int Blockid) {
		super(1,1,1);//边长为1
		for(int id : BLOCK_ID_TEXTURE_UNUNIFORM) {
			if(id==Blockid) {
				BLOCK_TEXTURE_UNIFORM=false;
				break;
			}
		}
	}
	public BlockFrontend() {
		super(1,1,1);
	}
	public void setBlockid(int Blockid) {
		for(int id : BLOCK_ID_TEXTURE_UNUNIFORM) {
			if(id==Blockid) {
				BLOCK_TEXTURE_UNIFORM=false;
				break;
			}
		}
		this.doUpdateGeometryTextures();
		if(BLOCK_IS_EMPTY!=(Blockid==0)) {
			BLOCK_IS_EMPTY=Blockid==0;
		}
	}
	
	@Deprecated
	public BlockFrontend(float x, float y, float z) {
        super(x,y,z);
    }

    
    @Deprecated
    public BlockFrontend(Vector3f center, float x, float y, float z) {
        super(center, x, y, z);
    }


    /**
     * Empty constructor for serialization only. Do not use.
     */
    //protected BlockFrontend(){
    //    super();
    //}
	
	@Override
    protected void doUpdateGeometryTextures() {
        if (getBuffer(Type.TexCoord) == null){
        	if(BLOCK_TEXTURE_UNIFORM) {
        		setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(GEOMETRY_TEXTURE_DATA_UNIFORM));
        	}else {
        		setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(GEOMETRY_TEXTURE_DATA_UNUNIFORM));
        	}
        }
    }
	private static final short[] GEOMETRY_INDICES_DATA_EMPTY = {};
	private static final short[] GEOMETRY_INDICES_DATA_OVERRIDE = {
	         2,  1,  0,  3,  2,  0, // back
	         6,  5,  4,  7,  6,  4, // right
	        10,  9,  8, 11, 10,  8, // front
	        14, 13, 12, 15, 14, 12, // left
	        18, 17, 16, 19, 18, 16, // top
	        22, 21, 20, 23, 22, 20  // bottom
	    };
    @Override
    protected void doUpdateGeometryIndices() {
        if (getBuffer(Type.Index) == null){
        	if(BLOCK_IS_EMPTY) {
        		setBuffer(Type.Index, 3, BufferUtils.createShortBuffer(GEOMETRY_INDICES_DATA_EMPTY));
        	}else {
        		setBuffer(Type.Index, 3, BufferUtils.createShortBuffer(GEOMETRY_INDICES_DATA_OVERRIDE));
            	
        	}
        }
    }
}
