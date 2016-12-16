package Client;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Manager.ManagerSession;
import shared.Config;


public class TotalRecords extends JPanel {

	private JPanel jpanel;
	private static JTextField txtMTLRecords,txtLVLRecords,txtDDORecords;

	private ManagerSession session;
	public JPanel panel(){
		
		JPanel jpanel = new JPanel();
		
		JLabel lblMTLRecords = new JLabel("Montreal Staff");
		txtMTLRecords = new JTextField();
		
		JLabel lblLVLRecords = new JLabel("Laval Staff");
		txtLVLRecords = new JTextField();
		
		JLabel lblDDORecords = new JLabel("DDO Staff");
		txtDDORecords = new JTextField();
		
		jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
		jpanel.add(lblMTLRecords);
		jpanel.add(txtMTLRecords);
		
		jpanel.add(lblLVLRecords);
		jpanel.add(txtLVLRecords);
		
		jpanel.add(lblDDORecords);
		jpanel.add(txtDDORecords);
		return jpanel;
		
	}
	
	public void getTotalRecord(ManagerSession session) throws Exception
	{
		String result = session.getService().execute(",,"+Login.managerCode+",,7,,");
		String[] myarray = result.split(",,");
		System.out.println(result);
		txtMTLRecords.setText(myarray[3]);
		txtDDORecords.setText(myarray[4]);
		txtLVLRecords.setText(myarray[5]);

	}
}
