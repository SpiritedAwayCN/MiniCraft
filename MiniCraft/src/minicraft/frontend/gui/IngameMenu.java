package minicraft.frontend.gui;

import javax.swing.JOptionPane;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.ColorRGBA;

import minicraft.frontend.MiniCraftApp;

public class IngameMenu extends Panel {

	public IngameMenu(MiniCraftApp app) {
		super(app);


		this.addComponent(new Button("Start Menu", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("ingameMenu.About hit");
				app.switchAppStatus(app.START_MENU);
			}
		}));
		this.addComponent(new Button("More Options", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.Options hit");
				JOptionPane.showMessageDialog(null,
						"可修改项：按键设置|视距\n请用文本编辑工具到options.json中进行修改");
			
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
		Background bg2 = new Background(new ColorRGBA(1, 1, 1, 0.35f), app.getCamera().getWidth(), app.getCamera().getHeight());
		this.setBackground(bg2);
	}

}
