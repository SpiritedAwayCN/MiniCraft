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
import minicraft.frontend.gui.Background;
import minicraft.frontend.gui.BlockBox;
import minicraft.frontend.gui.BlockList;
import minicraft.frontend.gui.Button;
import minicraft.frontend.gui.Panel;

/**
 * Minicraft主类
 * 
 * @author StarSky/IcyChlorine
 */
public class MiniCraftApp extends SimpleApplication {

	public static final String INPUT_MAPPING_MENU = "MYAPP_Menu";
	public static final String INPUT_BREAK_BLOCK = "MYAPP_Block_Break";
	public static final String INPUT_PLACE_BLOCK = "MYAPP_Blace_Block";

	private static final String START_MENU = "START_MENU";
	private static final String INGAME = "INGAME";
	private static final String INGAME_MENU = "INGAME_MENU";
	private static final String ABOUT_PANEL = "ABOUT_PANEL";
	private static final String OPTIONS_PANEL = "OPTIONS_PANEL";

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
			inputManager.addMapping(INPUT_MAPPING_MENU, new KeyTrigger(KeyInput.KEY_ESCAPE));
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

			inputManager.addMapping(INPUT_BREAK_BLOCK, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
			inputManager.addListener(new ActionListener() {
				@Override
				public void onAction(String name, boolean value, float tpf) {
					if (value) {
						breakBlockTemp();
						// System.out.println("left button hit");
					}
				}
			}, INPUT_BREAK_BLOCK);
			inputManager.addMapping(INPUT_PLACE_BLOCK, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
			inputManager.addListener(new ActionListener() {
				@Override
				public void onAction(String name, boolean value, float tpf) {
					if (value)
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
		// 加载材质
		Assets.initialize(assetManager);

		initGUI();

		// 设置天空颜色
		viewPort.setBackgroundColor(new ColorRGBA(0.2f, 0.4f, 0.6f, 1));

	}

	private void initScene(boolean firstInit) {
		// geoms=new GeometryBlock[256*64*256];

		// BlockBackend block;
		new Thread(() -> {
			overworld.refreshWholeUpdateBlockSet();
			// HashSet<BlockBackend> blocksToAdd=overworld.refreshWholeUpdateBlockSet();
			// Geometry geom;
			// for(BlockBackend block:blocksToAdd) {
			// if(block.getBlockid()==0)
			// continue;
			// geom=new GeometryBlock(block);
			// rootNode.attachChild(geom);
			// geoms[geom.hashCode()]=(GeometryBlock) geom;
			// //只好手写hashSet
			// }
			// blocksToAdd.clear();
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

		// #3 将模型和光源添加到场景图中
		rootNode.addLight(sun);
		rootNode.addLight(ambient);

		/* 产生阴影 */
		final int SHADOWMAP_SIZE = 1024;

		DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 1);
		dlsf.setLight(sun);
		dlsf.setEnabled(true);
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		fpp.addFilter(dlsf);
		viewPort.addProcessor(fpp);

		// rootNode.setShadowMode(ShadowMode.CastAndReceive);

	}

	private void switchRenderShadow() {
		renderShadow = !renderShadow;
		if (renderShadow) {
			rootNode.setShadowMode(ShadowMode.CastAndReceive);
		} else {
			rootNode.setShadowMode(ShadowMode.Off);
		}
	}

	private void switchRenderTransparentLeaves() {
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
			initScene(false);
	}

	private void initGUI() {
		// 设置鼠标
		JmeCursor cur = (JmeCursor) assetManager.loadAsset("gui/cursor.cur");
		inputManager.setMouseCursor(cur);

		// private Panel startMenu,ingameMenu,optionsPanel,aboutPanel;
		startMenu = new Panel(this);
		startMenu.addComponent(new Button("Quit", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				stop();
			}
		}));
		startMenu.addComponent(new Button("About", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.About hit");
				JOptionPane.showMessageDialog(null,
						"作者：\n石淳安/Spirited_Away\n李辰剑/IcyChlorine\n2020-5,copyright reserved.");
			}
		}));
		startMenu.addComponent(new Button("Options", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.Options hit");
				switchAppStatus(OPTIONS_PANEL);
			}
		}));
		Button gameStart = new Button("Start Game");
		gameStart.addActionListener(new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.GameStart hit");
				gameStart.setText("Loading...");// 无法正常显示，待修
				switchAppStatus(INGAME);
				gameStart.setText("Start Game");
			}
		});
		startMenu.addComponent(gameStart);
		Background bg1 = new Background(Assets.GUI_START_MENU_BACKGROUND, 1920, 1080);
		bg1.reshape(cam.getWidth(), cam.getHeight());

		startMenu.setBackground(bg1);
		guiNode.attachChild(startMenu);

		optionsPanel = new Panel(this);
		optionsPanel.setEnabled(false);
		optionsPanel.addComponent(new Button("Back", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("ingameMenu.About hit");
				switchAppStatus(START_MENU);
			}
		}));
		optionsPanel.addComponent(new Button("Switch Render Shadow", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.Options hit");
				switchRenderShadow();
			}
		}));
		optionsPanel.addComponent(new Button("Switch Render Transparent Leaves", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.Options hit");
				switchRenderTransparentLeaves();
			}
		}));
		optionsPanel.setBackground(bg1);
		guiNode.attachChild(optionsPanel);

		ingameMenu = new Panel(this);
		ingameMenu.setEnabled(false);
		ingameMenu.addComponent(new Button("Start Menu", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("ingameMenu.About hit");
				switchAppStatus(START_MENU);
			}
		}));
		ingameMenu.addComponent(new Button("Switch Render Shadow", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.Options hit");
				switchRenderShadow();
			}
		}));
		ingameMenu.addComponent(new Button("Switch Render Transparent Leaves", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.Options hit");
				switchRenderTransparentLeaves();
			}
		}));
		Background bg2 = new Background(new ColorRGBA(1, 1, 1, 0.35f), cam.getWidth(), cam.getHeight());
		ingameMenu.setBackground(bg2);
		guiNode.attachChild(ingameMenu);

		// 将图片后移一个单位，避免遮住状态界面。
		blockList = new BlockList(this);
		blockList.setEnabled(false);

		guiNode.attachChild(blockList);

	}

	private void switchAppStatus(String appStatusNew) {
		if (appStatus.equals(appStatusNew))
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
		// ------------>打开些什么
		if (appStatusNew.equals(START_MENU)) {
			startMenu.setEnabled(true);
		}
		if (appStatusNew.equals(INGAME)) {
			if (appStatus.equals(START_MENU)) {// 从开始菜单来
				// 世界第一次
				if (overworld == null) {
					overworld = new DimensionMap("overworld", this);
					// overworld.generateFromGenerator(false);
					try {
						overworld.generateFromSave("overworld.json");
					} catch (Exception e) {
						// 读取失败的异常 先暴力处理 得需要一个UI
						e.printStackTrace();
						overworld.generateFromGenerator(false);
					}

					//should be replaced to stats for player
					cam.setLocation(overworld.getPlayer().getCoordinate().add(playerEyeBias));
					initScene(true);
				}else {
					initScene(false);
				}
				
				
			}
			blockList.setEnabled(true);
			flyCam.setEnabled(true);//隐藏指针
		}else if(appStatusNew.equals(OPTIONS_PANEL)) {
			optionsPanel.setEnabled(true);
		}else if(appStatusNew.equals(INGAME_MENU)) {
			overworld.saveMap();
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

	public static final Vector3f playerEyeBias = new Vector3f(0, (float)1.5, 0);

	private long counter = 0;
	/**
	 * 主循环
	 */
	@Override
	public void simpleUpdate(float deltaTime) {
		// updateBlockVisibility();
		if(!appStatus.equals(INGAME)) return;
		counter++;
		if(counter > 1000){
			counter = 0;
			System.gc();
		}
		
		try{
			if(overworld.getPlayer().updateFalling())
				cam.setLocation(overworld.getPlayer().getCoordinate().add(playerEyeBias));
		}catch(NullPointerException e){

		}
		
	}
	/*
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
	*/

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
		if(!appStatus.equals(INGAME)) return;
		System.out.println("app.breakBlockTemp()");
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
		if(!appStatus.equals(INGAME)) return;
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