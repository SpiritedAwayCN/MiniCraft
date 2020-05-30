package minicraft.frontend;

import minicraft.backend.utils.*;
import minicraft.backend.constants.Constant;
import minicraft.backend.map.block.BlockBackend;

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
	
	
	
	@Deprecated
	protected BlockFrontend(float x, float y, float z) {
        super(x,y,z);
    }

    
    @Deprecated
    protected BlockFrontend(Vector3f center, float x, float y, float z) {
        super(center, x, y, z);
    }


    /**
     * Empty constructor for serialization only. Do not use.
     */
    //protected BlockFrontend(){
    //    super();
    //}

    public static BlockFrontend getBlockInstanceByID(int Blockid){
    	if(BlockBackend.getBlockInstanceByID(Blockid).isTextureUniform())
    		return new BlockFrontend();
    	else
    		return new BlockFrontendTexUnuniform();
    }
}
