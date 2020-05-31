package minicraft.frontend.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import minicraft.frontend.Assets;

public class BlockList extends Node implements ActionListener{
	private BlockBox[] boxes;
	private int size=8;
	private int selectedBoxIdx;
	private boolean enabled=true;
	private int prevWidth,prevHeight;//窗口长宽
	private SimpleApplication app;
	
	public BlockList(SimpleApplication app) {
		//初始化每个格子
		this.app=app;
		boxes=new BlockBox[size];
		for(int i=0;i<size;i++) {
			boxes[i]=new BlockBox();
			if(Assets.BLOCK_IMAGE[i]!=null) {
				boxes[i].setBlockByID(i);
			}
			boxes[i].setLocalTranslation(i*BlockBox.width,0,0);
			this.attachChild(boxes[i]);
		}
		//默认选中第零个格子
		boxes[1].setSelected(true);
		selectedBoxIdx=1;
		//将方块列表居中
		this.reshape(app.getCamera().getWidth(),
				app.getCamera().getHeight());
		
		registryMapping(app.getInputManager());
	}
	public void reshape(int w,int h) {
		this.setLocalTranslation(w/2-BlockBox.width*(size/2), 0, 0);
		prevWidth=w;
		prevHeight=h;
	}
	public void setEnabled(boolean enabled) {
		if(this.enabled==enabled) return;
		if(!enabled) {
			this.detachAllChildren();
		}else {
			for(int i=0;i<size;i++) {
				this.attachChild(boxes[i]);
			}
			this.reshape(app.getCamera().getWidth(),app.getCamera().getHeight());
		}
		
		this.enabled=enabled;
	}
	public boolean isEnabled() {
		return enabled;
	}
	final static String[] MAPPINGS_BOX_SELECTED= {"box_0_selected",
			"box_1_selected",
			"box_2_selected",
			"box_3_selected",
			"box_4_selected",
			"box_5_selected",
			"box_6_selected",
			"box_7_selected",
			"box_8_selected",
			"box_9_selected",
	};
	private void registryMapping(InputManager inputManager) {
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[0], new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[1], new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[2], new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[3], new KeyTrigger(KeyInput.KEY_4));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[4], new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[5], new KeyTrigger(KeyInput.KEY_6));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[6], new KeyTrigger(KeyInput.KEY_7));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[7], new KeyTrigger(KeyInput.KEY_8));
		
		inputManager.addListener(this, MAPPINGS_BOX_SELECTED);
		
	}
	@Override
    public void onAction(String name, boolean value, float tpf) {
		if(!enabled || !value)
			return;
		//find mapping
		int idx=0;
		for(;idx<MAPPINGS_BOX_SELECTED.length;idx++) {
			if(name.equals(MAPPINGS_BOX_SELECTED[idx]))
				break;
		}
		selectBox(idx);
	}
	private void selectBox(int idx) {
		boxes[selectedBoxIdx].setSelected(false);
		selectedBoxIdx=idx;
		boxes[selectedBoxIdx].setSelected(true);
	}
	public int getSelectedBlockid() {
		return boxes[selectedBoxIdx].getBlockid();
	}
}
