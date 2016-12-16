package Client;



import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Manager.ManagerSession;

public class EditRecords {
	
	public static String recordID;
	private ManagerSession session;
	private String doctorEdit[] = {"Address","Phone Number"};
	private String nurseEdit[] = {"Status Date","designation","status"};
	public static JComboBox cmbEditValue,cmbDEditField,cmbEditField;
	public static JTextField txtDoctorID,txtNurseID,txtDEditField,txtNEditField,txtDEditValue,txtDeleteValue;
	public String strIDForDelete,strIDForEdit,strFieldForEdit,strValueForEdit;
	
	public JPanel panelDoctorID(){
		JPanel jpanelid = new JPanel();
		
		JLabel lblDoctorID = new JLabel("Doctor ID");
	     txtDoctorID = new JTextField();
		jpanelid.setLayout(new BoxLayout(jpanelid, BoxLayout.Y_AXIS));
		
		jpanelid.add(lblDoctorID);
		jpanelid.add(txtDoctorID);
		
		
		return jpanelid;
	}
	
	public JPanel panelNurseID(){
		JPanel jpanelid = new JPanel();
		
		JLabel lblNurseID = new JLabel("Nurse ID");
	    txtNurseID = new JTextField();
		jpanelid.setLayout(new BoxLayout(jpanelid, BoxLayout.Y_AXIS));
		
		jpanelid.add(lblNurseID);
		jpanelid.add(txtNurseID);
		
		return jpanelid;
	}
	
	public JPanel doctorEditPanel(){
		JPanel paneldoctoredit = new JPanel();
		
		JLabel lblEditField = new JLabel("Edit Field");
		cmbDEditField = new JComboBox(doctorEdit);
		txtDEditValue = new JTextField();
		paneldoctoredit.setLayout(new BoxLayout(paneldoctoredit, BoxLayout.Y_AXIS));
		
		paneldoctoredit.add(lblEditField);
		paneldoctoredit.add(cmbDEditField);
		paneldoctoredit.add(txtDEditValue);
		
		return paneldoctoredit;
	}
	

	
	public JPanel nurseEditPanel(){
		JPanel panelnurseedit = new JPanel();
		
		JLabel lblEditField = new JLabel("Edit Field");
		cmbEditField = new JComboBox(nurseEdit);
		txtNEditField =new JTextField();
		
		panelnurseedit.setLayout(new BoxLayout(panelnurseedit, BoxLayout.Y_AXIS));
		
		
		panelnurseedit.add(lblEditField);
		panelnurseedit.add(cmbEditField);
		panelnurseedit.add(txtNEditField);
		return panelnurseedit;
	}
	
	public String getRecordIDForDoctorEdit()
	{
		recordID = txtDoctorID.getText().toString();
		return recordID;
	}
	
	public String getRecordIDForNurseEdit()
	{
		recordID = txtNurseID.getText().toString();
		return recordID;
	}
	
	public String getRecordIdForDelete()
	{
		recordID = txtDeleteValue.getText().toString();
		return recordID;
	}
	
	public String editNurse(ManagerSession session)
	{
		strIDForEdit = txtNurseID.getText().toString();
		strFieldForEdit = cmbEditField.getSelectedItem().toString();
		strValueForEdit = txtNEditField.getText().toString();
		return session.getService().execute(",,"+Login.managerCode+",,5,,"+strIDForEdit+",,"+strFieldForEdit+",,"+strValueForEdit+",,");
	}
	
	public String editDoctor(ManagerSession session)
	{
		strIDForEdit = txtDoctorID.getText().toString();
		strFieldForEdit = cmbDEditField.getSelectedItem().toString();
		strValueForEdit = txtDEditValue.getText().toString();
		return session.getService().execute(",,"+Login.managerCode+",,5,,"+strIDForEdit+",,"+strFieldForEdit+",,"+strValueForEdit+",,");
	}
}
