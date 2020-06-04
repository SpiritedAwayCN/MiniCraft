package minicraft.frontend.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;





/*
 * 应为Singleton，但尚未实现，待实现
 */
public class WorldSelectionDialog {

	private JDialog dialog;

	private JList listWorlds;
	private DefaultListModel<String> listModelWorlds;
	
	private String selectedWorldName;
	private boolean newWorld;
	private Boolean flat;//if newWorld
	private Object[] returnParam;
	
	private boolean aborted;//这一窗口是否被取消/关闭
	/**
	 * for test.
	 */
	public static void main(String[] args) {
		
		Object[] rst=getWorldParam();
		System.out.println(rst.length);
		//if(rst.length==2)
		//	System.out.println(rst[1]);
		
	}

	/**
	 * Create the application.
	 */
	public WorldSelectionDialog() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		dialog = new JDialog();
		dialog.setBounds(500, 400, 450, 300);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setModal(true);
		dialog.getContentPane().setLayout(null);
		dialog.setResizable(false);
		
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				loadWorldsFromFile();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				aborted=true;
				createResultAndQuit();
			}
		});
		
		FairButton btnCreateWorld = new FairButton("Create World");
		btnCreateWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Object[] rst=NewWorldDialog.getWorldParam();
					if(rst.length>0) {
						selectedWorldName=(String)rst[0];
						flat=(Boolean)rst[1];
						newWorld=true;
						createResultAndQuit();
					}else {
						//新世界窗口被取消了，于是什么也不做
					}
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(dialog,
							"世界名字不能为空！");
				}
			}
		});
		btnCreateWorld.setBounds(248, 47, 129, 27);
		dialog.getContentPane().add(btnCreateWorld);
		
		FairButton btnSelectWorld = new FairButton("Select World");
		btnSelectWorld.setBounds(248, 86, 129, 27);
		dialog.getContentPane().add(btnSelectWorld);
		btnSelectWorld.setEnabled(false);
		
		FairButton btnDelWorld = new FairButton("Del World");
		btnDelWorld.setBounds(248, 126, 129, 27);
		dialog.getContentPane().add(btnDelWorld);
		btnDelWorld.setEnabled(false);
		
		FairButton btnCancel = new FairButton("Cancel");
		btnCancel.setBounds(248, 186, 129, 27);
		dialog.getContentPane().add(btnCancel);
		//btnCancel.setEnabled(false);
		
		listWorlds = new JList();
		listWorlds.setBounds(47, 50, 152, 168);
		listModelWorlds=new DefaultListModel<String>();
		listWorlds.setModel(listModelWorlds);
		listWorlds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listWorlds.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(listWorlds.isSelectionEmpty()) {
					btnSelectWorld.setEnabled(false);
					btnDelWorld.setEnabled(false);
					selectedWorldName=listModelWorlds.elementAt(listWorlds.getSelectedIndex());
				}else {
					btnSelectWorld.setEnabled(true);
					btnDelWorld.setEnabled(true);
					selectedWorldName=null;
				}
			}
		});
		dialog.getContentPane().add(listWorlds);
		
		btnSelectWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedWorldName=listModelWorlds.elementAt(listWorlds.getSelectedIndex());
				System.out.println("world selected: "+selectedWorldName);
				//frame.dispose();
				createResultAndQuit();
			}
		});
		btnDelWorld.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedWorldName=listModelWorlds.elementAt(listWorlds.getSelectedIndex());
				//System.out.println("world deleted: "+selectedWorldName);
				
				File world=new File("./saves/"+selectedWorldName+".json");
				world.delete();
				
				listModelWorlds.remove(listWorlds.getSelectedIndex());
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aborted=true;
				createResultAndQuit();
			}
		});
	}

	protected void createResultAndQuit() {
		// TODO Auto-generated method stub
		if(!aborted) {
			if(newWorld) {
				returnParam=new Object[2];
				returnParam[0]=selectedWorldName;
				returnParam[1]=flat;
			}else {
				returnParam=new Object[1];
				returnParam[0]=selectedWorldName;
			}
		}else {//取消，或是关闭了这一窗口
			returnParam=new Object[0];
		}
		dialog.dispose();//模态窗口关闭，即相当于return返回
	}

	protected void loadWorldsFromFile() {
		File saveDir=new File("./saves/");
		String[] worlds=saveDir.list();
		for(int i=0;i<worlds.length;i++) {
			listModelWorlds.addElement(worlds[i].replaceAll("\\.json$", ""));
		}
		
	}
	public Object[] getResult() {
		return returnParam;
	}
	
	public static Object[] getWorldParam() {
		WorldSelectionDialog worldSelectionDialog=new WorldSelectionDialog();
		worldSelectionDialog.dialog.setVisible(true);
		return worldSelectionDialog.getResult();
	}
}
