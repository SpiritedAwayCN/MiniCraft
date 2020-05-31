package minicraft.frontend.gui;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

import minicraft.frontend.Assets;

public class Border extends Node {
	public final static int BORDER_WIDTH=4;
	
	private boolean cursorPassing=false;
	Geometry border;
	Border(Button button){
		Quad q=new Quad(button.getWidth()+BORDER_WIDTH*2,button.getHeight()+BORDER_WIDTH*2);
		border = new Geometry(null,q);
		Material mat = Assets.MATERIAL_UNSHADED.clone();
		mat.setColor("Color", ColorRGBA.Black);
		border.setMaterial(mat);
		border.setLocalTranslation(-BORDER_WIDTH,-BORDER_WIDTH,-0.2f);
		this.attachChild(border);
	}
	public void setCursorPassing(boolean arg) {
		if(cursorPassing==arg) return;
		//else
		Material mat = Assets.MATERIAL_UNSHADED.clone();
		
		if(arg) {
			mat.setColor("Color", ColorRGBA.White);
		}else {
			mat.setColor("Color", ColorRGBA.Black);
		}
		border.setMaterial(mat);
		cursorPassing=arg;
	}
}
