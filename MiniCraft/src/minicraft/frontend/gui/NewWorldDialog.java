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

	private JDialog dialog;
	private JTextField textField;
	private FairButton btnOK;
	
	private String newWorldName;
	private Boolean flat;
	
	private boolean aborted;//没有正常返回值，被取消了

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Object[] rst;
		try {
			rst = getWorldParam();
			System.out.println(rst.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		dialog = new JDialog();
		dialog.setBounds(500, 400, 350, 250);
		//frame.setSize(250,150);
		dialog.setTitle("Create New World");
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setModal(true);
		dialog.getContentPane().setLayout(null);
		dialog.setResizable(false);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				aborted=true;
				dialog.dispose();
			}
		});
		
		
		textField = new JTextField();
		textField.setBounds(58, 67, 202, 24);
		dialog.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("请输入新世界的名字：");
		lblNewLabel.setBounds(58, 36, 162, 18);
		dialog.getContentPane().add(lblNewLabel);
		
		JCheckBox chkboxFlat = new JCheckBox("超平坦");
		chkboxFlat.setBounds(58, 134, 73, 27);
		dialog.getContentPane().add(chkboxFlat);
		
		btnOK = new FairButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newWorldName=textField.getText();
				flat=chkboxFlat.isSelected();
				dialog.dispose();
			}
		});
		btnOK.setBounds(147, 134, 113, 27);
		dialog.getContentPane().add(btnOK);
	}
	/*
	 * @return Object[2] 其中Object[0]为String,是世界的名字，Object[1]为Boolean,表示是否为超平坦
	 * 	当窗口被关闭/取消时，返回Object[0]
	 * @throws 当世界名字为空时抛出异常
	 */
	public static Object[] getWorldParam() 
		throws Exception{
		NewWorldDialog newWorldDialog=new NewWorldDialog();
		newWorldDialog.dialog.setVisible(true);
		
		if(!newWorldDialog.aborted) {
			Object[] rst=new Object[2];
			if(newWorldDialog.newWorldName.isEmpty())
				throw new Exception();
			rst[0]=new String(newWorldDialog.newWorldName);
			rst[1]=new Boolean(newWorldDialog.flat);
			return rst;
		}else {
			Object[] rst=new Object[0];
			return rst;
		}
	}
}
