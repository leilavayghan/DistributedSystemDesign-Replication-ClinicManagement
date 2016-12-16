package FrontEnd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import CORBA.*;
import FrontEnd.MulticastMessageSender;
import shared.Config;


public class MyFrontEnd extends FrontEndPOA{

	private ORB orb;
	public static int serverport = 0;
	public static String str = null;
	public static Scanner in = new Scanner(System.in);
	
	private static MulticastMessageSender sender = new MulticastMessageSender();
	private static TestMulticastMessageSender testsender = new TestMulticastMessageSender();
	private static final List<Integer> Allports = Arrays.asList(Config.PORT_NUMBER_seq,
			Config.PORT_NUMBER_ReplicaLeila, Config.PORT_NUMBER_ReplicaAnanta,
			Config.PORT_NUMBER_ReplicaMandeep);
	
	public void setORB(ORB orb_val)
	{
		orb = orb_val;
	}
	
	/**
	 * sends a message to a chosen destination by UDP
	 * @param destination
	 * @param msg
	 * @return
	 */
	public String communicate(int destination,String msg){
		
		DatagramSocket clientSocket = null;
		try
		{
			clientSocket = new DatagramSocket(Config.PORT_NUMBER_FrontEnd);
			byte[] udpCount = (msg).getBytes();
			InetAddress address = InetAddress.getByName(Config.IP);
			DatagramPacket request = new DatagramPacket(udpCount, (msg).length(),address,destination);
			clientSocket.send(request);
						
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			
			clientSocket.receive(reply);			
			return (new String(reply.getData()));
			
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {System.out.println("IO: o" + e.getMessage());}
		finally
		{
			if(clientSocket!=null)
			clientSocket.close();
		}
		return "oops in communicate";
	}
	
	/**
	 * detects an absent replica
	 * @param mylist
	 * @return
	 */
	public String detect(List<String> mylist){
		
		String[] p1 = mylist.get(0).split(",,");
		String[] p2 = mylist.get(1).split(",,");
		String[] p3 = mylist.get(2).split(",,");
		
		List<String> present=Arrays.asList(p1[1],p2[1],p3[1]);
		
		if (!present.contains(Config.CLINIC_CODE_ReplicaLeila))
			return Config.CLINIC_CODE_ReplicaLeila;
		else if (!present.contains(Config.CLINIC_CODE_ReplicaAnanta))
			return Config.CLINIC_CODE_ReplicaAnanta;
		else
			return Config.CLINIC_CODE_ReplicaMandeep;
		
	}
	
	/**
	 * finds the port number of the absent replica
	 * @param detected
	 * @return
	 */
	private int findRM(String detected) {
		if(detected.equals(Config.CLINIC_CODE_ReplicaLeila))
			return Config.PORT_NUMBER_RMLeila;
		else if(detected.equals(Config.CLINIC_CODE_ReplicaAnanta))
			return Config.PORT_NUMBER_RMAnanta;
		else
			return Config.PORT_NUMBER_RMMandeep;
		
	}
	
	/**
	 * main function that the client uses to ask for server methods.
	 */
	@Override
	public String execute(String request){
		
			String[] temparray = request.split(",,");
			
			if(temparray[1].equals("crash")){
				return communicate(Integer.valueOf(temparray[0]),"crash");
			}
			else{
				List<String> received;
				try {
						
						received = sender.multicastMessage(Allports,request);
						//received = testsender.multicastMessage(Allports,request);
						if(received.size()>3){
								return makeResult(received);
							}
						else{
							System.out.println("Possible Crash in DSMS");
							System.out.println("the " + detect(received) + " seems to be crashed!");
							communicate(findRM(detect(received)),"confirm");
							return "System Restored";
						}
				}
				catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
					return "oooops in execute";
				}
	}
	
	/**
	 * chooses the correct and single result that should be sent to client
	 * @param received
	 * @return
	 */
	public String makeResult(List<String> received){
		
		if(received.get(0).substring(0, 1).equals("F"))
			return (received.get(0)).trim();
		else if (received.get(1).substring(0, 1).equals("F")){
			return (received.get(1)).trim();
		}
		else
			return (received.get(2)).trim();
		
	}
	
	public static void main(String[] args) throws SocketTimeoutException, ClassNotFoundException, SocketException, IOException {
	
		try 
		{	
				
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialPort", "1050");
			ORB orb = ORB.init(args, props);
			
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
		
			MyFrontEnd myFE = new MyFrontEnd();
			myFE.setORB(orb);
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(myFE);
			FrontEnd FE = FrontEndHelper.narrow(ref);
		
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			NameComponent path[] = ncRef.to_name("FE");
			ncRef.rebind(path,FE);	
		
			for(;;)
			{
				orb.run();
			}
		}
		catch (NotFound e) 
		{
			e.printStackTrace();
		} 
		catch (CannotProceed e) 
		{
			e.printStackTrace();
		}
		catch (InvalidName e) 
		{
			e.printStackTrace();
		}
		catch (AdapterInactive e) 
		{
			e.printStackTrace();
		}
		catch (ServantNotActive e) 
		{
			e.printStackTrace();
		}
		catch (WrongPolicy e) 
		{
			e.printStackTrace();
		}
		catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) 
		{
			e.printStackTrace();
		}
	
	}

}
