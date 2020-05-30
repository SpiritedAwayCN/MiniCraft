// package minicraft.frontend;

// import com.jme3.app.SimpleApplication;
// import com.jme3.app.StatsAppState;
// import com.jme3.app.state.ConstantVerifierState;

// import java.util.HashMap;
// import java.util.HashSet;

// import com.jme3.app.DebugKeysAppState;
// //import com.jme3.app.FlyCamAppState;
// //用了minicraft.frontend.FlyCamAppState
// import com.jme3.asset.plugins.FileLocator;
// import com.jme3.audio.AudioListenerState;
// import com.jme3.input.KeyInput;
// import com.jme3.input.MouseInput;
// import com.jme3.input.controls.ActionListener;
// import com.jme3.input.controls.KeyTrigger;
// import com.jme3.input.controls.MouseButtonTrigger;
// import com.jme3.light.AmbientLight;
// import com.jme3.light.DirectionalLight;
// import com.jme3.material.Material;
// import com.jme3.math.ColorRGBA;
// import com.jme3.math.FastMath;
// import com.jme3.math.Vector3f;
// import com.jme3.renderer.queue.RenderQueue.Bucket;
// import com.jme3.scene.Geometry;
// import com.jme3.scene.Mesh;
// import com.jme3.scene.Spatial;
// import com.jme3.scene.Spatial.CullHint;
// import com.jme3.scene.shape.Box;
// import com.jme3.system.AppSettings;
// import com.jme3.system.JmeContext.Type;
// import com.jme3.texture.Texture;
// import com.jme3.texture.Texture.MagFilter;
// import com.jme3.util.SkyFactory;

// import minicraft.backend.constants.Constant;
// import minicraft.backend.map.DimensionMap;
// import minicraft.backend.map.block.BlockBackend;
// import minicraft.backend.utils.BlockCoord;

// /**
//  * Minicraft主类
//  * @author StarSky/IcyChlorine
//  */
// public class TestApplication extends SimpleApplication {
	
// 	public static final String INPUT_MAPPING_MENU = "MYAPP_Menu";
// 	public static final String INPUT_BREAK_BLOCK = "MYAPP_Block_Break";
// 	public static final String INPUT_PLACE_BLOCK = "MYAPP_Blace_Block";
	
// 	DimensionMap overworld;
// 	GeometryBlock[] geoms;//不得已才这么写，待改进
	
	
// 	TestApplication(){
// 		super(new StatsAppState(),
// 				new minicraft.frontend.FlyCamAppState(),//注意这里
// 				new AudioListenerState(),
// 				new DebugKeysAppState(),
// 	            new ConstantVerifierState());
// 	}
// 	@Override
// 	public void initialize() {
// 		super.initialize();
// 		if (stateManager.getState(FlyCamAppState.class) != null) {
//             flyCam = new GamerCamera(cam, this);//我修改的地方
//             flyCam.setMoveSpeed(5f); // odd to set this here but it did it before
//             stateManager.getState(FlyCamAppState.class).setCamera( flyCam );
//         }
// 		//将esc的作用改写为释放/隐藏鼠标，便于用鼠标调整窗口大小
// 		if (inputManager!=null) {
// 			inputManager.deleteMapping(INPUT_MAPPING_EXIT);
// 			inputManager.addMapping(INPUT_MAPPING_MENU, new KeyTrigger(KeyInput.KEY_ESCAPE));
// 			inputManager.addListener(new ActionListener() {
// 				@Override
// 		        public void onAction(String name, boolean value, float tpf) {
// 	            //if (name.equals(INPUT_MAPPING_EXIT))
// 					//System.out.println("esc action triggered");
// 					if(value)
// 						flyCam.setEnabled(!flyCam.isEnabled());
// 				}
// 			}, INPUT_MAPPING_MENU);
			
// 			inputManager.addMapping(INPUT_BREAK_BLOCK,new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
// 			inputManager.addListener(new ActionListener() {
// 				@Override
// 		        public void onAction(String name, boolean value, float tpf) {
// 					if(value) {
// 						breakBlockTemp();
// 						//System.out.println("left button hit");
// 					}
// 				}
// 			}, INPUT_BREAK_BLOCK);
// 			inputManager.addMapping(INPUT_PLACE_BLOCK,new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
// 			inputManager.addListener(new ActionListener() {
// 				@Override
// 		        public void onAction(String name, boolean value, float tpf) {
// 					if(value)
// 						placeBlockTemp();
// 				}
// 			}, INPUT_PLACE_BLOCK);
// 		}
		
		
// 		//gui-swing test
// 		JMEDisplaySystem display=DisplaySystem.getDisplaySystem();
// 		displaywide=display.getWidth();
// 		displayheight=display.getHeight();
// 		JMEDesktop desktop=new JMEDesktop("controldesk");
// 		desktop.getJDesktop().setBackground(new Color(0,0,0,0));//完全透明
// 		desktop.setup(displaywide,displayheight,false,input);

// 	}
	
// 	/**
// 	 * 初始化APP
// 	 */
// 	@Override
// 	public void simpleInitApp() {
// 		System.out.println("TestApplication.simpleInitApp()");
		
// 		overworld=new DimensionMap("overworld");
// 		overworld.generateFromGenerator(true);
// 		//should be replaced to stats for player
// 		cam.setLocation(new Vector3f(0,5.5f,0));
		
// 		GeometryBlock.initialize(assetManager);
// 		geoms=new GeometryBlock[256*64*256];
		
// 		//BlockBackend block;
// 		new Thread(()->
// 		{
// 			HashSet<BlockBackend> blocksToAdd=overworld.refreshWholeUpdateBlockSet();
// 			Geometry geom;
// 			for(BlockBackend block:blocksToAdd) {
// 				if(block.getBlockid()==0)
// 					continue;
// 				geom=new GeometryBlock(block);
// 				rootNode.attachChild(geom);
// 				geoms[geom.hashCode()]=(GeometryBlock) geom;
// 				//只好手写hashSet
// 			}
// 			blocksToAdd.clear();
// 		}).run();
		
        
//         // 定向光
//         DirectionalLight sun = new DirectionalLight();
//         sun.setDirection(new Vector3f(-1, -2, -3));
        
//         // 环境光
//         AmbientLight ambient = new AmbientLight();

//         // 调整光照亮度
//         ColorRGBA lightColor = new ColorRGBA();
//         sun.setColor(lightColor.mult(0.8f));
//         ambient.setColor(lightColor.mult(0.2f));
        
//         // #3 将模型和光源添加到场景图中
//         rootNode.addLight(sun);
//         rootNode.addLight(ambient);
        
//         //设置天空颜色
//         viewPort.setBackgroundColor(new ColorRGBA(0.2f, 0.4f, 0.6f, 1));

// 	}

// 	/**
// 	 * 主循环
// 	 */
// 	@Override
// 	public void simpleUpdate(float deltaTime) {
// 		updateBlockVisibility();
// 	}
	
// 	void updateBlockVisibility() {
// 		//更新周围方块，使其显示
// 		HashSet<BlockBackend> blocksToUpdate=overworld.getUpdateBlockSet();
// 		//if(blocksToUpdate.size()>0)
// 			//System.out.println(blocksToUpdate.size());
// 		for(BlockBackend b:blocksToUpdate) {
// 			if(b.getShouldBeShown() == (geoms[b.hashCode()]!=null)) {
// 				continue;//显示状态相同，不用更新
// 			}
// 			//添加显示
// 			if(b.getShouldBeShown()) {
// 				GeometryBlock geom=new GeometryBlock(b);
// 				rootNode.attachChild(geom);
// 				geoms[b.hashCode()]=(GeometryBlock) geom;
// 			}else {//不显示
// 				//System.out.println("rootNode.detaching sth.");
// 				rootNode.detachChild(geoms[b.hashCode()]);
// 				geoms[b.hashCode()]=null;
				
// 			}
// 		}
// 		blocksToUpdate.clear();//将更新一笔勾销
// 	}
// 	/*
// 	 * @return 当flag=true,返回找到的最后一个空气方块；flag=false，返回找到的第一个实体方块
// 	 */
// 	BlockBackend findBlockByDir(Vector3f r0,Vector3f dir,boolean flagFindAir) {
// 		Vector3f r;
// 		BlockBackend block=null;
// 		for(float d=0;d<=7;d+=0.1f) {//d for distance
// 			r=r0.add(dir.mult(d));
// 			block=overworld.getBlockByVector3f(r);
			
// 			//跳过空气，以及跳过视线探出地图的情况
// 			if(block==null || block.getBlockid()==0) {
// 				continue;
// 			}
			
// 			//找到了实体方块
// 			if(flagFindAir) {
// 				//回滚到前一位位置，即要放方块的空气处
// 				r=r.subtract(dir.mult(0.1f));
// 				block=overworld.getBlockByVector3f(r);
// 			}
// 			//else ...
// 			return block;
// 		}
// 		return null;
// 	}
// 	public void breakBlockTemp() {
// 		//获得视线指向的方块
// 		BlockBackend block=findBlockByDir(cam.getLocation(),cam.getDirection(),false);
// 		if(block==null) return;//no block within reach
// 		BlockCoord r=block.getBlockCoord();
		
// 		block.destoryBlock();
// 		overworld.updateBlockSetTemp(r, true);
// 		block=overworld.getBlockByCoord(r);
// 		//block.setShouldBeShown(false);

// 	}
// 	public void placeBlockTemp() {
// 		//System.out.println("app.placeBlockTemp()");
// 		BlockBackend block=findBlockByDir(cam.getLocation(),cam.getDirection(),true);
// 		if(block==null) return;//no block within reach
// 		BlockCoord r=block.getBlockCoord();
		
// 		//放置方块
// 		block=BlockBackend.getBlockInstanceByID(3);//放泥土
// 		block.placeAt(r, overworld);
// 		overworld.updateBlockSetTemp(r, false);

// 	}

	

// 	public static void main(String[] args) {
// 		// 配置参数
// 		AppSettings settings = new AppSettings(true);
// 		settings.setTitle("MiniCraft");
// 		settings.setResolution(1024, 768);
// 		settings.setResizable(true);
		
// 		// 启动jME3程序
// 		TestApplication app = new TestApplication();
// 		app.setSettings(settings);// 应用参数
// 		app.setShowSettings(false);
// 		app.start();
// 	}

// }