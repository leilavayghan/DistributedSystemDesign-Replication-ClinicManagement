package Client;


import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Manager.ManagerSession;


public class TransferRecord extends JPanel{
	
	private JPanel jpanel;
	private static JTextField txtRecordID,txtDestination;
	private static String strRecordID,strDestination;
	private static JComboBox cmbDestination;
	
	
	
	public JPanel panel(boolean restriction){			// false for creating , true for editing

	JPanel jpanel = new JPanel();
	
	JLabel lblRecordID = new JLabel("Record ID: ");
	txtRecordID = new JTextField();
		
	JLabel lblDesignation = new JLabel("Destination Server: ");
	
	if(Login.managerCode.equals("MTL")){
		String[] myStrings = { "DDO", "LVL"};
		cmbDestination = new JComboBox(myStrings);
	}
	else if(Login.managerCode.equals("DDO")){
		String[] myStrings = { "MTL", "LVL"};
		cmbDestination = new JComboBox(myStrings);
	}
	else if(Login.managerCode.equals("LVL")){
		String[] myStrings = { "MTL", "DDO"};
		cmbDestination = new JComboBox(myStrings);
	}
	
	
	jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
	jpanel.add(lblRecordID);
	jpanel.add(txtRecordID);
	
	jpanel.add(lblDesignation);
	jpanel.add(cmbDestination);
	
	if(restriction)
	{
		txtRecordID.setEditable(false);
	}
	return jpanel;
	
	}
	
	public String TransferValidCreate(ManagerSession session) throws Exception
	{
		
		strRecordID = txtRecordID.getText().toString();
		strDestination = cmbDestination.getSelectedItem().toString();
		if(strRecordID.equals(""))
			JOptionPane.showMessageDialog(jpanel,"Enter Record ID");	
		else if(strDestination.equals(""))
			JOptionPane.showMessageDialog(jpanel,"Choose Destination Server");
		else
		{
			return session.getService().execute(",,"+Login.managerCode+",,6,,"+strRecordID+",,"+strDestination+",,"+Login.managerID+",,");
		}
		return "transfer not done";
		
	}
}
