package minicraft.frontend.gui;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.ui.Picture;

import minicraft.backend.constants.Constant;
import minicraft.frontend.Assets;

public class BlockBox extends Node {
	public static final int width=80;
	public static final int height=80;
	
	private static final int block_width=64;
	private static final int block_height=64;
	private static final int block_x_offset=(width-block_width)/2;
	private static final int block_y_offset=(width-block_width)/2;
	
	private static final int selected_width=80+4*2;
	private static final int selected_height=80+4;
	private static final int selected_box_x_offset=-4;
	private static final int selected_box_y_offset=0;
	private static final float selected_box_z_offset=-0.9f;
	
	private Geometry box,block;
	int blockid;
	private boolean selected=false;
	
	public BlockBox(){
		Quad q=new Quad(width,height);
		box = new Geometry(null,q);
		Material mat = Assets.MATERIAL_UNSHADED.clone();
		mat.setTexture("ColorMap", Assets.BLOCK_BOX);
		box.setMaterial(mat);
		box.setLocalTranslation(0,0,-1);
		
		q=new Quad(block_width,block_height);
		mat = Assets.MATERIAL_UNSHADED.clone();
		//mat.setTexture("ColorMap", Assets.BLOCK_IMAGE[1]);
		mat.setTexture("ColorMap", Assets.NULL_TEXTURE);
		//设置透明
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		block = new Geometry(null,q);
		block.setMaterial(mat);
		block.setLocalTranslation(block_x_offset,block_y_offset,0);
		//block.setQueueBucket(Bucket.Transparent);
		
		this.attachChild(box);
		this.attachChild(block);
	}
	
	@Override
	public Spatial move(float dx,float dy,float dz) {
		//System.out.println("BlockBox.move()");
		box.move(dx,dy,dz);
		block.move(dx,dy,dz);
		return this;
	}
	/*
	 * @param 框里要显示的方块的id，输入-1来显示nothing
	 */
	public void setBlockByID(int Blockid) 
		throws IllegalArgumentException{
		if(Blockid<-1 || Blockid>Constant.BLOCK_TYPE_NUM )
			throw new IllegalArgumentException();
		blockid=Blockid;
		Material mat = Assets.MATERIAL_UNSHADED.clone();
		if(Blockid==-1) {
			mat.setTexture("ColorMap", Assets.NULL_TEXTURE);
		}else if(Assets.BLOCK_IMAGE[Blockid]==null) {
			mat.setTexture("ColorMap", Assets.NULL_TEXTURE);
		}else {
			mat.setTexture("ColorMap", Assets.BLOCK_IMAGE[Blockid]);
		}
		//设置透明
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		block.setMaterial(mat);
	}
	/*
	 * @return 若为空，返回-1
	 */
	public int getBlockid() {
		return blockid;
	}
	
	public void setSelected(boolean selected) {
		if(this.selected==selected)
			return;
		if(selected) {
			Quad q=new Quad(selected_width,selected_height);
			box.setMesh(q);
			Material mat = Assets.MATERIAL_UNSHADED.clone();
			mat.setTexture("ColorMap", Assets.BLOCK_BOX_SELECTED);
			box.setMaterial(mat);
			box.setLocalTranslation(selected_box_x_offset,
							selected_box_y_offset,
							selected_box_z_offset);
		}else{
			Quad q=new Quad(width,height);
			box.setMesh(q);
			Material mat = Assets.MATERIAL_UNSHADED.clone();
			mat.setTexture("ColorMap", Assets.BLOCK_BOX);
			box.setMaterial(mat);
			box.setLocalTranslation(0,0,-1);
							
		}
		this.selected=selected;
	}
	public boolean getSelected() {
		return this.selected;
	}
}
