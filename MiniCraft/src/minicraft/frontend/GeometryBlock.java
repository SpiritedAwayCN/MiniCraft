package minicraft.frontend;

import minicraft.backend.constants.*;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.utils.BlockCoord;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;

public class GeometryBlock extends Geometry {
	
private static Material[] materails;
	
	static {
		if(!Assets.initialized)
			throw new RuntimeException();
		
		materails=new Material[Constant.BLOCK_TYPE_NUM];

		for(int Blockid=0;Blockid<Constant.BLOCK_TYPE_NUM;Blockid++) {
			// 设置受光材质
			materails[Blockid] = Assets.MATERIAL_LIGHTING.clone();
			
			// 设置纹理贴图
	        materails[Blockid].setTexture("DiffuseMap", Assets.BLOCK_TEXTURE[Blockid]);

	        // 设置反光度
	        materails[Blockid].setFloat("Shininess", 2.0f);
		}      
	}
	
	
	//BlockFrontend block;
	BlockBackend block;
	
	@Deprecated
	public GeometryBlock(BlockCoord pos,int Blockid) {
		super(null, BlockFrontend.getBlockInstanceByID(Blockid));
		this.setMaterial(materails[Blockid]);
		this.move(pos.getX(),pos.getY(),pos.getZ());
	}
	public GeometryBlock() {
		super(null, new BlockFrontend());
		this.block=null;
	}
	public GeometryBlock(BlockBackend block) {
		super(null,BlockFrontend.getBlockInstanceByID(block.getBlockid()));
		this.setMaterial(materails[block.getBlockid()]);
		BlockCoord pos=block.getBlockCoord();
		this.move(pos.getX(),pos.getY(),pos.getZ());
		this.block=block;
		
	}
	
	@Override
	public int hashCode() {
		if(block==null)
			return -1;
		BlockCoord r=block.getBlockCoord();
		return (r.getX()-Constant.minX)*Constant.maxY*(Constant.maxZ-Constant.minZ)
				+(r.getY()-Constant.minY)*(Constant.maxZ-Constant.minZ)
				+(r.getZ()-Constant.minZ);
	}
}