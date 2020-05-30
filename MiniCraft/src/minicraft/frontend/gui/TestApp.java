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

    	
    	p=new Panel(this);
    	Button b1=new Button("button a");
    	b1.addActionListener(new ActionListener() {
    		@Override
    		public void onAction(String arg0,boolean arg1,float arg2) {
    			System.out.println("button a clicked!");
    		}
    	});
    	p.addComponent(b1);
    	Button b2=new Button("button b");
    	b2.addActionListener(new ActionListener() {
    		@Override
    		public void onAction(String arg0,boolean arg1,float arg2) {
    			System.out.println("button b clicked!");
    		}
    	});
    	p.addComponent(b2);
    	Button b3=new Button("button c");
    	b3.addActionListener(new ActionListener() {
    		@Override
    		public void onAction(String arg0,boolean arg1,float arg2) {
    			System.out.println("button c clicked!");
    		}
    	});
    	p.addComponent(b3);
        guiNode.attachChild(p);
    }
    Panel p;
    @Override
    public void reshape(int w,int h) {
    	super.reshape(w, h);
    	p.reshape(w,h);
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