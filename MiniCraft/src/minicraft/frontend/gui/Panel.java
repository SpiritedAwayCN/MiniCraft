package minicraft.frontend.gui;

import java.util.List;

import com.jme3.app.StatsAppState;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.scene.Node;

public class Panel extends Node implements ActionListener,AnalogListener{
	private List<Button> buttons;
	
	public Panel() {
		
	}
	public void addComponent(Button button) {
		buttons.add(button);
	}
	public void reshape(int w,int h) {
		int idx=0;
		for(Button button:buttons) {
			button.setLocalTranslation(w/2-button.getWidth(),
					(float) (h*0.2+idx*button.getHeight()+20),
					0);
			idx++;
		}
	}
}
