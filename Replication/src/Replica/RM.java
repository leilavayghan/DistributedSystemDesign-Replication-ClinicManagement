package Replica;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;

import Replica.ServerLeila;
import shared.Config;

public class RM implements Runnable{
	
	public int RMPort,ReplicaPort;
	public String RMName,ReplicaName;
	public String ack = "none";
	public Thread rep;
	public boolean isCrashed = false;
	public boolean isRestored = false;
	public boolean isUpdated = false;
	static Logger loggerServer = Logger.getLogger("server");
	
	/**
	 * this constructor initializes the replica. it makes 4 instances of the server.
	 * one is called Replica and is the middle man between FE and servers, the remaining 
	 * 3 are for MTL, DDO and LVL.
	 * @throws InterruptedException
	 */
	
	public RM(int code) throws InterruptedException{
		
		if (code==1){
			RMPort = Config.PORT_NUMBER_RMLeila;
			RMName = Config.CLINIC_CODE_RMLeila;
			ReplicaPort = Config.PORT_NUMBER_ReplicaLeila;
			ReplicaName = Config.CLINIC_CODE_ReplicaLeila;
			
			System.out.println("STARTING REPLICA LEILA");
			System.out.println(".\n.");
			//Replica
			//this thread is the middle man between FE and servers in MTL, DDO and LVL.
			ServerLeila.Replica = new ServerLeila(ReplicaName,ReplicaPort);
			rep = new Thread(ServerLeila.Replica);
			rep.start();
			Thread.sleep(10);
		
			//MTL
			ServerLeila.MTLServer = new ServerLeila(Config.CLINIC_CODE_MTL_Leila,Config.PORT_NUMBER_MTL_Leila);
			Thread mtl = new Thread(ServerLeila.MTLServer);
			mtl.start();
			Thread.sleep(10);
			System.out.println("Montreal Server Running");
		
			//DDO
			ServerLeila.DDOServer = new ServerLeila(Config.CLINIC_CODE_DDO_Leila,Config.PORT_NUMBER_DDO_Leila);
			Thread ddo = new Thread(ServerLeila.DDOServer);
			ddo.start();
			Thread.sleep(10);
			System.out.println("DDO Server Running");
		
			//LVL
			ServerLeila.LVLServer = new ServerLeila(Config.CLINIC_CODE_LVL_Leila,Config.PORT_NUMBER_LVL_Leila);
			Thread lvl = new Thread(ServerLeila.LVLServer);
			lvl.start();
			Thread.sleep(10);
			System.out.println("LVL Server Running");
			System.out.println("");

		}
		
		else if (code==2){
			RMPort = Config.PORT_NUMBER_RMAnanta;
			RMName = Config.CLINIC_CODE_RMAnanta;
			ReplicaPort = Config.PORT_NUMBER_ReplicaAnanta;
			ReplicaName = Config.CLINIC_CODE_ReplicaAnanta;
			
			System.out.println("STARTING REPLICA ANANTA");
			System.out.println(".\n.");
			//Replica
			//this thread is the middle man between FE and servers in MTL, DDO and LVL.
			ServerAnanta.Replica = new ServerAnanta(ReplicaName,ReplicaPort);
			rep = new Thread(ServerAnanta.Replica);
			rep.start();
			Thread.sleep(10);
		
			//MTL
			ServerAnanta.MTLServer = new ServerAnanta(Config.CLINIC_CODE_MTL_Ananta,Config.PORT_NUMBER_MTL_Ananta);
			Thread mtl = new Thread(ServerAnanta.MTLServer);
			mtl.start();
			Thread.sleep(10);
			System.out.println("Montreal Server Running");
		
			//DDO
			ServerAnanta.DDOServer = new ServerAnanta(Config.CLINIC_CODE_DDO_Ananta,Config.PORT_NUMBER_DDO_Ananta);
			Thread ddo = new Thread(ServerAnanta.DDOServer);
			ddo.start();
			Thread.sleep(10);
			System.out.println("DDO Server Running");
		
			//LVL
			ServerAnanta.LVLServer = new ServerAnanta(Config.CLINIC_CODE_LVL_Ananta,Config.PORT_NUMBER_LVL_Ananta);
			Thread lvl = new Thread(ServerAnanta.LVLServer);
			lvl.start();
			Thread.sleep(10);
			System.out.println("LVL Server Running");
			System.out.println("");

		}
		
		else if (code==3){
			RMPort = Config.PORT_NUMBER_RMMandeep;
			RMName = Config.CLINIC_CODE_RMMandeep;
			ReplicaPort = Config.PORT_NUMBER_ReplicaMandeep;
			ReplicaName = Config.CLINIC_CODE_ReplicaMandeep;
			
			System.out.println("STARTING REPLICA MANDEEP");
			System.out.println(".\n.");
			//Replica
			//this thread is the middle man between FE and servers in MTL, DDO and LVL.
			ServerMandeep.Replica = new ServerMandeep(ReplicaName,ReplicaPort);
			rep = new Thread(ServerMandeep.Replica);
			rep.start();
			Thread.sleep(10);
		
			//MTL
			ServerMandeep.MTLServer = new ServerMandeep(Config.CLINIC_CODE_MTL_Mandeep,Config.PORT_NUMBER_MTL_Mandeep);
			Thread mtl = new Thread(ServerMandeep.MTLServer);
			mtl.start();
			Thread.sleep(10);
			System.out.println("Montreal Server Running");
		
			//DDO
			ServerMandeep.DDOServer = new ServerMandeep(Config.CLINIC_CODE_DDO_Mandeep,Config.PORT_NUMBER_DDO_Mandeep);
			Thread ddo = new Thread(ServerMandeep.DDOServer);
			ddo.start();
			Thread.sleep(10);
			System.out.println("DDO Server Running");
		
			//LVL
			ServerMandeep.LVLServer = new ServerMandeep(Config.CLINIC_CODE_LVL_Mandeep,Config.PORT_NUMBER_LVL_Mandeep);
			Thread lvl = new Thread(ServerMandeep.LVLServer);
			lvl.start();
			Thread.sleep(10);
			System.out.println("LVL Server Running");
			System.out.println("");
		}
		
		else if(code==4){
			tSequencer tsec = new tSequencer();
			Thread seq = new Thread(tsec);
			seq.start();
		}
		
	}
	
	/**
	 * this method is called when the FE wants to confirm with the RM if the replica is crashed. 
	 * it gets the RM socket and communicates with the Replica. if it receives a reply, it will return false,
	 * if it gets no reply, it will return true.
	 * @param socket the socket that is opened for RM
	 * @return true: if the replica is crashed, false: if it is not crashed. 
	 */
	
	public boolean confirm(DatagramSocket socket){
		try
		{
			byte[] udpCount = "RM,,".getBytes();
			InetAddress address = InetAddress.getByName(Config.IP);
			DatagramPacket request = new DatagramPacket(udpCount, "RM,,".length(),address,ReplicaPort);
			socket.send(request);
			
			//we wait 10 seconds for the reply from Replica.
			socket.setSoTimeout(10000);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			
			try{
				socket.receive(reply);
			}
			//if we don't hear from Replica in 10 seconds, we assume it is crashed and return true.
			catch(SocketTimeoutException e){
				return true;
			}
						
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}
		
		return false;
	}
	
	/**
	 * this method crashes the replica. it is for making the test scenario
	 * @param t a thread we need to kill.
	 */
	@SuppressWarnings("deprecation")
	public static void crash(Thread t){
		t.stop();
	}
	
	/**
	 * after the crash is confirmed, this function is called to restore the replica.
	 */
	public boolean restore(){
		if(RMPort==Config.PORT_NUMBER_RMLeila){
			ServerLeila.Replica = new ServerLeila(ReplicaName,ReplicaPort);
			rep = new Thread(ServerLeila.Replica);
			rep.start();
			return true;
		}
		
		else if(RMPort==Config.PORT_NUMBER_RMAnanta){
			ServerAnanta.Replica = new ServerAnanta(ReplicaName,ReplicaPort);
			rep = new Thread(ServerAnanta.Replica);
			rep.start();
			return true;
		}
		
		else if(RMPort==Config.PORT_NUMBER_RMMandeep){
			ServerMandeep.Replica = new ServerMandeep(ReplicaName,ReplicaPort);
			rep = new Thread(ServerMandeep.Replica);
			rep.start();
			return true;
		}
		return false;
	}
	
	/**
	 * this function is called when a crash is confirmed and the RM wants to update the state of its replica. using its own socket, it communicates with a non-crashed replica
	 * @param RM the port of the non-crashed replica manager
	 * @param socket of the RM of the crashed replica
	 * @return
	 */
	public boolean stateUpdate(int RM, DatagramSocket socket){
		try
		{
			
			byte[] udpCount = "update".getBytes();
			InetAddress address = InetAddress.getByName(Config.IP);
			DatagramPacket request = new DatagramPacket(udpCount, "update".length(),address,RM);
			socket.send(request);
								
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			
			String receivedmsg=(new String(reply.getData()));
			//if the remote replica is ready to send its hashmap
			if(receivedmsg.subSequence(0, 5).equals("ready")){
				
				//the previously crashed replica is called to get ready to receive the hashmap from its input stream
				callReplica("UPDST,,",socket);
				
				//the new record count is sent to the previously crashed replica
				callReplica(receivedmsg,socket);
				return true;
			}
						
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}
		
		return false;
	}
	
	/**
	 * this function sends a msg to the replica associated with the replica manager.
	 * @param msg
	 * @param socket
	 * @return it returns what it receives back from the replica.
	 */
	public String callReplica(String msg, DatagramSocket socket){
		try
		{
			byte[] udpCount = msg.getBytes();
			InetAddress address = InetAddress.getByName(Config.IP);
			DatagramPacket request = new DatagramPacket(udpCount, msg.length(),address,ReplicaPort);
			socket.send(request);
			
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socket.receive(reply);
			return new String(reply.getData());

		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}
		return "nothing";
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		RM rmleila = new RM(1);
		RM rmAnanta = new RM(2);
		RM rmMandeep = new RM(3);
		RM seq = new RM(4);
		
		Thread t1 = new Thread(rmleila);
		Thread t2 = new Thread(rmAnanta);
		Thread t3 = new Thread(rmMandeep);
		Thread t4 = new Thread(seq);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
	}
	
	@Override
	public void run() {
				
		//we open a socket for the RM.
		DatagramSocket mySocket = null;
		try
		{
			mySocket = new DatagramSocket(RMPort);
			byte[] mybuffer = new byte[1000];
			
			while(true)
			{
				DatagramPacket myrequest = new DatagramPacket(mybuffer, mybuffer.length);
				mySocket.receive(myrequest);
				String receivedmsg=(new String(myrequest.getData()));
								
				//FE asks to crash the replica (this is for creating the test scenario)
				if (receivedmsg.substring(0, 5).equals("crash")){
					loggerServer.info(RMName+ " is asked to crash its replica for test.");
					crash(rep);
					receivedmsg="Successfully Crashed "+ReplicaName;
					loggerServer.info(RMName+ "Successfully Crashed "+ReplicaName);
				}
				
				//FE needs to confirm with the RM that the replica is crashed.
				else if (receivedmsg.substring(0, 7).equals("confirm")){
					
					loggerServer.info("FrontEnd has asked "+RMName+ " if its replica is crashed");
					if(confirm(mySocket)){
						System.out.println(ReplicaName+" is crashed for sure.");
						loggerServer.info(RMName+ " says: "+ReplicaName+" is crashed for sure.");
						isCrashed = true;
					}
					else{
						receivedmsg="nack";	
					}
					//in the case of crash, replica is restored by the RM.
					if (isCrashed){
						
						isRestored = restore();
						
						loggerServer.info(RMName+ " says: "+ ReplicaName + " is restored");
						if(RMPort==Config.PORT_NUMBER_RMLeila || RMPort==Config.PORT_NUMBER_RMMandeep)
							isUpdated = stateUpdate(Config.PORT_NUMBER_RMAnanta,mySocket);
						
						else if(RMPort==Config.PORT_NUMBER_RMAnanta)
							isUpdated = stateUpdate(Config.PORT_NUMBER_RMLeila,mySocket);
						
						if (isRestored && isUpdated){
							//we let FE know that the replica is restored.
							receivedmsg = ReplicaName+",,Restored,,";
							loggerServer.info(RMName+ " says: "+ ReplicaName + " is updated according to other replicas");
							System.out.println(receivedmsg);
						}
						isCrashed = false;
					}
				}
				
				//RM is contacted by another RM for updating the state
				else if (receivedmsg.substring(0, 6).equals("update")){
						callReplica("UPSRC,,",mySocket);
						String count = callReplica("scount,,",mySocket);
						receivedmsg = "ready,,"+count;
				}
				
				//the result is sent back to FE
				DatagramPacket myreply = new DatagramPacket(receivedmsg.getBytes(),
						receivedmsg.length(),InetAddress.getByName(Config.IP),myrequest.getPort());
				mySocket.send(myreply);
				
				//this is to remove the timeout limit.
				mySocket.setSoTimeout(999999999);
				
			}
				
			
		}
		catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {System.out.println("IO: " + e.getMessage());}
		finally
		{
			if(mySocket!=null){
				mySocket.close();
			}
		}
				
	}
}