package minicraft.frontend;


import com.jme3.collision.MotionAllowedListener;
import com.jme3.input.CameraInput;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;


public class GamerCamera extends FlyByCamera {

	private static String[] mappings = new String[]{
	        CameraInput.FLYCAM_LEFT,
	        CameraInput.FLYCAM_RIGHT,
	        CameraInput.FLYCAM_UP,
	        CameraInput.FLYCAM_DOWN,

	        CameraInput.FLYCAM_STRAFELEFT,
	        CameraInput.FLYCAM_STRAFERIGHT,
	        CameraInput.FLYCAM_FORWARD,
	        CameraInput.FLYCAM_BACKWARD,

	        CameraInput.FLYCAM_ZOOMIN,
	        CameraInput.FLYCAM_ZOOMOUT,
	        CameraInput.FLYCAM_ROTATEDRAG,

	        CameraInput.FLYCAM_RISE,
	        CameraInput.FLYCAM_LOWER,

	        CameraInput.FLYCAM_INVERTY
	    };
    
    private MiniCraftApp miniCraftApp;
    public GamerCamera(Camera cam, MiniCraftApp app){
        super(cam);
        this.miniCraftApp = app;
    }

	public GamerCamera(Camera cam) {
		super(cam);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	//因为FlyByCamera的这个函数有些bug，因此override
	public void setEnabled(boolean enable){
        if (enabled && !enable){
            if (inputManager!= null && (!dragToRotate || (dragToRotate && canRotate))){
                inputManager.setCursorVisible(true);
            }
        }
        
        if (!enabled && enable) {
        	if(inputManager!= null && !dragToRotate) {
        		inputManager.setCursorVisible(false);//FlyByCamera中没有这个
        	}
        }
        enabled = enable;
    }
	
	@Override
	/**
     * Register this controller to receive input events from the specified input
     * manager.
     *
     * @param inputManager
     */
    public void registerWithInput(InputManager inputManager){
        this.inputManager = inputManager;

        // both mouse and button - rotation of cam
        inputManager.addMapping(CameraInput.FLYCAM_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true),
                new KeyTrigger(KeyInput.KEY_LEFT));

        inputManager.addMapping(CameraInput.FLYCAM_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false),
                new KeyTrigger(KeyInput.KEY_RIGHT));

        inputManager.addMapping(CameraInput.FLYCAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false),
                new KeyTrigger(KeyInput.KEY_UP));

        inputManager.addMapping(CameraInput.FLYCAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true),
                new KeyTrigger(KeyInput.KEY_DOWN));

        // mouse only - zoom in/out with wheel, and rotate drag
        inputManager.addMapping(CameraInput.FLYCAM_ZOOMIN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(CameraInput.FLYCAM_ZOOMOUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping(CameraInput.FLYCAM_ROTATEDRAG, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        // keyboard only WASD for movement and 空格/CAPSLOCK for rise/lower height
        inputManager.addMapping(CameraInput.FLYCAM_STRAFELEFT, new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(CameraInput.FLYCAM_STRAFERIGHT, new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(CameraInput.FLYCAM_FORWARD, new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(CameraInput.FLYCAM_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping(CameraInput.FLYCAM_RISE, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping(CameraInput.FLYCAM_LOWER, new KeyTrigger(KeyInput.KEY_CAPITAL));

        inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(dragToRotate || !isEnabled());

        Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks != null && joysticks.length > 0){
            for (Joystick j : joysticks) {
                mapJoystick(j);
            }
        }
    }
	
	@Override
	/**
     * Translate the camera left or forward by the specified amount.
     *
     * @param value translation amount
     * @param sideways true&rarr;left, false&rarr;forward
     */
    protected void moveCamera(float value, boolean sideways){
        Vector3f vel = new Vector3f();
        Vector3f pos = cam.getLocation().clone();
        // System.out.println("begin " + pos.y);

        if (sideways){
            cam.getLeft(vel);
        }else{
            cam.getDirection(vel);
            vel.y=0;//这两行是我修改的地方
            vel=vel.normalize();
        }
        vel.multLocal(value * moveSpeed);

        if (motionAllowed != null)
            motionAllowed.checkMotionAllowed(pos, vel);
        else
            pos.addLocal(vel);

        // System.out.println("end " + pos.y);
        Vector3f temp = pos.subtract(MiniCraftApp.playerEyeBias);
        this.miniCraftApp.overworld.movePlayerTo(temp);
        cam.setLocation(temp.add(MiniCraftApp.playerEyeBias));
    }
    
    @Override
    protected void riseCamera(float value) {
        if(this.miniCraftApp.overworld.getPlayer().getLowGravelty()){
            super.riseCamera(value);
            this.miniCraftApp.overworld.movePlayerTo(cam.getLocation().subtract(MiniCraftApp.playerEyeBias));
        }else if(this.miniCraftApp.overworld.getPlayer().getOnGround()){
            this.miniCraftApp.overworld.getPlayer().setOnGround(false);
            this.miniCraftApp.overworld.getPlayer().setNaturalV(new Vector3f(0, (float)0.35, 0));
        }

    }
}
