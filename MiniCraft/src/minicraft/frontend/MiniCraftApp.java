package minicraft.frontend;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ConstantVerifierState;
import com.jme3.app.DebugKeysAppState;
//import com.jme3.app.FlyCamAppState;
//用了minicraft.frontend.FlyCamAppState
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioListenerState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
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
	
	private Geometry geom;
	
	
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
		}
	}
	
	/**
	 * 初始化APP
	 */
	@Override
	public void simpleInitApp() {
		System.out.println("MiniCraftApp.simpleInitApp()");
		
		DimensionMap overworld=new DimensionMap("overworld");
		overworld.generateFromGenerator(true);
		BlockBackend block=BlockBackend.getBlockInstanceByID(1);
		block.placeAt(new BlockCoordinate(0,1,0), overworld);
		
		GeometryBlock.initialize(assetManager);
		
		
		for(int x=Constant.minX;x<=Constant.maxX;x++) {
			for(int y=Constant.minY;y<=Constant.maxY;y++) {
				for(int z=Constant.minZ;z<=Constant.maxZ;z++) {
					geom=new GeometryBlock(overworld.getBlockByCoordinate(new BlockCoordinate(x,y,z)));
					rootNode.attachChild(geom);
				}
			}
		}
		//geom=new GeometryBlock(block);

		//geom.setMaterial();
		//geom.setMesh(null);
		
		//rootNode.attachChild(geom);
		
        
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
	
	/**
	 * 主循环
	 */
	@Override
	public void simpleUpdate(float deltaTime) {
		// 旋转速度：每秒360°
		float speed = FastMath.TWO_PI;
		// 让方块匀速旋转
		//geom.rotate(0, deltaTime * speed, 0);
		//geom.move(0.0f,0.1f*deltaTime,0.0f);
		
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