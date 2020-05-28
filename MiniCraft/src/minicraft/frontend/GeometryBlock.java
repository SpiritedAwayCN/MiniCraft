package minicraft.frontend;

import minicraft.backend.constants.*;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.utils.BlockCoordinate;

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
	

	
	public static void initialize (AssetManager assetManager)
			throws AssetNotFoundException
	{
		loadMaterial(assetManager);
	}
	private static void loadMaterial(AssetManager assetManager) 
			throws AssetNotFoundException{
		materails=new Material[Constant.BLOCK_TYPE_NUM];
		
		//待修改——改为map
		final String[] texFilename= {
				null,//air
				"texture/stone.bmp",
				"texture/grass.bmp",
				"texture/dirt.bmp",
				"texture/bedrock.bmp",
				null//glass
		};
		
		assetManager.registerLocator("assets", FileLocator.class);
		for(int i=0;i<materails.length;i++) {
			// 设置受光材质
			materails[i] = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
			// 设置纹理贴图
			if(texFilename[i]!=null){
				TextureKey param=new TextureKey(texFilename[i]);
				param.setGenerateMips(true);
				Texture tex = assetManager.loadTexture(param);
				
	        	tex.setMagFilter(MagFilter.Nearest);
	        	materails[i].setTexture("DiffuseMap", tex);
			}
	        
	        // 设置反光度
	        materails[i].setFloat("Shininess", 2.0f);
		}      
        // #3 创造1个方块，应用此材质。
        //geom = new Geometry("grass", new Block(1, 1, 1));
		//geom.setMaterial(mat);
	}
	
	//BlockFrontend block;
	BlockBackend block;
	
	@Deprecated
	public GeometryBlock(BlockCoordinate pos,int Blockid) {
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
		BlockCoordinate pos=block.getBlockCoordinate();
		this.move(pos.getX(),pos.getY(),pos.getZ());
		this.block=block;
		
	}
	
	@Override
	public int hashCode() {
		if(block==null)
			return -1;
		BlockCoordinate r=block.getBlockCoordinate();
		return (r.getX()-Constant.minX)*Constant.maxY*(Constant.maxZ-Constant.minZ)
				+(r.getY()-Constant.minY)*(Constant.maxZ-Constant.minZ)
				+(r.getZ()-Constant.minZ);
	}
}