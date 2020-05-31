package minicraft.frontend.gui;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Panel extends Node implements ActionListener,AnalogListener{
	private List<Button> buttons=new ArrayList<Button>();
	private Background background;
	private boolean enabled=true;
	private int prevWidth,prevHeight;
	private SimpleApplication app;
	
	public Panel(SimpleApplication app) {
		this.app=app;
		
		registerWithInput(app.getInputManager());
		prevWidth=app.getCamera().getWidth();
		prevHeight=app.getCamera().getHeight();
		this.reshape(prevWidth,prevHeight);
	}
	public void addComponent(Button button) {
		buttons.add(button);
		if(enabled) this.attachChild(button);
		this.reshape(prevWidth,prevHeight);
	}
	public void reshape(int w,int h) {
		int idx=0;
		for(Button button:buttons) {
			button.setLocalTranslation(w/2-button.getWidth()/2,
					(float) (h*0.35+(idx-buttons.size()/2f)*(button.getHeight()+20)),
					0);
			idx++;
			
		}
		if(background!=null)
		{
			background.reshape(w, h);
		}
		prevWidth=w;
		prevHeight=h;
	}
	public void setEnabled(boolean enabled) {
		if(this.enabled==enabled) return;
		if(!enabled) {
			this.detachAllChildren();
		}else {
			for(Button button:buttons) {
				this.attachChild(button);
			}
			if(background!=null) {
				this.attachChild(background);
			}
			this.reshape(app.getCamera().getWidth(), app.getCamera().getHeight());
		}
		this.enabled=enabled;
	}
	public boolean isEnabled() {
		return enabled;
	}
	/**
     * Register this controller to receive input events from the specified input
     * manager.
     *
     * @param inputManager
     */
	private static final String MAPPING_MOUSE_MOVE="MAPPING_MOUSE_MOVE";
	private static final String MAPPING_MOUSE_CLICK="MAPPING_MOUSE_CLICK";
    public void registerWithInput(InputManager inputManager){
     

        // both mouse and button - rotation of cam
        inputManager.addMapping(MAPPING_MOUSE_MOVE, 
        		new MouseAxisTrigger(MouseInput.AXIS_X, true),
        		new MouseAxisTrigger(MouseInput.AXIS_X, false),
        		new MouseAxisTrigger(MouseInput.AXIS_Y, true),
        		new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        
        
        inputManager.addMapping(MAPPING_MOUSE_CLICK, 
        		new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
       
       
        inputManager.addListener(this, MAPPING_MOUSE_MOVE);
        inputManager.addListener(this, MAPPING_MOUSE_CLICK);

    }
    private static boolean pointInButton(Vector2f r,Button button) {
    	Vector3f r1=button.getLocalTranslation();
    	Vector2f r2=new Vector2f(r1.x+button.getWidth(),r1.y+button.getHeight());
    	return r1.x<r.x && r.x<r2.x &&
    			r1.y<r.y && r.y<r2.y;
    }
	@Override
	public void onAnalog(String arg0, float arg1, float arg2) {
		//System.out.println("panel.onAnalog");
		if(!enabled) return;
		for(Button button:buttons) {
			if(pointInButton(app.getInputManager().getCursorPosition(),button)) {
				button.setCursorPassing(true);
			}else {
				button.setCursorPassing(false);
			}
		}
		
	}
	@Override
	public void onAction(String arg0, boolean arg1, float arg2) {
		// TODO Auto-generated method stub
		if(!enabled) return;
		for(Button button:buttons) {
			if(pointInButton(app.getInputManager().getCursorPosition(),button) && arg1) {//left button down
				button.setClicking(true);
				
			}else if(!arg1){
				if(button.isClicking())
					button.onClick();
				button.setClicking(false);
				
				//鼠标抬起时触发按钮
			}
		}
	}
	public void setBackground(Background bg) {
		background=bg;
		if(enabled)
			this.attachChild(bg);
	}
}
