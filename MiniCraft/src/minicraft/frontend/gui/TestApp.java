package minicraft.frontend.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.FlyByCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;

import minicraft.frontend.Assets;
import minicraft.frontend.FlyCamAppState;
import minicraft.frontend.GamerCamera;

public class TestApp extends SimpleApplication {

    private String txtB =
    "ABCDEFGHIKLMNOPQRSTUVWXYZ1234567890`~!@#$%^&*()-=_+[]\\;',./{}|:<>?";

    public static void main(String[] args){
        TestApp app = new TestApp();
        AppSettings settings = new AppSettings(true);
        settings.setResizable(true);
        app.setSettings(settings);// 应用参数
        app.start();
    }

    @Override
    public void simpleInitApp() {
    	Assets.initialize(assetManager);

    	
    	Button l=new Button("hello");
    	//l.setLocalTranslation(0, 100, 0);
    	//Quad q=new Quad(792,72);
		//Geometry pic = new Geometry(null,q);
		//Material mat = Assets.MATERIAL_UNSHADED.clone();
		//mat.setTexture("ColorMap", Assets.BLOCK_BOX);
		//pic.setMaterial(mat);
		//pic.setLocalTranslation(0,0,-1);
		//this.attachChild(pic);
    	l.setLocalTranslation(100,100,0);
        guiNode.attachChild(l);
    }
    @Override
	public void initialize() {
		super.initialize();
		if (stateManager.getState(FlyCamAppState.class) != null) {
            flyCam = new FlyByCamera(cam);
            flyCam.setMoveSpeed(5f); // odd to set this here but it did it before
            stateManager.getState(FlyCamAppState.class).setCamera( flyCam );
        }
		registerInputMapping();
	}
    public static final String INPUT_MAPPING_MENU = "MYAPP_Menu";
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
			
			
		}
	}

}