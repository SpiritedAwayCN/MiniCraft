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
import com.jme3.cursors.plugins.JmeCursor;
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
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.Styles;

import minicraft.backend.constants.Constant;
import minicraft.backend.map.DimensionMap;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.utils.BlockCoord;
import minicraft.frontend.gui.BlockBox;
import minicraft.frontend.gui.BlockList;

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
            flyCam = new GamerCamera(cam, this);//我修改的地方
            flyCam.setMoveSpeed(5f); // odd to set this here but it did it before
            stateManager.getState(FlyCamAppState.class).setCamera( flyCam );
        }
		registerInputMapping();
	}
	private void registerInputMapping() {
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
		overworld.generateFromGenerator(false);
		//should be replaced to stats for player
		cam.setLocation(new Vector3f(0,5.5f,0));
		
		Assets.initialize(assetManager);
		//初始化场景及相关类
		initScene();
        
        //设置天空颜色
        viewPort.setBackgroundColor(new ColorRGBA(0.2f, 0.4f, 0.6f, 1));

        initGUI();
	}
	private void initScene() {
		geoms=new GeometryBlock[256*64*256];
		
		//BlockBackend block;
		new Thread(()->
		{
			HashSet<BlockBackend> blocksToAdd=overworld.refreshWholeUpdateBlockSet();
			Geometry geom;
			for(BlockBackend block:blocksToAdd) {
				if(block.getBlockid()==0)
					continue;
				geom=new GeometryBlock(block);
				//透明方块特判
				if(block.isTransparent()) {
					geom.setQueueBucket(Bucket.Transparent);
				}
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
        
        /* 产生阴影 */
        final int SHADOWMAP_SIZE=1024;

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 1);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
        rootNode.setShadowMode(ShadowMode.CastAndReceive);
	}
	private void initGUI() {
		//设置鼠标
		JmeCursor cur = (JmeCursor) assetManager.loadAsset("gui/cursor.cur");
		inputManager.setMouseCursor(cur);
		
		
		// 初始化Lemur GUI
        GuiGlobals.initialize(this);
        Styles styles = GuiGlobals.getInstance().getStyles();
        styles.getSelector( Slider.THUMB_ID, "glass" ).set( "text", "[]", false );
        styles.getSelector( Panel.ELEMENT_ID, "glass" ).set( "background",
                                new QuadBackgroundComponent(new ColorRGBA(0, 0.25f, 0.25f, 0.5f)) );
        styles.getSelector( Button.ELEMENT_ID, "glass" ).set( "background",
                                new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 0.5f, 0.5f)) );
        styles.getSelector( Label.ELEMENT_ID, "glass" ).set( "background",
                new QuadBackgroundComponent(new ColorRGBA(0, 0.5f, 0.5f, 0.5f)) );


        
        // 创建一个Container作为窗口中其他GUI元素的容器
    	Container myWindow = new Container("glass");
    	guiNode.attachChild(myWindow);

    	// 设置窗口在屏幕上的坐标
    	// 注意：Lemur的GUI元素是以控件左上角为原点，向右、向下生成的。
    	// 然而，作为一个Spatial，它在GuiNode中的坐标原点依然是屏幕的左下角。
    	myWindow.setLocalTranslation(300, 300, 0);

    	// 添加一个Label控件
    	myWindow.addChild(new Label("Hello, World.","glass"));
    	
    	// 添加一个Button控件
    	Button clickMe = myWindow.addChild(new Button("Click Me","glass"));
    	clickMe.addClickCommands(new Command<Button>() {
    		@Override
    		public void execute(Button source) {
    			System.out.println("The world is yours.");
    		}
    	});
		//Picture pic = new Picture("picture");

        // 设置图片
        //pic.setImage(assetManager, "gui/block_box.png", true);

        
        //pic.setWidth(80);
        //pic.setHeight(80);

        // 将图片后移一个单位，避免遮住状态界面。
        //pic.setLocalTranslation(cam.getWidth()/2-80/2, 0, -1);
        blockList=new BlockList(this);    
        guiNode.attachChild(blockList);
        
	}
	BlockList blockList;
	
	/*
	 * 在窗口大小变化时，gui也需随之变化
	 */
	@Override
	public void reshape(int w,int h) {
		super.reshape(w, h);
		blockList.reshape(w,h);
	}


	/**
	 * 主循环
	 */
	@Override
	public void simpleUpdate(float deltaTime) {
		updateBlockVisibility();
		
		
	}
	
	void updateBlockVisibility() {
		//更新周围方块，使其显示
		HashSet<BlockBackend> blocksToUpdate=overworld.getUpdateBlockSet();
		// if(blocksToUpdate.size()>0)
		// 	System.out.println(blocksToUpdate.size());
		for(BlockBackend b:blocksToUpdate) {
			// System.out.println(b.getBlockCoord() + " " + b.getShouldBeShown());
			if(b.getShouldBeShown() == (geoms[b.hashCode()]!=null)) {
				continue;//显示状态相同，不用更新
			}
			//添加显示
			if(b.getShouldBeShown()) {
				GeometryBlock geom=new GeometryBlock(b);
				//透明方块特判
				if(b.isTransparent()) {
					geom.setQueueBucket(Bucket.Transparent);
				}
				rootNode.attachChild(geom);
				geoms[b.hashCode()]=(GeometryBlock) geom;
				
			}else {//不显示
				//System.out.println("rootNode.detaching sth.");
				rootNode.detachChild(geoms[b.hashCode()]);
				geoms[b.hashCode()]=null;
				
			}
		}
		blocksToUpdate.clear();//将更新一笔勾销
	}
	/*
	 * @return 当flag=true,返回找到的最后一个空气方块；flag=false，返回找到的第一个实体方块
	 */
	BlockBackend findBlockByDir(Vector3f r0,Vector3f dir,boolean flagFindAir) {
		Vector3f r;
		BlockBackend block=null;
		for(float d=0;d<=7;d+=0.1f) {//d for distance
			r=r0.add(dir.mult(d));
			block=overworld.getBlockByVector3f(r);
			
			//跳过空气，以及跳过视线探出地图的情况
			if(block==null || block.getBlockid()==0) {
				continue;
			}
			
			//找到了实体方块
			if(flagFindAir) {
				//回滚到前一位位置，即要放方块的空气处
				r=r.subtract(dir.mult(0.1f));
				block=overworld.getBlockByVector3f(r);
			}
			//else ...
			return block;
		}
		return null;
	}
	public void breakBlockTemp() {
		//获得视线指向的方块
		BlockBackend block=findBlockByDir(cam.getLocation(),cam.getDirection(),false);
		if(block==null) return;//no block within reach
		BlockCoord r=block.getBlockCoord();
		
		block.destoryBlock();
		overworld.updateBlockSetTemp(r, true);
		block=overworld.getBlockByCoord(r);
		//block.setShouldBeShown(false);

	}
	public void placeBlockTemp() {
		//System.out.println("app.placeBlockTemp()");
		BlockBackend block=findBlockByDir(cam.getLocation(),cam.getDirection(),true);
		if(block==null) return;//no block within reach
		BlockCoord r=block.getBlockCoord();
		
		//放置方块
		int blockid=blockList.getSelectedBlockid();
		if(blockid==-1) return;
		block=BlockBackend.getBlockInstanceByID(blockList.getSelectedBlockid());	block.placeAt(r, overworld);
		overworld.updateBlockSetTemp(r, false);

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