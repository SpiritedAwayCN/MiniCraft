package minicraft.frontend.gui;

import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

import minicraft.frontend.Assets;

public class Background extends Node {
	private Geometry pic;
	private static int picWidth=1920;
	private static int picHeight=1080;
	private boolean hasPic;
	
	public Background(Texture tex,int texWidth,int texHeight){
		hasPic=true;
		picWidth=texWidth;
		picHeight=texHeight;
		Quad q=new Quad(picWidth,picHeight);
		pic = new Geometry(null,q);
		Material mat = Assets.MATERIAL_UNSHADED.clone();
		mat.setTexture("ColorMap", tex);
		pic.setMaterial(mat);
		pic.setLocalTranslation(0,0,-1);
		this.attachChild(pic);
	}
	public Background(ColorRGBA color,int width,int height) {
		hasPic=false;
		Quad q=new Quad(width,height);
		pic = new Geometry(null,q);
		Material mat = Assets.MATERIAL_UNSHADED.clone();
		mat.setColor("Color", color);
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		pic.setMaterial(mat);
		pic.setLocalTranslation(0,0,-1);
		this.attachChild(pic);
	}
	public void setPictureSize(int w,int h) {
		picWidth=w;
		picHeight=h;
	}
	public void reshape(int w,int h) {
		if(!hasPic) {
			pic.setMesh(new Quad(w,h));
		}else {
			if((float)w/h>=(float)picWidth/picHeight) {//屏幕比照片宽（按比例）
				pic.setMesh(new Quad(w,(float)w*picHeight/picWidth));
				setLocalTranslation(0,-((float)w*picHeight/picWidth-h)/2,-1);
				//保持图片不形变
			}else {
				pic.setMesh(new Quad((float)h*picWidth/picHeight,h));
				setLocalTranslation(-((float)h*picWidth/picHeight-w)/2,0,-1);
				//保持图片不形变
			}
			
		}
	}
}
