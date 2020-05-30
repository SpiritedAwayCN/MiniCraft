package minicraft.frontend.gui;

import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

import minicraft.frontend.Assets;

public class Button extends Node {
	private static final int width=792;
	private static final int height=72;
	
	private Label text;
	private Geometry pic;
	private Border border;
	private ActionListener listener;
	
	private boolean cursorPassing=false;
	private boolean clicking=false;
	
	Button(){
		this("");
	}
	Button(String caption){
		border=new Border(this);
		this.attachChild(border);
		
		Quad q=new Quad(width,height);
		pic = new Geometry(null,q);
		Material mat = Assets.MATERIAL_UNSHADED.clone();
		mat.setTexture("ColorMap", Assets.GUI_BUTTON);
		pic.setMaterial(mat);
		pic.setLocalTranslation(0,0,-0.1f);
		this.attachChild(pic);
		
		text=new Label(caption);
		text.setLocalTranslation(width/2-text.getWidth()/2, height/2-text.getHeight()/2, 0);
		this.attachChild(text);
	
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void setCursorPassing(boolean arg) {
		if(cursorPassing==arg) return;
		cursorPassing=arg;
		border.setCursorPassing(cursorPassing);
	}
	public void setClicking(boolean arg) {
		if(clicking==arg) return;
		if(arg) {
			pic.setLocalRotation(new Quaternion(0,0,1,0));//旋转180度，伪造按下去的效果
			pic.setLocalTranslation(width,height,-0.1f);
			text.move(4,-4,0);
		}else {
			pic.setLocalRotation(new Quaternion(0,0,0,1));//旋转180度，伪造按下去的效果
			pic.setLocalTranslation(0,0,-0.1f);
			text.move(-4,4,0);
		}
			
		clicking=arg;
	}
	public boolean isClicking() {
		return clicking;
	}
	public void onClick() {
		if(listener==null) return;
		listener.onAction(null, false, 0);
	}
	public void addActionListener(ActionListener listener) {
		this.listener=listener;
	}
}
