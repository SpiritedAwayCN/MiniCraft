
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;

/**
 * 你的第一个jME3程序
 * @author yanmaoyuan
 */
public class HelloJME3 extends SimpleApplication {
	
	private Geometry geom;

	/**
	 * 初始化3D场景，显示一个方块。
	 */
	@Override
	public void simpleInitApp() {
		
		 // #1 创建一个无光材质
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

        // #2 设置纹理贴图
        // 漫反射贴图
        //assetManager.registerLocator("C:/StarSky/Programming/JavaLab/MiniCraft/MiniCraft/assets", FileLocator.class);
        assetManager.registerLocator("assets", FileLocator.class);
        
        Texture tex = assetManager.loadTexture("texture/dirt.bmp");
        tex.setMagFilter(MagFilter.Nearest);
        mat.setTexture("DiffuseMap", tex);
        
        // 法线贴图
        //tex = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall_normal.jpg");
        //mat.setTexture("NormalMap", tex);
        
        // 视差贴图
        //tex = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall_height.jpg");
        //mat.setTexture("ParallaxMap", tex);

        // 设置反光度
        mat.setFloat("Shininess", 2.0f);
        
        // #3 创造1个方块，应用此材质。
        Geometry geom = new Geometry("文艺方块", new Box(1, 1, 1));
        geom.setMaterial(mat);
        
        rootNode.attachChild(geom);
        
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
		settings.setTitle("一个方块");
		settings.setResolution(480, 720);
		
		// 启动jME3程序
		HelloJME3 app = new HelloJME3();
		//app.setSettings(settings);// 应用参数
		//app.setShowSettings(false);
		app.start();
	}

}