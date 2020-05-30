package minicraft.frontend.gui;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.scene.Node;

import minicraft.frontend.Assets;

public class Label extends Node {
	static BitmapFont guiFont;
	static {
		if(!Assets.initialized)
			throw new RuntimeException();
		guiFont=Assets.assetManager.loadFont("Interface/Fonts/Default.fnt");
	}
	private BitmapText text;
	public void setText(String text) {
		this.text.setText(text);
		
	        
	}
	public String getText() {
		return this.text.getText();
	}
	
	Label(){
		this("");
	}
	Label(String text){
		this.text = new BitmapText(guiFont, false);
		this.text.setText(text);
		this.text.setSize(guiFont.getPreferredSize() * 2f);
		this.text.setLocalTranslation(0, this.text.getHeight(), 0);
        this.attachChild(this.text);
	}
	public int getHeight() {
		return (int) text.getHeight();
	}
	public int getWidth() {
		return text.getText().length()*10;
	}
	
}
