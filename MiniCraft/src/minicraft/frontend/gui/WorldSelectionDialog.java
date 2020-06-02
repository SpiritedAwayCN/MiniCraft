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

	private JDialog frame;

	private JList listWorlds;
	private DefaultListModel<String> listModelWorlds;
	
	private String selectedWorldName;
	private boolean newWorld;
	private Boolean flat;//if newWorld
	private Object[] returnParam;
	/**
	 * for test.
	 */
	public static void main(String[] args) {
		
		Object[] rst=getWorldParam();
		System.out.println(rst[0]);
		if(rst.length==2)
			System.out.println(rst[1]);
		
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
		frame = new JDialog();
		frame.setBounds(500, 400, 450, 300);
		frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		frame.setModal(true);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				loadWorldsFromFile();
			}
			
			
		});
		
		FairButton btnCreateWorldl = new FairButton("Create World");
		btnCreateWorldl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Object[] rst=NewWorldDialog.getWorldParam();
					selectedWorldName=(String)rst[0];
					flat=(Boolean)rst[1];
					newWorld=true;
					createResultAndQuit();
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(frame,
							"世界名字不能为空！");
				}
			}
		});
		btnCreateWorldl.setBounds(248, 47, 129, 27);
		frame.getContentPane().add(btnCreateWorldl);
		
		FairButton btnSelectWorld = new FairButton("Select World");
		btnSelectWorld.setBounds(248, 86, 129, 27);
		frame.getContentPane().add(btnSelectWorld);
		btnSelectWorld.setEnabled(false);
		
		
		FairButton btnDelWorld = new FairButton("Del World");
		btnDelWorld.setBounds(248, 126, 129, 27);
		frame.getContentPane().add(btnDelWorld);
		btnDelWorld.setEnabled(false);
		
		FairButton btnCancel = new FairButton("Cancel");
		btnCancel.setBounds(248, 186, 129, 27);
		frame.getContentPane().add(btnCancel);
		btnCancel.setEnabled(false);
		
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
				}else {
					btnSelectWorld.setEnabled(true);
					btnDelWorld.setEnabled(true);
				}
			}
		});
		frame.getContentPane().add(listWorlds);
		
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
		
	}

	protected void createResultAndQuit() {
		// TODO Auto-generated method stub
		if(newWorld) {
			returnParam=new Object[2];
			returnParam[0]=selectedWorldName;
			returnParam[1]=flat;
		}else {
			returnParam=new Object[1];
			returnParam[0]=selectedWorldName;
		}
		frame.dispose();
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
		worldSelectionDialog.frame.setVisible(true);
		return worldSelectionDialog.getResult();
	}
}
