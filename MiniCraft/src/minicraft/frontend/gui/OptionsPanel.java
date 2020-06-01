package minicraft.frontend.gui;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;

import minicraft.frontend.MiniCraftApp;

public class OptionsPanel extends Panel {

	public OptionsPanel(MiniCraftApp app) {
		super(app);

		this.addComponent(new Button("Back", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("ingameMenu.About hit");
				app.switchAppStatus(app.START_MENU);
			}
		}));
		this.addComponent(new Button("Switch Render Shadow", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.Options hit");
				app.switchRenderShadow();
			}
		}));
		this.addComponent(new Button("Switch Render Transparent Leaves", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.Options hit");
				app.switchRenderTransparentLeaves();
			}
		}));
		//optionsPanel.setBackground(bg1);
	}

}
