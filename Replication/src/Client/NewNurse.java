package Client;

import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Manager.ManagerSession;
import shared.enumerations.NurseDesignation;
import shared.enumerations.NurseStatus;


public class NewNurse extends JPanel{
	
	private JPanel jpanel;
	private static JTextField txtFirstName,txtLastName,txtStatusDate;
	private static String strFirstName,strLastName,strStatusDate;
	private Date statusDate;
	private static String strDesignation;
	private static String strStatus;
	private static JComboBox cmbStatus,cmbDesignation;
	
	public JPanel panel(boolean restriction){			// false for creating , true for editing

	JPanel jpanel = new JPanel();
	
	JLabel lblFirstName = new JLabel("First Name: ");
	txtFirstName = new JTextField();
	
	JLabel lblLastName = new JLabel("Last Name: ");
	txtLastName = new JTextField();
	
	JLabel lblDesignation = new JLabel("Designation: ");
	cmbDesignation = new JComboBox(NurseDesignation.values());
	
	JLabel lblStatus = new JLabel("Status: ");
	cmbStatus= new JComboBox(NurseStatus.values());
	
	JLabel lblStatusDate = new JLabel("Status Date: ");
	txtStatusDate = new JTextField();

	
	jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
	jpanel.add(lblFirstName);
	jpanel.add(txtFirstName);
	
	jpanel.add(lblLastName);
	jpanel.add(txtLastName);
	
	jpanel.add(lblDesignation);
	jpanel.add(cmbDesignation);
	
	jpanel.add(lblStatus);
	jpanel.add(cmbStatus);
	
	jpanel.add(lblStatusDate);
	jpanel.add(txtStatusDate);
	
	if(restriction)
	{
		txtFirstName.setEditable(false);
		txtLastName.setEditable(false);
	}
	return jpanel;
	
	}
	
	public String NurseValidCreate(ManagerSession session) throws Exception
	{
		
		strFirstName = txtFirstName.getText().toString();
		strLastName = txtLastName.getText().toString();
		strDesignation = cmbDesignation.getSelectedItem().toString();
		strStatus = cmbStatus.getSelectedItem().toString();
		strStatusDate = txtStatusDate.getText().toString();
		if(strFirstName.equals(""))
			JOptionPane.showMessageDialog(jpanel,"Enter First Name");	
		else if(strLastName.equals(""))
			JOptionPane.showMessageDialog(jpanel,"Enter Last Name");
		else
		{
				return session.getService().execute(",,"+Login.managerCode+",,3,,"+strFirstName+",,"+strLastName+
						",,"+strDesignation+",,"+strStatus+",,"+strStatusDate+
						",,"+Login.managerCode+",,"+Login.managerID+",,");
		}
		return "nurse create not done";
	}
}
