package minicraft.frontend.gui;

import javax.swing.JOptionPane;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;

import minicraft.frontend.Assets;
import minicraft.frontend.MiniCraftApp;

public class StartMenu extends Panel {

	public StartMenu(MiniCraftApp app) {
		super(app);
		
		this.addComponent(new Button("Quit", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				app.stop();
			}
		}));
		this.addComponent(new Button("About", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.About hit");
				JOptionPane.showMessageDialog(null,
						"作者：\n石淳安/SpiritedAwayCN\n李辰剑/IcyChlorine\n2020-5,copyright reserved.");
			}
		}));
		this.addComponent(new Button("Options", new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.Options hit");
				app.switchAppStatus(app.OPTIONS_PANEL);
			}
		}));
		Button gameStart = new Button("Start Game");
		gameStart.addActionListener(new ActionListener() {
			@Override
			public void onAction(String name, boolean value, float tpf) {
				// System.out.println("startMenu.GameStart hit");
				gameStart.setText("Loading...");// 无法正常显示，待修
				app.switchAppStatus(app.INGAME);
				gameStart.setText("Start Game");
			}
		});
		this.addComponent(gameStart);
		Background bg1 = new Background(Assets.GUI_START_MENU_BACKGROUND, 1920, 1080);
		bg1.reshape(app.getCamera().getWidth(), app.getCamera().getHeight());
		this.setBackground(bg1);
	}

}
