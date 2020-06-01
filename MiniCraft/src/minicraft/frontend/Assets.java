package minicraft.frontend;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.block.BlockBackend;

public class Assets {
	public static Texture[] BLOCK_TEXTURE;
	public static Texture[] BLOCK_IMAGE;
	public static Material[] BLOCK_MATERIAL;
	public static Texture BLOCK_BOX,BLOCK_BOX_SELECTED;
	public static Texture NULL_TEXTURE;
	
	public static Texture GUI_BUTTON;
	public static Texture GUI_START_MENU_BACKGROUND;
	
	public static Material MATERIAL_LIGHTING;
	public static Material MATERIAL_UNSHADED;
	
	public static boolean initialized=false;
	
	public static AssetManager assetManager;
	
	public static void initialize(AssetManager assetManager) {
		Assets.assetManager=assetManager;
		
		assetManager.registerLocator("assets", FileLocator.class);
		
		final String[] blockTexFilepath= {
				null,//air
				"texture/stone.bmp",
				"texture/grass.png",
				"texture/dirt.bmp",
				"texture/bedrock.bmp",
				"texture/glass.png",
				"texture/spruce_log.png",
				"texture/oak_leaves.png",
				"texture/oak_planks.png",
				"texture/stone_bricks.png",
				"texture/sand.bmp",
				"texture/cobblestone.png",
				"texture/bricks.png",
				"texture/gold_block.png",
				"texture/iron_block.png"
		};
		final String[] blockImageFilepath= {
				null,//air
				"gui/block_stone.png",
				"gui/block_grass.png",
				"gui/block_dirt.png",
				"gui/block_bedrock.png",
				"gui/block_glass.png",
				"gui/block_spruce_log.png",
				"gui/block_oak_leaves.png",
				"gui/block_oak_planks.png",
				"gui/block_stone_bricks.png",
				"gui/block_sand.png",
				"gui/block_cobblestone.png",
				"gui/block_brick.png",
				"gui/block_gold.png",
				"gui/block_iron.png"
		};
		

		//TextureKey param=new TextureKey(texFilename[i]);
		//param.setGenerateMips(true);
		//Texture tex = assetManager.loadTexture(param);
		BLOCK_TEXTURE=new Texture[Constant.BLOCK_TYPE_NUM];
		for(int i=0;i<Constant.BLOCK_TYPE_NUM;i++) {
			if(blockTexFilepath[i]==null)
				continue;
			BLOCK_TEXTURE[i]=assetManager.loadTexture(blockTexFilepath[i]);
			BLOCK_TEXTURE[i].setMagFilter(MagFilter.Nearest);
		}
		
		BLOCK_IMAGE=new Texture[Constant.BLOCK_TYPE_NUM];
		for(int i=0;i<Constant.BLOCK_TYPE_NUM;i++) {
			if(blockImageFilepath[i]==null)
				continue;
			BLOCK_IMAGE[i]=assetManager.loadTexture(blockImageFilepath[i]);
			//BLOCK_IMAGE[i].setMagFilter(MagFilter.);
		}
		
		BLOCK_BOX=assetManager.loadTexture("gui/block_box.png");
		BLOCK_BOX_SELECTED=assetManager.loadTexture("gui/block_box_selected.png");
		NULL_TEXTURE=assetManager.loadTexture("gui/empty.png");
		GUI_BUTTON=assetManager.loadTexture("gui/button.png");
		GUI_START_MENU_BACKGROUND=assetManager.loadTexture("gui/menu_background.png");
		BLOCK_BOX.setMagFilter(MagFilter.Nearest);
		BLOCK_BOX_SELECTED.setMagFilter(MagFilter.Nearest);
		GUI_BUTTON.setMagFilter(MagFilter.Nearest);
		
		MATERIAL_LIGHTING=new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		MATERIAL_UNSHADED=new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		
		BLOCK_MATERIAL=new Material[Constant.BLOCK_TYPE_NUM];
		for(int Blockid=0;Blockid<Constant.BLOCK_TYPE_NUM;Blockid++) {
			// 设置受光材质
			BLOCK_MATERIAL[Blockid] = Assets.MATERIAL_LIGHTING.clone();
			// 设置纹理贴图
			BLOCK_MATERIAL[Blockid].setTexture("DiffuseMap", Assets.BLOCK_TEXTURE[Blockid]);
	        // 设置反光度
			BLOCK_MATERIAL[Blockid].setFloat("Shininess", 2.0f);
			if(BlockBackend.getBlockInstanceByID(Blockid).isTransparent()) {
				BLOCK_MATERIAL[Blockid].getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
			}    
		}
		
		initialized=true;
	}
}
