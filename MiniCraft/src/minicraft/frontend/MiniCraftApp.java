package minicraft.frontend;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ConstantVerifierState;

import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JOptionPane;

import com.jme3.app.DebugKeysAppState;
//import com.jme3.app.FlyCamAppState;
//用了minicraft.frontend.FlyCamAppState
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioListenerState;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioData.DataType;
import com.jme3.bullet.BulletAppState;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
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

import minicraft.backend.constants.Constant;
import minicraft.backend.map.DimensionMap;
import minicraft.backend.map.block.BlockBackend;
import minicraft.backend.utils.BlockCoord;
import minicraft.frontend.control.GameOption;
import minicraft.frontend.gui.Background;
import minicraft.frontend.gui.BlockBox;
import minicraft.frontend.gui.BlockList;
import minicraft.frontend.gui.Button;
import minicraft.frontend.gui.IngameMenu;
import minicraft.frontend.gui.OptionsPanel;
import minicraft.frontend.gui.Panel;
import minicraft.frontend.gui.StartMenu;
import minicraft.frontend.gui.WorldSelectionDialog;

/**
 * Minicraft主类
 * 
 * @author StarSky/IcyChlorine
 */
public class MiniCraftApp extends SimpleApplication {

	public static final String INPUT_MAPPING_MENU = "MYAPP_Menu";
	public static final String INPUT_BREAK_BLOCK = "MYAPP_Block_Break";
	public static final String INPUT_PLACE_BLOCK = "MYAPP_Place_Block";
	public static final String INPUT_TAB_SWITCH = "MYAPP_TAB_SWITCH";

	public static final String START_MENU = "START_MENU";
	public static final String INGAME = "INGAME";
	public static final String INGAME_MENU = "INGAME_MENU";
	public static final String ABOUT_PANEL = "ABOUT_PANEL";
	public static final String OPTIONS_PANEL = "OPTIONS_PANEL";

	private Panel startMenu, ingameMenu, optionsPanel, aboutPanel;
	BlockList blockList;
	// 游戏进入，显示开始菜单
	private String appStatus = START_MENU;

	private boolean renderShadow, renderTransparentLeaves;

	DimensionMap overworld;
	// GeometryBlock[] geoms;//不得已才这么写，待改进

	MiniCraftApp() {
		super(new StatsAppState(), new minicraft.frontend.FlyCamAppState(), // 注意这里
				new AudioListenerState(), new DebugKeysAppState(), new ConstantVerifierState());
	}

	@Override
	public void initialize() {
		super.initialize();
		if (stateManager.getState(FlyCamAppState.class) != null) {
			flyCam = new GamerCamera(cam, this);// 我修改的地方
			flyCam.setMoveSpeed(5f); // odd to set this here but it did it before
			stateManager.getState(FlyCamAppState.class).setCamera(flyCam);
		}
		flyCam.setEnabled(false);// 初始为不使用——开始菜单。
		registerInputMapping();
	}

	private void registerInputMapping() {
		// 将esc的作用改写为释放/隐藏鼠标，便于用鼠标调整窗口大小
		if (inputManager != null) {
			inputManager.deleteMapping(INPUT_MAPPING_EXIT);
			inputManager.addMapping(INPUT_MAPPING_MENU, new KeyTrigger(option.getKeyMappingMenu()));
			inputManager.addListener(new ActionListener() {
				@Override
				public void onAction(String name, boolean value, float tpf) {
					if (value == false)
						return;

					if (appStatus.equals(INGAME))
						switchAppStatus(INGAME_MENU);
					else if (appStatus.equals(INGAME_MENU))
						switchAppStatus(INGAME);
				}
			}, INPUT_MAPPING_MENU);
			inputManager.addMapping(INPUT_TAB_SWITCH, new KeyTrigger(option.getKeyMappingSwitch()));
			inputManager.addListener(new ActionListener() {
				@Override
				public void onAction(String name, boolean value, float tpf) {
					if (value == false)
						return;
					if(!appStatus.equals(INGAME)) 
						return;
					overworld.getPlayer().setLowGravelty(
							!overworld.getPlayer().getLowGravelty());
					
				}
			}, INPUT_TAB_SWITCH);

			inputManager.addMapping(INPUT_BREAK_BLOCK, new MouseButtonTrigger(option.getMouseMappingBreak()));
			inputManager.addListener(new ActionListener() {
				@Override
				public void onAction(String name, boolean value, float tpf) {
					if (value) {
						breakBlock();
						// System.out.println("left button hit");
					}
				}
			}, INPUT_BREAK_BLOCK);
			inputManager.addMapping(INPUT_PLACE_BLOCK, new MouseButtonTrigger(option.getMouseMappingPlace()));
			inputManager.addListener(new ActionListener() {
				@Override
				public void onAction(String name, boolean value, float tpf) {
					if (value)
						placeBlock();
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
		// 加载材质
		Assets.initialize(assetManager);

		initGUI();

		initAudio();
		// 设置天空颜色
		viewPort.setBackgroundColor(new ColorRGBA(0.66f, 0.95f, 1.0f, 1));

	}

	private AudioNode audioNature, audioInGame;
	public AudioNode[] audioSounds;

	private void initAudio(){
		audioNature = new AudioNode(assetManager, "./audio/music/" + Constant.titleMusic, DataType.Stream);
		audioNature.setLooping(true); // 循环播放
		audioNature.setPositional(false);
		audioNature.setVolume(3);// 音量
		// 将音源添加到场景中
		// rootNode.attachChild(audioNature);
		audioInGame = new AudioNode(assetManager, "./audio/music/" + Constant.inGameStrinig, DataType.Stream);
		audioInGame.setLooping(true); // 循环播放
		audioInGame.setPositional(false);
		audioInGame.setVolume(3);// 音量

		audioSounds = new AudioNode[6];

		audioSounds[0] = new AudioNode(assetManager, "./audio/sounds/glass1.ogg", DataType.Buffer);
		audioSounds[1] = new AudioNode(assetManager, "./audio/sounds/grass1.ogg", DataType.Buffer);
		audioSounds[2] = new AudioNode(assetManager, "./audio/sounds/sand1.ogg", DataType.Buffer);
		audioSounds[3] = new AudioNode(assetManager, "./audio/sounds/stone1.ogg", DataType.Buffer);
		audioSounds[4] = new AudioNode(assetManager, "./audio/sounds/wood1.ogg", DataType.Buffer);
		audioSounds[5] = new AudioNode(assetManager, "./audio/sounds/slime.ogg", DataType.Buffer);
		audioNature.play();
	}
	
	private boolean firstInit=true;
	private void initScene() {
		new Thread(() -> {
			overworld.refreshWholeUpdateBlockSet();
		}).run();

		if (!firstInit)
			return;

		// 定向光
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-1, -2, -3));

		// 环境光
		AmbientLight ambient = new AmbientLight();

		// 调整光照亮度
		ColorRGBA lightColor = new ColorRGBA();
		sun.setColor(lightColor.mult(0.8f));
		ambient.setColor(lightColor.mult(0.2f));

		// 将模型和光源添加到场景图中
		rootNode.addLight(sun);
		rootNode.addLight(ambient);

		// 产生阴影 
		final int SHADOWMAP_SIZE = 1024;

		DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 1);
		dlsf.setLight(sun);
		dlsf.setEnabled(true);
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		fpp.addFilter(dlsf);
		viewPort.addProcessor(fpp);
		
		// 阴影默认关
		// rootNode.setShadowMode(ShadowMode.CastAndReceive);
		firstInit=false;

	}
	
	public void switchRenderShadow() {
		renderShadow = !renderShadow;
		if (renderShadow) {
			rootNode.setShadowMode(ShadowMode.CastAndReceive);
		} else {
			rootNode.setShadowMode(ShadowMode.Off);
		}
	}

	public void switchRenderTransparentLeaves() {
		renderTransparentLeaves = !renderTransparentLeaves;
		if (renderTransparentLeaves) {
			minicraft.backend.map.block.OakLeavesBlock.setTransparent(false);
			Assets.BLOCK_MATERIAL[Constant.BLOCK_OAK_LEAVES].getAdditionalRenderState().setBlendMode(BlendMode.Off);
		} else {
			minicraft.backend.map.block.OakLeavesBlock.setTransparent(true);
			Assets.BLOCK_MATERIAL[Constant.BLOCK_OAK_LEAVES].getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		}
		rootNode.detachAllChildren();
		if (appStatus.equals(INGAME) || appStatus.equals(INGAME_MENU))
			initScene();
	}

	private void initGUI() {
		// 设置鼠标
		JmeCursor cur = (JmeCursor) assetManager.loadAsset("gui/cursor.cur");
		inputManager.setMouseCursor(cur);

		// private Panel startMenu,ingameMenu,optionsPanel,aboutPanel;
		startMenu=new StartMenu(this);
		optionsPanel=new OptionsPanel(this);
		ingameMenu=new IngameMenu(this);
		blockList = new BlockList(this);
		
		//startMenu.setEnabled(true);
		optionsPanel.setEnabled(false);
		ingameMenu.setEnabled(false);
		blockList.setEnabled(false);
		
		guiNode.attachChild(startMenu);	
		guiNode.attachChild(optionsPanel);
		guiNode.attachChild(ingameMenu);
		guiNode.attachChild(blockList);

	}

	public void switchAppStatus(String appStatusNew) {
		if (appStatus.equals(appStatusNew))
			return;
		if(appStatusNew.equals(INGAME) && !(appStatus.equals(START_MENU) || appStatus.equals(INGAME_MENU)))
			return;

		// 关掉些什么
		if (appStatus.equals(START_MENU)) {
			startMenu.setEnabled(false);
		} else if (appStatus.equals(OPTIONS_PANEL)) {
			optionsPanel.setEnabled(false);
		} else if (appStatus.equals(INGAME_MENU)) {
			ingameMenu.setEnabled(false);

			blockList.setEnabled(false);
			flyCam.setEnabled(false);
			if (appStatusNew.equals(START_MENU)) {// 回到主菜单，把所以地图相关移除
				rootNode.detachAllChildren();
			}
		}
		// System.out.println(appStatus + ", " + appStatusNew);
		// ------------>打开些什么
		if (appStatusNew.equals(START_MENU)) {
			audioInGame.stop();
			startMenu.setEnabled(true);
			audioNature.play();
		}else if (appStatusNew.equals(INGAME) && appStatus.equals(START_MENU)) {//从开始菜单来
			Object[] rst=WorldSelectionDialog.getWorldParam();
			
			if(rst.length==0) {//选项被取消
				startMenu.setEnabled(true);
				appStatusNew=START_MENU;
			}else {
				overworld =new DimensionMap((String)rst[0], this);
				if(rst.length==2)//new world
				{
					overworld.generateFromGenerator((Boolean)rst[1]);
				}else {//read from file
					try {
						overworld.generateFromSave((String)rst[0]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				cam.setLocation(overworld.getPlayer().getCoordinate().add(playerEyeBias));
				audioNature.stop();
				initScene();
				audioInGame.play();
				blockList.setEnabled(true);
				flyCam.setEnabled(true);//隐藏指针
			}
		}else if(appStatusNew.equals(INGAME) && appStatus.equals(INGAME_MENU)){ //游戏中来
			audioInGame.play();
			blockList.setEnabled(true);
			flyCam.setEnabled(true);//隐藏指针
		}else if(appStatusNew.equals(OPTIONS_PANEL)) {
			optionsPanel.setEnabled(true);
		}else if(appStatusNew.equals(INGAME_MENU)) {
			overworld.saveMap();
			audioInGame.pause();
			flyCam.setEnabled(false);//释放鼠标
			ingameMenu.setEnabled(true);
		}
		appStatus=appStatusNew;
	}
	
	/*
	 * 在窗口大小变化时，gui也需随之变化
	 */
	@Override
	public void reshape(int w,int h) {
		super.reshape(w, h);
		if(appStatus.equals(START_MENU) && startMenu!=null) {
			startMenu.reshape(w,h);
		}
		if(appStatus.equals(INGAME) && blockList!=null) {
			blockList.reshape(w,h);
		}
		if(appStatus.equals(INGAME_MENU) && ingameMenu!=null) {
			ingameMenu.reshape(w,h);
		}
		if(appStatus.equals(OPTIONS_PANEL) && optionsPanel!=null) {
			optionsPanel.reshape(w,h);
		}
		if(appStatus.equals(ABOUT_PANEL) && aboutPanel!=null) {
			aboutPanel.reshape(w,h);
		}
	}

	public static final Vector3f playerEyeBias = new Vector3f(0, (float)2.0, 0);

	public static final float timePerTick=0.05f;
	/**
	 * 主循环
	 */
	@Override
	public void simpleUpdate(float deltaTime) {
		if(!appStatus.equals(INGAME)) return;

		try {
		if(overworld.getPlayer().updateFalling(deltaTime))
			cam.setLocation(overworld.getPlayer().getCoordinate().add(playerEyeBias));
		}catch(NullPointerException e) {}
		
		
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
	public void breakBlock() {
		if(!appStatus.equals(INGAME)) return;
		// System.out.println("app.breakBlockTemp()");
		//获得视线指向的方块
		BlockBackend block=findBlockByDir(cam.getLocation(),cam.getDirection(),false);
		if(block==null) return;//no block within reach
		BlockCoord r=block.getBlockCoord();
		
		block.destoryBlock();
		overworld.updateBlockSetTemp(r, true);
		block=overworld.getBlockByCoord(r);
		//block.setShouldBeShown(false);

	}
	public void placeBlock() {
		if(!appStatus.equals(INGAME)) return;
		//System.out.println("app.placeBlockTemp()");
		BlockBackend block=findBlockByDir(cam.getLocation(),cam.getDirection(),true);
		if(block==null) return;//no block within reach
		BlockCoord r=block.getBlockCoord();
		
		//放置方块
		int blockid=blockList.getSelectedBlockid();
		if(blockid==-1) return;
		block=BlockBackend.getBlockInstanceByID(blockList.getSelectedBlockid());	
		block.placeAt(r, overworld);
		overworld.updateBlockSetTemp(r, false);
	}

	public GameOption option = GameOption.loadInstanceFromFile();

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