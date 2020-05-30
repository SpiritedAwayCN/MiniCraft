package minicraft.frontend;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;

import minicraft.backend.constants.Constant;

public class Assets {
	public static Texture[] BLOCK_TEXTURE;
	public static Texture[] BLOCK_IMAGE;
	public static Texture BLOCK_BOX,BLOCK_BOX_SELECTED;
	public static Texture NULL_TEXTURE;
	
	public static Material MATERIAL_LIGHTING;
	public static Material MATERIAL_UNSHADED;
	
	public static boolean initialized=false;
	
	static void initialize(AssetManager assetManager) {
		assetManager.registerLocator("assets", FileLocator.class);
		
		final String[] blockTexFilepath= {
				null,//air
				"texture/stone.bmp",
				"texture/grass.bmp",
				"texture/dirt.bmp",
				"texture/bedrock.bmp",
				null//glass
		};
		final String[] blockImageFilepath= {
				null,//air
				"gui/block_stone.png",
				"gui/block_grass.png",
				"gui/block_dirt.png",
				"gui/block_bedrock.png",
				null//glass
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
		BLOCK_BOX.setMagFilter(MagFilter.Nearest);
		BLOCK_BOX_SELECTED.setMagFilter(MagFilter.Nearest);
		
		MATERIAL_LIGHTING=new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		MATERIAL_UNSHADED=new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		
		
		initialized=true;
	}
}
