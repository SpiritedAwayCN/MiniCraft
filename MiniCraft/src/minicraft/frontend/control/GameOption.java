package minicraft.frontend.control;

import java.io.*;

import com.alibaba.fastjson.JSON;
import com.jme3.input.*;

public class GameOption {
    private static final String fileName = "./options.json";

    private int viewChunkDistance = 4;
    private int keyMappingMenu = KeyInput.KEY_ESCAPE;
    private int keyMappingSwitch = KeyInput.KEY_TAB;
    private int mouseMappingBreak = MouseInput.BUTTON_LEFT;
    private int mouseMappingPlace = MouseInput.BUTTON_RIGHT;
    private int keyCamRise = KeyInput.KEY_SPACE;
    private int keyCamLower = KeyInput.KEY_LSHIFT;

    public int getViewChunkDistance() {
        return viewChunkDistance;
    }
    public void setViewChunkDistance(int viewChunkDistance) {
        this.viewChunkDistance = viewChunkDistance;
    }
    public int getKeyMappingMenu() {
        return keyMappingMenu;
    }
    public void setKeyMappingMenu(int keyMappingMenu) {
        this.keyMappingMenu = keyMappingMenu;
    }
    public int getKeyMappingSwitch() {
        return keyMappingSwitch;
    }
    public void setKeyMappingSwitch(int keyMappingSwitch) {
        this.keyMappingSwitch = keyMappingSwitch;
    }
    public int getMouseMappingBreak() {
        return mouseMappingBreak;
    }
    public void setMouseMappingBreak(int mouseMappingBreak) {
        this.mouseMappingBreak = mouseMappingBreak;
    }
    public int getMouseMappingPlace() {
        return mouseMappingPlace;
    }
    public void setMouseMappingPlace(int mouseMappingPlace) {
        this.mouseMappingPlace = mouseMappingPlace;
    }
    public int getKeyCamLower() {
        return keyCamLower;
    }
    public void setKeyCamLower(int keyCamLower) {
        this.keyCamLower = keyCamLower;
    }
    public int getKeyCamRise() {
        return keyCamRise;
    }
    public void setKeyCamRise(int keyCamRise) {
        this.keyCamRise = keyCamRise;
    }

    public void saveToFile(){
        try(OutputStream out = new FileOutputStream(fileName)){
            String jsonString = JSON.toJSONString(this);
			out.write(jsonString.getBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static GameOption loadInstanceFromFile(){
        GameOption option = null;
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))){
            option = JSON.parseObject(br.readLine(), GameOption.class);
        }catch(Exception e){
            System.err.println("Options file not found, creating...");
            // e.printStackTrace();
        }
		if(option == null) {
			try(OutputStream out = new FileOutputStream(fileName)) {
				option = new GameOption();
				String jsonString = JSON.toJSONString(option);
				out.write(jsonString.getBytes());
			}catch (Exception e) {
				e.printStackTrace();
				return option;
			}
        }
        return option;
    }
}