package minicraft.frontend.gui;


import java.awt.event.ActionListener;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

import minicraft.frontend.Assets;

public class Button extends Node {
	private static final int width=792;
	private static final int height=72;
	
	Label text;
	Geometry pic;
	Border border;
	ActionListener listener;
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
}
