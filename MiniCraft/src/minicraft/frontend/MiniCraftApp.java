package minicraft.frontend;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ConstantVerifierState;

import java.util.HashMap;
import java.util.HashSet;

import com.jme3.app.DebugKeysAppState;
//import com.jme3.app.FlyCamAppState;
//用了minicraft.frontend.FlyCamAppState
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioListenerState;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.DimensionMap;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.utils.BlockCoordinate;

/**
 * Minicraft主类
 * @author StarSky/IcyChlorine
 */
public class MiniCraftApp extends SimpleApplication {
	
	public static final String INPUT_MAPPING_MENU = "MYAPP_Menu";
	public static final String INPUT_BREAK_BLOCK = "MYAPP_Block_Break";
	public static final String INPUT_PLACE_BLOCK = "MYAPP_Blace_Block";
	
	DimensionMap overworld;
	GeometryBlock[] geoms;//不得已才这么写，待改进
	
	
	MiniCraftApp(){
		super(new StatsAppState(),
				new minicraft.frontend.FlyCamAppState(),//注意这里
				new AudioListenerState(),
				new DebugKeysAppState(),
	            new ConstantVerifierState());
	}
	@Override
	public void initialize() {
		super.initialize();
		if (stateManager.getState(FlyCamAppState.class) != null) {
            flyCam = new GamerCamera(cam);//我修改的地方
            flyCam.setMoveSpeed(5f); // odd to set this here but it did it before
            stateManager.getState(FlyCamAppState.class).setCamera( flyCam );
        }
		//将esc的作用改写为释放/隐藏鼠标，便于用鼠标调整窗口大小
		if (inputManager!=null) {
			inputManager.deleteMapping(INPUT_MAPPING_EXIT);
			inputManager.addMapping(INPUT_MAPPING_MENU, new KeyTrigger(KeyInput.KEY_ESCAPE));
			inputManager.addListener(new ActionListener() {
				@Override
		        public void onAction(String name, boolean value, float tpf) {
	            //if (name.equals(INPUT_MAPPING_EXIT))
					//System.out.println("esc action triggered");
					if(value)
						flyCam.setEnabled(!flyCam.isEnabled());
				}
			}, INPUT_MAPPING_MENU);
			
			inputManager.addMapping(INPUT_BREAK_BLOCK,new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
			inputManager.addListener(new ActionListener() {
				@Override
		        public void onAction(String name, boolean value, float tpf) {
					if(value) {
						breakBlockTemp();
						//System.out.println("left button hit");
					}
				}
			}, INPUT_BREAK_BLOCK);
			inputManager.addMapping(INPUT_PLACE_BLOCK,new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
			inputManager.addListener(new ActionListener() {
				@Override
		        public void onAction(String name, boolean value, float tpf) {
					if(value)
						placeBlockTemp();
				}
			}, INPUT_PLACE_BLOCK);
		}
	}
	
	/**
	 * 初始化APP
	 */
	@Override
	public void simpleInitApp() {
		System.out.println("MiniCraftApp.simpleInitApp()");
		
		overworld=new DimensionMap("overworld");
		overworld.generateFromGenerator(true);
		
		GeometryBlock.initialize(assetManager);
		geoms=new GeometryBlock[256*64*256];
		
		//BlockBackend block;
		new Thread(()->
		{
			HashSet<BlockBackend> blocksToAdd=overworld.initializeWholeUpdateBlockSetTemp();
			Geometry geom;
			for(BlockBackend block:blocksToAdd) {
				if(block.getBlockid()==0)
					continue;
				geom=new GeometryBlock(overworld.getBlockByCoordinate(block.getBlockCoordinate()));
				rootNode.attachChild(geom);
				geoms[geom.hashCode()]=(GeometryBlock) geom;
				//只好手写hashSet
			}
			blocksToAdd.clear();
		}).run();
		
        
        // 定向光
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-1, -2, -3));
        
        // 环境光
        AmbientLight ambient = new AmbientLight();

        // 调整光照亮度
        ColorRGBA lightColor = new ColorRGBA();
        sun.setColor(lightColor.mult(0.8f));
        ambient.setColor(lightColor.mult(0.2f));
        
        // #3 将模型和光源添加到场景图中
        rootNode.addLight(sun);
        rootNode.addLight(ambient);
        
	}
	GeometryBlock getGeomByBlock(BlockBackend block) 
			throws NullPointerException{
		if(block==null) {
			throw new NullPointerException();
		}
		if(geoms[block.hashCode()]==null) {
			geoms[block.hashCode()]=new GeometryBlock(block);
		}
		return geoms[block.hashCode()];
	}
	/**
	 * 主循环
	 */
	@Override
	public void simpleUpdate(float deltaTime) {
		
		
	}
	public void breakBlockTemp() {
		Vector3f vFwd=cam.getDirection();
		Vector3f vPos=cam.getLocation();
		Vector3f v;
		for(float d=0;d<=5;d+=0.1f) {
			v=vPos.add(vFwd.mult(d));
			BlockCoordinate r=new BlockCoordinate(
					(int)Math.round(v.x),(int)Math.round(v.y),(int)Math.round(v.z));
			//find the block to be break
			BlockBackend block=overworld.getBlockByCoordinate(r);
			if(block.getBlockid()==0) {
				continue;
			}
			
			//更新周围方块，使其显示
			Geometry geom;
			HashSet<BlockBackend> blocksToUpdate=overworld.updateAdjacentBlockTemp(r);
			//System.out.println(blocksToUpdate.size());
			for(BlockBackend b:blocksToUpdate) {
				if(b.getBlockid()==0)//空气不算
					continue;
				geom=new GeometryBlock(b);
				if(geoms[geom.hashCode()]==null) {
					rootNode.attachChild(geom);
					geoms[geom.hashCode()]=(GeometryBlock) geom;
				}
			}
			blocksToUpdate.clear();//应后端要求，清空之
			
			//破坏方块本身
			//因为后端的技术原因...先把这个放在后面
			if(geoms[block.hashCode()]==null)
				throw new RuntimeException();
			rootNode.detachChild(getGeomByBlock(block));
			geoms[block.hashCode()]=null;
			block.destoryBlock();
			block=BlockBackend.getBlockInstanceByID(0);
			block.placeAt(r, overworld);
			
			//一次只破坏一个方块
			break;
		}
	}
	public void placeBlockTemp() {
		//System.out.println("app.placeBlockTemp()");
		Vector3f vFwd=cam.getDirection();
		Vector3f vPos=cam.getLocation();
		Vector3f v;
		for(float d=0;d<=5;d+=0.1f) {
			v=vPos.add(vFwd.mult(d));
			BlockCoordinate r=new BlockCoordinate(
					(int)Math.round(v.x),(int)Math.round(v.y),(int)Math.round(v.z));
			//find the block to be break
			BlockBackend block=overworld.getBlockByCoordinate(r);
			if(block.getBlockid()==0) {
				continue;
			}
			//else: block.id!=air
			//回滚到前一位位置，即要放方块的空气处
			v=v.subtract(vFwd.mult(0.1f));
			r=new BlockCoordinate(
					(int)Math.round(v.x),(int)Math.round(v.y),(int)Math.round(v.z));
			block=overworld.getBlockByCoordinate(r);
			
			//放置方块
			block=BlockBackend.getBlockInstanceByID(3);//放泥土
			block.placeAt(r, overworld);
			rootNode.attachChild(getGeomByBlock(block));
			
			//更新周围方块的显示状态
			Geometry geom;
			HashSet<BlockBackend> blocksToUpdate=overworld.updateAdjacentBlockTemp(r);
			//System.out.println(blocksToUpdate.size());
			for(BlockBackend b:blocksToUpdate) {
				if(b.getBlockid()==0)
					continue;
				if(!overworld.isBlockAdjacentToAir(b.getBlockCoordinate())){
					geom=geoms[b.hashCode()];
					rootNode.detachChild(geom);
					geoms[geom.hashCode()]=null;
				}
				
			}
			blocksToUpdate.clear();//应后端要求，清空之
			
			//一次只放置一个方块
			break;
		}
	}

	public static void main(String[] args) {
		// 配置参数
		AppSettings settings = new AppSettings(true);
		settings.setTitle("MiniCraft");
		settings.setResolution(1024, 768);
		settings.setResizable(true);
		
		// 启动jME3程序
		MiniCraftApp app = new MiniCraftApp();
		app.setSettings(settings);// 应用参数
		app.setShowSettings(false);
		app.start();
	}

}