package Model;

import java.io.Serializable;

/**
 * Class representing the doctor records and the attributes
 *
 */
public class DoctorRecord extends StaffRecord implements Serializable
{
	public String Address;
	public String PhoneNumber;
	public String Specialization;
	public String Location;
	public static int dCount=0;
	public DoctorRecord(){
		dCount++;
	}
	public DoctorRecord(String clinicCode){
		if(clinicCode.equals("MTL")){
			int index = 10000 + MTLcount;
			RecordID = "DR" + Integer.toString(index);
		}
		if(clinicCode.equals("LVL")){
			int index = 10000 +  LVLcount;
			RecordID = "DR" + Integer.toString(index);
		}
		if(clinicCode.equals("DDO")){
			int index = 10000 + DDOcount;
			RecordID = "DR" + Integer.toString(index);
		}
	}	
}
