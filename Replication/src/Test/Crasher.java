package Test;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import CORBA.*;
import shared.Config;

public class Crasher {

	public static FrontEnd FE;
	
	public static void main(String[] args){
		String finalResult = null;
		try 
		{
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialPort", "1050");
			ORB orb = ORB.init(args, props);
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			FrontEnd dsmsRef = (FrontEnd)FrontEndHelper.narrow(ncRef.resolve_str("FE"));
			FE = dsmsRef;	
			
			finalResult=FE.execute(Config.PORT_NUMBER_RMLeila+",,crash,,");
			
		
			System.out.println(finalResult);
		
		}
		catch (NotFound ee) 
		{
		ee.printStackTrace();
		} 
		catch (CannotProceed ee) 
		{
		ee.printStackTrace();
		} 
		catch (org.omg.CosNaming.NamingContextPackage.InvalidName ee) 
		{
		ee.printStackTrace();
		}
		catch (InvalidName ee) 
		{
		ee.printStackTrace();
		}
		
	}
}
