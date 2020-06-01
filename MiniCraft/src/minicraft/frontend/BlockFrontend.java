package minicraft.frontend;

import minicraft.backend.utils.*;
import minicraft.backend.constants.Constant;
import minicraft.backend.map.block.BlockBackend;

import java.nio.FloatBuffer;

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
	
	protected BlockFrontend() {
		super(0.5f,0.5f,0.5f);
	}
	
	/**
     * 将函数化为static，总共只计算一次，以优化性能
     *
     * @return a newly created array of vertex vectors.
     * @see com.jme3.scene.shape.Box.computeVertices()
     */
    protected static Vector3f[] computeVerticesStatic() {
        Vector3f[] axes = {
                Vector3f.UNIT_X.mult(0.5f),
                Vector3f.UNIT_Y.mult(0.5f),
                Vector3f.UNIT_Z.mult(0.5f)
        };
        Vector3f origin=new Vector3f(0,0,0);
        return new Vector3f[] {
        		origin.subtract(axes[0]).subtractLocal(axes[1]).subtractLocal(axes[2]),
        		origin.add(axes[0]).subtractLocal(axes[1]).subtractLocal(axes[2]),
        		origin.add(axes[0]).addLocal(axes[1]).subtractLocal(axes[2]),
        		origin.subtract(axes[0]).addLocal(axes[1]).subtractLocal(axes[2]),
        		origin.add(axes[0]).subtractLocal(axes[1]).addLocal(axes[2]),
        		origin.subtract(axes[0]).subtractLocal(axes[1]).addLocal(axes[2]),
        		origin.add(axes[0]).addLocal(axes[1]).addLocal(axes[2]),
        		origin.subtract(axes[0]).addLocal(axes[1]).addLocal(axes[2])
        };
    }
    static FloatBuffer GEOMETRY_VERTICES_DATA;
	static {
		GEOMETRY_VERTICES_DATA = BufferUtils.createVector3Buffer(24);
	    Vector3f[] v = computeVerticesStatic();
	    GEOMETRY_VERTICES_DATA.put(new float[] {
	                v[0].x, v[0].y, v[0].z, v[1].x, v[1].y, v[1].z, v[2].x, v[2].y, v[2].z, v[3].x, v[3].y, v[3].z, // back
	                v[1].x, v[1].y, v[1].z, v[4].x, v[4].y, v[4].z, v[6].x, v[6].y, v[6].z, v[2].x, v[2].y, v[2].z, // right
	                v[4].x, v[4].y, v[4].z, v[5].x, v[5].y, v[5].z, v[7].x, v[7].y, v[7].z, v[6].x, v[6].y, v[6].z, // front
	                v[5].x, v[5].y, v[5].z, v[0].x, v[0].y, v[0].z, v[3].x, v[3].y, v[3].z, v[7].x, v[7].y, v[7].z, // left
	                v[2].x, v[2].y, v[2].z, v[6].x, v[6].y, v[6].z, v[7].x, v[7].y, v[7].z, v[3].x, v[3].y, v[3].z, // top
	                v[0].x, v[0].y, v[0].z, v[5].x, v[5].y, v[5].z, v[4].x, v[4].y, v[4].z, v[1].x, v[1].y, v[1].z  // bottom
	    });
	}
	
	
	@Override
    protected void doUpdateGeometryVertices() {
       
        setBuffer(Type.Position, 3, GEOMETRY_VERTICES_DATA);
        updateBound();
    }


    public static BlockFrontend getBlockInstanceByID(int Blockid){
    	if(BlockBackend.getBlockInstanceByID(Blockid).isTextureUniform())
    		return new BlockFrontend();
    	else
    		return new BlockFrontendTexUnuniform();
    }
}
