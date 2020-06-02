package minicraft.frontend.gui;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/*
 * 应为Singleton，但尚未实现，待实现
 */
public class NewWorldDialog {

	private JDialog frame;
	private JTextField textField;
	private FairButton btnOK;
	
	private String newWorldName;
	private Boolean flat;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//Object[] rst=getWorldParam();
		//System.out.println(rst[0]);
		//System.out.println(rst[1]);
	}

	/**
	 * Create the application.
	 */
	public NewWorldDialog() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JDialog();
		frame.setBounds(500, 400, 350, 250);
		//frame.setSize(250,150);
		frame.setTitle("Create New World");
		frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		frame.setModal(true);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
	        public void windowClosing(WindowEvent e) {
				
				frame.dispose();
	        }	
			
		});
		
		textField = new JTextField();
		textField.setBounds(58, 67, 202, 24);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("请输入新世界的名字：");
		lblNewLabel.setBounds(58, 36, 162, 18);
		frame.getContentPane().add(lblNewLabel);
		
		JCheckBox chkboxFlat = new JCheckBox("超平坦");
		chkboxFlat.setBounds(58, 134, 73, 27);
		frame.getContentPane().add(chkboxFlat);
		
		btnOK = new FairButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newWorldName=textField.getText();
				flat=chkboxFlat.isSelected();
				frame.dispose();
			}
		});
		btnOK.setBounds(147, 134, 113, 27);
		frame.getContentPane().add(btnOK);
	}
	/*
	 * @return Object[2] 其中Object[0]为String,是世界的名字，Object[1]为Boolean,表示是否为超平坦
	 * @throws 当世界名字为空时抛出异常
	 */
	public static Object[] getWorldParam() 
		throws Exception{
		NewWorldDialog newWorldDialog=new NewWorldDialog();
		newWorldDialog.frame.setVisible(true);
		Object[] rst=new Object[2];
		if(newWorldDialog.newWorldName.isEmpty())
			throw new Exception();
		rst[0]=new String(newWorldDialog.newWorldName);
		rst[1]=new Boolean(newWorldDialog.flat);
		return rst;
	}
}
