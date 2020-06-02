package minicraft.frontend.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.input.CameraInput;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

import minicraft.backend.constants.Constant;
import minicraft.frontend.Assets;

public class BlockList extends Node implements ActionListener{
	private BlockBox[] boxes;
	private static final int ROW_SIZE=8;
	
	private int currRow=0;//在第几排方块
	private static final int TOT_ROW_NUM=(Constant.BLOCK_TYPE_NUM-1)/ROW_SIZE+1;
	//数学运算搞得有点复杂，plz feel free to opt it, if needed.
	
	
	private int selectedBoxIdx;
	private boolean enabled=true;
	private SimpleApplication app;
	
	public BlockList(SimpleApplication app) {
		//初始化每个格子
		this.app=app;
		boxes=new BlockBox[ROW_SIZE];
		for(int i=0;i<ROW_SIZE;i++) {
			boxes[i]=new BlockBox();
			if(Assets.BLOCK_IMAGE[i+1]!=null) {
				boxes[i].setBlockByID(i+1);
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
		this.setLocalTranslation(w/2-BlockBox.width*(ROW_SIZE/2), 0, 0);
		
	}
	private void shiftRow(int row) {
		if(currRow==row) return;
		if(row<0 || row>=TOT_ROW_NUM) return;
		
		currRow=row;
		for(int i=0;i<ROW_SIZE;i++) {
			if(row*ROW_SIZE+i+1>=Constant.BLOCK_TYPE_NUM) {
				boxes[i].setBlockByID(-1);
			}
			else if(Assets.BLOCK_IMAGE[row*ROW_SIZE+i+1]!=null) {
				boxes[i].setBlockByID(row*ROW_SIZE+i+1);
			}
		}
	}
	public void setEnabled(boolean enabled) {
		if(this.enabled==enabled) return;
		if(!enabled) {
			this.detachAllChildren();
		}else {
			for(int i=0;i<ROW_SIZE;i++) {
				this.attachChild(boxes[i]);
			}
			this.reshape(app.getCamera().getWidth(),app.getCamera().getHeight());
		}
		
		this.enabled=enabled;
	}
	public boolean isEnabled() {
		return enabled;
	}
	private final static String[] MAPPINGS_BOX_SELECTED= {"box_0_selected",
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
	private final static String MAPPINGS_SWITCH_ROW_PREV="MAPPINGS_SWITCH_ROW_PREV";
	private final static String MAPPINGS_SWITCH_ROW_NEXT="MAPPINGS_SWITCH_ROW_NEXT";
	private void registryMapping(InputManager inputManager) {
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[0], new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[1], new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[2], new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[3], new KeyTrigger(KeyInput.KEY_4));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[4], new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[5], new KeyTrigger(KeyInput.KEY_6));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[6], new KeyTrigger(KeyInput.KEY_7));
		inputManager.addMapping(MAPPINGS_BOX_SELECTED[7], new KeyTrigger(KeyInput.KEY_8));
		
		inputManager.addMapping(MAPPINGS_SWITCH_ROW_PREV, new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping(MAPPINGS_SWITCH_ROW_NEXT, new KeyTrigger(KeyInput.KEY_DOWN));
		
		inputManager.addListener(this, MAPPINGS_BOX_SELECTED);
		inputManager.addListener(this, MAPPINGS_SWITCH_ROW_PREV);
		inputManager.addListener(this, MAPPINGS_SWITCH_ROW_NEXT);
		
	}
	@Override
    public void onAction(String name, boolean value, float tpf) {
		if(!enabled || !value)
			return;
		//find mapping
		
		if(name.equals(MAPPINGS_SWITCH_ROW_PREV)) {
			this.shiftRow(currRow-1);
			return;
		}else if(name.equals(MAPPINGS_SWITCH_ROW_NEXT)) {
			this.shiftRow(currRow+1);
			return;
		}
		
		for(int idx=0;idx<MAPPINGS_BOX_SELECTED.length;idx++) {
			if(name.equals(MAPPINGS_BOX_SELECTED[idx]))
				selectBox(idx);
		}
		
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
