package minicraft.frontend.gui;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

import minicraft.frontend.Assets;

public class Border extends Node {
	public final static int BORDER_WIDTH=4;
	
	boolean selected=false;
	Geometry border;
	Border(Button button){
		Quad q=new Quad(button.getWidth()+BORDER_WIDTH*2,button.getHeight()+BORDER_WIDTH*2);
		border = new Geometry(null,q);
		Material mat = Assets.MATERIAL_UNSHADED.clone();
		mat.setColor("Color", ColorRGBA.White);
		border.setMaterial(mat);
		border.setLocalTranslation(-BORDER_WIDTH,-BORDER_WIDTH,-0.2f);
		this.attachChild(border);
	}
}
