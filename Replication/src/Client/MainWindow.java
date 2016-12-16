package Client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Manager.ManagerSession;

public class MainWindow extends JFrame {

	public static JFrame jframe;
	public  JPanel jpanel;
	public static String title = "COMP 6321 DSMS";
	private int conditionID=0;
	private boolean check= false;
	private static int recordindex =0;
	private static String allrecords[] =  new String[100];
	private ManagerSession session;
	
	public MainWindow(ManagerSession session){
		
	JFrame jframe = new JFrame();
	JPanel jpanel = new JPanel();
	
	JMenuBar menubar = new JMenuBar();
	JMenuItem menuDoctorRecord = new JMenuItem("Create Doctor");
	JMenuItem menuNurseRecord = new JMenuItem("Create Nurse");
	JMenuItem menuTotalRecord = new JMenuItem("Total Records");
	JMenuItem menuEditDoctorRecord = new JMenuItem("Edit Doctor");
	JMenuItem menuEditNurseRecord = new JMenuItem("Edit Nurse");
	JMenuItem menuInfoRecord = new JMenuItem("Record Info");
	JMenuItem menuTransferRecord = new JMenuItem("Transfer Record");
	
	
	menubar.add(menuDoctorRecord);
	menubar.add(menuNurseRecord);
	menubar.add(menuTotalRecord);
	menubar.add(menuEditDoctorRecord);
	menubar.add(menuEditNurseRecord);
	menubar.add(menuInfoRecord);
	menubar.add(menuTransferRecord);

	
	jpanel.setLayout(new BorderLayout());
	
	JButton btnSubmit = new JButton("SUBMIT");
	jframe.add(btnSubmit,BorderLayout.SOUTH);
	jframe.setSize(new Dimension(740,480));
	jframe.setLocationRelativeTo(null);
	jframe.setResizable(false);
	jframe.add(jpanel);
	jframe.setJMenuBar(menubar);
	jframe.setVisible(true);
	jframe.setTitle(title);
	jframe.setDefaultCloseOperation(EXIT_ON_CLOSE);

	
	menuDoctorRecord.addActionListener(new ActionListener() 
	{
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
		   jpanel.removeAll();
		   jpanel.add(btnSubmit,BorderLayout.SOUTH);
		   NewDoctor doctorPanel = new NewDoctor();
		   jpanel.add(doctorPanel.panel(false));
		   jframe.add(jpanel);
		   jframe.setVisible(true);
		   conditionID =1;
		}
	});

	
	menuNurseRecord.addActionListener(new ActionListener() 
	{
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			   jpanel.removeAll();
			   jpanel.add(btnSubmit,BorderLayout.SOUTH);
			   NewNurse nursePanel = new NewNurse();
			   jpanel.add(nursePanel.panel(false));
			   jframe.add(jpanel);
			   jframe.setVisible(true);
			   conditionID =2;
		}
	});

	menuTotalRecord.addActionListener(new ActionListener() 
	{
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			   jpanel.removeAll();
			   jpanel.add(btnSubmit,BorderLayout.SOUTH);
			   TotalRecords totalrecords = new TotalRecords();
			   jpanel.add(totalrecords.panel());
			   jframe.add(jpanel);
			   jframe.setVisible(true);
			   try 
			   {			    
			   
			    totalrecords.getTotalRecord(session);
			 
			   }
			   catch (Exception e1) 
			   {
				e1.printStackTrace();
			   }
			
		}
	});
	
	menuEditDoctorRecord.addActionListener(new ActionListener() 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			   jpanel.removeAll();
			   jpanel.add(btnSubmit,BorderLayout.SOUTH);
			   EditRecords edtDoctorRecords = new EditRecords();
			   jpanel.add(edtDoctorRecords.panelDoctorID());
			  
			   jframe.add(jpanel);
			   jframe.setVisible(true);
			   conditionID = 3;
		}
	});
	
	menuEditNurseRecord.addActionListener(new ActionListener() 
	{
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			   jpanel.removeAll();
			   jpanel.add(btnSubmit,BorderLayout.SOUTH);
			   EditRecords edtNurseRecords = new EditRecords();
			   jpanel.add(edtNurseRecords.panelNurseID());
			   jframe.add(jpanel);
			   jframe.setVisible(true);
			   conditionID = 4;
		}
	});
	
	
	menuInfoRecord.addActionListener(new ActionListener() 
	{
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String records = session.getService().execute(",,"+Login.managerCode+",,4,,").trim();
			String[] myarray = records.split(",,");
			records = myarray[3];
			System.out.println(Login.managerCode+" : " +records);
		}
	});		
	
	
	
	menuTransferRecord.addActionListener(new ActionListener() 
	{
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			jpanel.removeAll();
			   jpanel.add(btnSubmit,BorderLayout.SOUTH);
			   TransferRecord transferPanel = new TransferRecord();
			   jpanel.add(transferPanel.panel(false));
			   jframe.add(jpanel);
			   jframe.setVisible(true);
			   conditionID = 8;
		}
	});
	
	btnSubmit.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
		
			if(conditionID == 1)
			{
				
				NewDoctor newdoctor = new NewDoctor();
				try {
					String records = newdoctor.DoctorCreateValid(session);
					System.out.println(Login.managerCode+" : " +records);
				} catch (Exception e1) {
				
					e1.printStackTrace();
				}
			}
			if(conditionID == 2)
			{
				NewNurse newnurse = new NewNurse();
				try {
					String records = newnurse.NurseValidCreate(session);
					System.out.println(Login.managerCode+" : " +records);
				} catch (Exception e1) {
					
					e1.printStackTrace();
				}
			}
			if(conditionID == 3)
			{
				String records = session.getService().execute(",,"+Login.managerCode+",,4,,").trim();
				String[] myarray = records.split(",,");
				records = myarray[3];
				for(int i=1;i<records.length();i+=9)
				{
					allrecords[recordindex] = records.substring(i, i+7);
					recordindex++;
				}
				
				for(int k=0;k<recordindex;k++)
				{
					if(new EditRecords().getRecordIDForDoctorEdit().equals(allrecords[k]))
					{
						jpanel.removeAll();
						jpanel.add(btnSubmit, BorderLayout.SOUTH);
						EditRecords editrecords = new EditRecords();
						jpanel.add(editrecords.doctorEditPanel());
						jframe.add(jpanel);
						jframe.setVisible(true);
						conditionID = 6;
						check = true;
					}
				}
					if(!check)
				{ 
						JOptionPane.showMessageDialog(jpanel, "Invalid ID for Edition");
				} 
			}
			
			if(conditionID == 4)
			{
				String records = session.getService().execute(",,"+Login.managerCode+",,4,,").trim();
				String[] myarray = records.split(",,");
				records = myarray[3];
				for(int i=1;i<records.length();i+=9)
				{
					allrecords[recordindex] = records.substring(i, i+7);
					recordindex++;
				}
			
				for(int k=0;k<recordindex;k++)
				{
				   if(new EditRecords().getRecordIDForNurseEdit().equals(allrecords[k]))
				   {
					   jpanel.removeAll();
					   jpanel.add(btnSubmit, BorderLayout.SOUTH);
					   EditRecords editrecords = new EditRecords();
					   jpanel.add(editrecords.nurseEditPanel());
					   jframe.add(jpanel);
					   jframe.setVisible(true);
					   check = true;
					   conditionID=7;
				}
				}
					if(!check)
				{ 
						JOptionPane.showMessageDialog(jpanel, "Invalid ID for Edition");
				} 
			}
			
			if(conditionID == 6)
			{		
				EditRecords editrecords = new EditRecords();
				String records = editrecords.editDoctor(session);
				System.out.println(Login.managerCode+" : " +records);
			}
			
			if(conditionID == 7)
			{
				EditRecords editrecords = new EditRecords();
				if(EditRecords.cmbEditField.getSelectedIndex()==1)
				{
				if(EditRecords.txtNEditField.getText().toString().equalsIgnoreCase("Junior") || EditRecords.txtNEditField.getText().toString().equalsIgnoreCase("Senior"))
				{
					String records =editrecords.editNurse(session);
					System.out.println(Login.managerCode+" : " +records);
				}
				else
				JOptionPane.showMessageDialog(null, "Invalid Designation");
				}
				
				if(EditRecords.cmbEditField.getSelectedIndex()==2)
				{
				if(EditRecords.txtNEditField.getText().toString().equalsIgnoreCase("Active") || EditRecords.txtNEditField.getText().toString().equalsIgnoreCase("Terminated"))
				{
					String records =editrecords.editNurse(session);
					System.out.println(Login.managerCode+" : " +records);
				}
				else
				JOptionPane.showMessageDialog(null, "Invalid Status");
				}
			}

			if(conditionID == 8)
			{
				TransferRecord transfer = new TransferRecord();
				try {
						String records =transfer.TransferValidCreate(session);
						System.out.println(Login.managerCode+" : " +records);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	});
}
	

	public JFrame getPanel()
	{
		return this.jframe;
	}

}
