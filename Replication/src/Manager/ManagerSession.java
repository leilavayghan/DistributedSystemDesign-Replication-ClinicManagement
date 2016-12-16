package Manager;


import CORBA.FrontEnd;

/**
 * Class containing the necessary variables for creating a session by manager or client
 */
public class ManagerSession{

	private String managerID;
	public FrontEnd service;
	/**
	 * Getting client service based on manager id and clinic code
	 * @param managerID
	 * @throws Exception
	 */
	public ManagerSession(String managerID, FrontEnd ref) throws Exception
	{
		this.managerID = managerID;
		this.service = ref;
	}
	
	public ManagerSession(String managerID) 
	{
		this.managerID = managerID;
		
	}
	/**
	 *getting manager id
	 * @return
	 */
	public String getManagerID()
	{
		return managerID;
	}
	/**
	 * Gettin instance of DSMSCorba
	 * @return
	 */
	public FrontEnd getService()
	{
		return service;
	}
	
}
