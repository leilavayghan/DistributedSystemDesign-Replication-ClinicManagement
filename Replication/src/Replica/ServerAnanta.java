package Replica;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import Data.StaffRecordRepository;
import Model.DoctorRecord;
import Model.NurseRecord;
import Model.StaffRecord;
import shared.Config;

public class ServerAnanta implements Runnable{

	public  StaffRecordRepository StaffRecords = new StaffRecordRepository();
	public static int MTLcount = 0;
	public static int DDOcount = 0;
	public static int LVLcount = 0;
	
	public int udpserverport;
	public String currentLocation = null;
	public int currentCount = 0;
	public static ServerAnanta Replica;
	public static ServerAnanta MTLServer;
	public static ServerAnanta DDOServer;
	public static ServerAnanta LVLServer;
	static Logger loggerSystem = Logger.getLogger("dsmssystem");
	static Logger loggerServer = Logger.getLogger("server");
	public static HashMap<Double, String> hmessage = new HashMap<Double, String>();
	static Queue<Double> message=new LinkedList<Double>();
	public static Double sequence=(double) 1;
	public static Double qSequence=(double) 1;
	public static boolean wait = true;
	
	//initializing
	public ServerAnanta(String L, int P){
		this.udpserverport = P;
		this.currentLocation = L;
	}
	
	public void createDoctorRecord(String firstName, String lastName, String address, String phone,
			String specialization, String location, String managerID) {
		
		StaffRecord.MTLcount=MTLcount;
		StaffRecord.DDOcount=DDOcount;
		StaffRecord.LVLcount=LVLcount;
		
		DoctorRecord record = new DoctorRecord(location);
		record.FirstName = firstName;	
		record.LastName = lastName;
		record.Address = address;
		record.PhoneNumber = phone;
		record.Specialization = specialization;
		record.getRecordID();
		record.Location = location;
		
		
		this.StaffRecords.Add(record);
		
		
		if(location.equals("MTL")){
			MTLcount++;
			currentCount=MTLcount;
		}
		if(location.equals("DDO")){
			DDOcount++;
			currentCount=DDOcount;
		}
		if(location.equals("LVL")){
			LVLcount++;
			currentCount=LVLcount;
		}
	}

	public void createNurseRecord(String firstName, String lastName, String designation, String status, String date,
			String location, String managerID) 
	{
		StaffRecord.MTLcount=MTLcount;
		StaffRecord.DDOcount=DDOcount;
		StaffRecord.LVLcount=LVLcount;
		NurseRecord record = new NurseRecord(location);
		record.FirstName = firstName;
		record.LastName = lastName;
		record.designation = designation;
		record.status = status;
		record.statusDate =  date;
		record.getRecordID();
		record.cliniclocation = location;

		
		this.StaffRecords.Add(record);
		
		if(location.equals("MTL")){
			MTLcount++;
			currentCount=MTLcount;
		}
		if(location.equals("DDO")){
			DDOcount++;
			currentCount=DDOcount;
		}
		if(location.equals("LVL")){
			LVLcount++;
			currentCount=LVLcount;
		}
		
	}

	public void editRecord(String recordID, String fieldName, String fieldValue) 
	{
		try
		{
			this.StaffRecords.Edit(recordID, fieldName, fieldValue);
		}
		catch(Exception ex)
		{
			
		}
		
	}

	public void transferRecord(String managerID, String recordID, String remoteClinicServerName) 
	{
		int remoteport;
		if(remoteClinicServerName.equals("MTL")){
			remoteport = Config.PORT_NUMBER_MTL_Ananta;
		}
		else if(remoteClinicServerName.equals("DDO")){
			remoteport = Config.PORT_NUMBER_DDO_Ananta;
		}
		else{
			remoteport = Config.PORT_NUMBER_LVL_Ananta;
		}
		if(this.StaffRecords.findRecord(recordID)){
			String mymsg = null;
			DoctorRecord doctor;
			NurseRecord nurse;
			
			if (recordID.substring(0, 1).equals("D")){
				doctor = (DoctorRecord) this.StaffRecords.GetRecord(recordID);
				mymsg = ",,"+remoteClinicServerName+",,D" + ",," + doctor.FirstName + ",," + doctor.LastName + ",," + doctor.Address + ",," + 
						doctor.PhoneNumber + ",," + doctor.Specialization + ",," + remoteClinicServerName + 
							",," + managerID;
			}
			
			else if (recordID.substring(0, 1).equals("N")){
				nurse = (NurseRecord) this.StaffRecords.GetRecord(recordID);
				mymsg = ",,"+remoteClinicServerName+",,N" + ",," + nurse.FirstName + ",," + nurse.LastName + ",," + nurse.designation + ",," + 
						nurse.status + ",," + nurse.statusDate + ",," + remoteClinicServerName + 
							",," + managerID;
			}
			
			DatagramSocket clientSocket = null;
			try
			{
				
				byte[] udpCount = mymsg.getBytes();
				InetAddress address = InetAddress.getByName(Config.IP);
				clientSocket = new DatagramSocket();
				DatagramPacket request = new DatagramPacket(udpCount, mymsg.length(),address,remoteport);
				clientSocket.send(request);
				
			}
			catch (SocketException e){System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {System.out.println("IO: " + e.getMessage());}
			finally{
				if(clientSocket!=null)
				clientSocket.close();
			}
			
			this.StaffRecords.deleteRecord(recordID);
			if(managerID.substring(0,3).equals("MTL")){
				MTLcount--;
				currentCount=MTLcount;
			}
			else if(managerID.substring(0,3).equals("DDO")){
				DDOcount--;
				currentCount=DDOcount;
			}
			else if(managerID.substring(0,3).equals("LVL")){
				LVLcount--;
				currentCount=LVLcount;
			}
		}
		else{
			DatagramSocket clientSocket = null;
			try
			{
				
				byte[] udpCount = ",,not found,,".getBytes();
				InetAddress address = InetAddress.getByName(Config.IP);
				clientSocket = new DatagramSocket();
				DatagramPacket request = new DatagramPacket(udpCount, ",,not found,,".length(),address,remoteport);
				clientSocket.send(request);
				
			}
			catch (SocketException e){System.out.println("Socket: " + e.getMessage());
			} catch (IOException e) {System.out.println("IO: " + e.getMessage());}
			finally{
				if(clientSocket!=null)
				clientSocket.close();
			}
		}
	}
	
	public String getRecord() {
		return StaffRecords.getRecord();
	}

	/**
	 * this method is called when the server has received the request from Replica Server and wants to perform the request based on the string it got.
	 * directory:
	 * 1: to confirm a connection with the server. it works like a ping.
	 * 2: to create a doctor record.
	 * 3: to create a nurse record.
	 * 4: to get a list of records in a location.
	 * 5: to edit a record.
	 * 6: to transfer a record.
	 * 7: to get the record count in each location.
	 * D: if a doctor record is being transfered to your location and you need to add it.
	 * N: if a nurse record is being transfered to your location and you need to add it.
	 * @param server the server which the FE calls.
	 * @param s the request from FE
	 * @return an acknowledgment as a message saying that the task is done.
	 */
	public String action(ServerAnanta server, String s){
		String reply = null;
		String[] array = s.split(",,");
				switch(array[2]){
					case "1":{
						reply = "FE,,ReplicaAnanta,,1,,The Connection was Confirmed by the server in "+ server.currentLocation+",,";
						break;
					}
					case "2":{

						loggerServer.info("ReplicaAnanta Says: "+"Create Doctor Operation is Invoked at server of " + server.currentLocation + " by manager with manager-id" + array[9]);
						
						server.createDoctorRecord(array[3], array[4], array[5], array[6], array[7], server.currentLocation, array[9]);
						
						loggerSystem.info("ReplicaAnanta Says: "+"New Doctor Record is created by manager with managerID "
								 + array[9] + " in clinic location " + server.currentLocation);
						
						reply = "FE,,ReplicaAnanta,,2,,added records are: " + server.getRecord()+" and the currentcount is: " + server.currentCount+",,";
						break;
					}
					case "3":{
						
						loggerServer.info("ReplicaAnanta Says: "+"Create Nurse Operation is Invoked at server of " + server.currentLocation + " by manager with manager-id" + array[9]);
						
						server.createNurseRecord(array[3], array[4], array[5], array[6], array[7], server.currentLocation, array[9]);
						
						loggerSystem.info("ReplicaAnanta Says: "+"New Nurse Record is created by manager with managerID "
								 + array[9] + " in clinic location " + server.currentLocation);
						
						reply = "FE,,ReplicaAnanta,,3,,added records are: " + server.getRecord()+" and the currentcount is: " + server.currentCount+",,";
						break;
					}
					case "4":{
						
						loggerServer.info("ReplicaAnanta Says: "+" Record Info Operation is Invoked at server of " + server.currentLocation);
						
						reply = "FE,,ReplicaAnanta,,4,," + server.getRecord()+",,";
						break;
					}
					case "5":{
						loggerServer.info("ReplicaAnanta Says: "+"Edit Record Operation is Invoked at server of " + server.currentLocation);
						
						server.editRecord(array[3], array[4], array[5]);
						DoctorRecord doctor;
						NurseRecord nurse;
						if (array[3].substring(0, 1).equals("D")){
							doctor = (DoctorRecord) server.StaffRecords.GetRecord(array[3]);
						
							reply = "FE,,ReplicaAnanta,,5" + ",, Doctor Record with the ID of " +array[3]+ " was edited and the value of field "+ array[4]+ 
									" was successfully changed to " + array[5]+ ". The current record details are: First Name: " + doctor.FirstName + 
									" Last Name: " + doctor.LastName + " Address: " + doctor.Address + " Phone Number: " + doctor.PhoneNumber + " Specialization: " + 
									doctor.Specialization + ",,";
							
							loggerSystem.info("ReplicaAnanta Says: "+",, Doctor Record with the ID of " +array[3]+ " was edited and the value of field "+ array[4]+ 
									" was successfully changed to " + array[5]+ ". The current record details are: First Name: " + doctor.FirstName + 
									" Last Name: " + doctor.LastName + " Address: " + doctor.Address + " Phone Number: " + doctor.PhoneNumber + " Specialization: " + 
									doctor.Specialization + ",,");
						}
						
						if (array[3].substring(0, 1).equals("N")){
							nurse = (NurseRecord) server.StaffRecords.GetRecord(array[3]);
							
							reply = "FE,,ReplicaAnanta,,5" + ",, Nurse Record with the ID of " +array[3]+ " was edited and the value of field "+ array[4]+ 
									" was successfully changed to "	+ array[5]+ ". The current record details are: First Name: " + nurse.FirstName + 
									" Last Name: " + nurse.LastName + " Designation: " + nurse.designation	+ " Status: " + nurse.status + " Status Date: " + 
									nurse.statusDate + ",,";
							
							loggerSystem.info("ReplicaAnanta Says: "+",, Nurse Record with the ID of " +array[3]+ " was edited and the value of field "+ array[4]+ 
									" was successfully changed to "	+ array[5]+ ". The current record details are: First Name: " + nurse.FirstName + 
									" Last Name: " + nurse.LastName + " Designation: " + nurse.designation	+ " Status: " + nurse.status + " Status Date: " + 
									nurse.statusDate + ",,");
						}
						
						break;
					}
					case "6":{
						loggerServer.info("ReplicaAnanta Says: "+"Edit Record Operation is Invoked at server of " + server.currentLocation);
						
						server.transferRecord(array[5], array[3], array[4]);
						
						reply = "FE,,ReplicaAnanta,,6,,The record with ID: "+array[3]+" is being transfered from "+server.currentLocation+" to " + array[4]+",,";
						
						loggerSystem.info("ReplicaAnanta Says: "+"The record with ID: "+array[3]+" is being transfered from "+server.currentLocation+" to " + array[4]+",,");
						break;
					}
					case "7":{
						loggerServer.info("ReplicaAnanta Says: "+"Total Record Operation is Invoked at server of " + server.currentLocation);
						
						reply = "FE,,ReplicaAnanta,,7,," + MTLcount+",," + 
								DDOcount+",," + LVLcount+",,";
						
						loggerSystem.info("ReplicaAnanta Says: "+"MTLCount: "+MTLcount+" DDOCount: " + 
								DDOcount+" LVLCount: " + LVLcount+",,");
						
						break;
					}
					case "D":{
						
						loggerServer.info("ReplicaAnanta Says: "+"Create Doctor Operation is Invoked at server of " + server.currentLocation + " by manager with manager-id" + array[9]);
						
						server.createDoctorRecord(array[3], array[4],array[5],array[6],
								array[7],server.currentLocation,array[9]);
						
						reply = "FE,,ReplicaAnanta,,8,,added records are: " + server.getRecord()+" and the currentcount is: " + server.currentCount+",,";

						loggerSystem.info("ReplicaAnanta Says: "+server.currentLocation+":"+"added records are: " + server.getRecord()+" and the currentcount is: " + server.currentCount+",,");
						break;
					}
					case "N":{
						loggerServer.info("ReplicaAnanta Says: "+"Create Nurse Operation is Invoked at server of " + server.currentLocation + " by manager with manager-id" + array[9]);
						
						server.createNurseRecord(array[3], array[4],array[5],array[6],
								array[7],server.currentLocation,array[9]);
						
						reply = "FE,,ReplicaAnanta,,8,,added records are: " + server.getRecord()+" and the currentcount is: " + server.currentCount+",,";
						
						loggerSystem.info("ReplicaAnanta Says: "+server.currentLocation+":"+"added records are: " + server.getRecord()+" and the currentcount is: " + server.currentCount+",,");
						break;
					}
				
		}
				return reply;
	}
	
	/**
	 * a server gets an updated hashmap from its input stream using this function and updates its own hashmap.
	 * @param port port of the stream it is receiving the hashmap from.
	 */
	@SuppressWarnings("unchecked")
	public void updateHashMap(int port){
		try {
			
			Socket clientSocket = new Socket(Config.IP, port);
			
				InputStream myinput = clientSocket.getInputStream();
				ObjectInputStream input = new ObjectInputStream(myinput);
			
				if(port==Config.MTL_UpDate)
					MTLServer.StaffRecords.setHashMap(input.readObject());
				else if(port==Config.DDO_UpDate)
					DDOServer.StaffRecords.setHashMap(input.readObject());
				else if(port==Config.LVL_UpDate)
					LVLServer.StaffRecords.setHashMap(input.readObject());
				else if(port==Config.SEQ_UpDate)
					hmessage = (HashMap<Double, String>) input.readObject();
				else if(port==Config.Q_UpDate)
					message = (Queue<Double>) input.readObject();			
				myinput.close();
				clientSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * this method checks to see whether the received message is in the correct order or not.
	 * @param d
	 * @param msg
	 * @param mySocket
	 * @throws IOException
	 */
	private static void checkSequence(double d, String msg, DatagramSocket mySocket) throws IOException {
		sequence=tSequencer.dSequence();
		if(d==sequence+1)
		{
			processMsg(msg,mySocket);
			sequence++;
			double check=0;
			System.out.println("Sequence: "+sequence);
			System.out.println("output: "+d);
			wait=true;
			qSequence=sequence;
			java.util.Iterator<Double> it=message.iterator();
			while(message.size()>0 && qSequence!=0)
			{
				int qSize=message.size();
				int hSize=hmessage.size();
				System.out.println("size of hashmap: " +hSize);
				
				for(int i=0;i<hSize;i++)
				{
					check=(double) it.next();
					if(check==qSequence+1)
					{
						
						String msg1=hmessage.get(check);
						System.out.println("map element: "+msg1);
						processMsg(msg1,mySocket);
						message.remove(check);
						hmessage.remove(check);
						sequence++;
						qSequence=check+1;
						it=message.iterator();
					}
					else
					{
						wait=false;
					}
				}
				it=message.iterator();
				qSequence--;
			}
		}
		else
		{
			System.out.println("output is out of order");
			processMsg("OUT,,output is out of oder,,m,,m,,m,,m,,m,,",mySocket);
			hmessage.put(d,msg); //hashmap
			message.offer(d); //queue
			System.out.println("msg enter to hashmap: "+hmessage.get(d));
		}
	}
	
	/**
	 * this function sends the message to the correct servers, if we are not in a correct order we send an error message to the Front End.
	 * @param msg
	 * @param mySocket
	 * @throws IOException
	 */
	public static void processMsg(String msg, DatagramSocket mySocket) throws IOException{
		
		int myport=0;
		 String receivedmsg=msg;
		 String[] receivedarray = receivedmsg.split(",,");
	        
				if(receivedarray[1].equals("MTL")){
					myport=Config.PORT_NUMBER_MTL_Ananta;
				}
				else if(receivedarray[1].equals("DDO")){
					
					myport=Config.PORT_NUMBER_DDO_Ananta;
				}
				else if(receivedarray[1].equals("LVL")){
					myport=Config.PORT_NUMBER_LVL_Ananta;
				}
				else{
					myport = Config.PORT_NUMBER_FrontEnd;
				}
				
				//the message is sent to the chosen destination.
				DatagramPacket myreply = new DatagramPacket(receivedmsg.getBytes(),
						receivedmsg.length(),InetAddress.getByName(Config.IP),myport);
				mySocket.send(myreply);
				System.out.println("message for processing: "+msg);
	}
	
	@Override
	public void run() {
		
		//we open a socket.
		DatagramSocket mySocket = null;
		
		try
		{
			mySocket = new DatagramSocket(udpserverport);
			while(true)
			{
				
				int myport=0; //this variable determines where the message should be directed.
				
				//if it is the Replica:
				if(this.udpserverport == Config.PORT_NUMBER_ReplicaAnanta){
					byte[] mybufferMULTI = new byte[1000];
					DatagramPacket myrequestMULTI = new DatagramPacket(mybufferMULTI, mybufferMULTI.length);
					mySocket.receive(myrequestMULTI);
										
					//if the FE is requesting a method invocation
					if(myrequestMULTI.getPort()==Config.PORT_NUMBER_FrontEnd){
						
							final ByteArrayInputStream bais=new ByteArrayInputStream(mybufferMULTI);
							final DataInputStream dais=new DataInputStream(bais);
							final double d=dais.readDouble();
							final String msg=dais.readLine();
							System.out.println(d);
							System.out.println(msg);
							checkSequence(d,msg,mySocket);
				 
					}
					
					else{
					
						String receivedmsg=(new String(myrequestMULTI.getData()));
						String[] receivedarray = receivedmsg.split(",,");
						
						//if the message is for FE (reply from server after doing the execution)
						if(receivedarray[0].equals("FE")){
							
							myport=Config.PORT_NUMBER_FrontEnd;
						}
					
						//if the message is from RM (the case of crash confirmation)
						else if(receivedarray[0].equals("RM")){
						
							myport=Config.PORT_NUMBER_RMAnanta;
						}
					
						//if the message is from RM (the case of updating the state, the replica is sending its hashmap)
						else if(receivedarray[0].equals("UPSRC")){
						
							State mtlState = new State(MTLServer.StaffRecords.getHashMap(),Config.MTL_UpDate);
							State ddoState = new State(DDOServer.StaffRecords.getHashMap(),Config.DDO_UpDate);
							State lvlState = new State(LVLServer.StaffRecords.getHashMap(),Config.LVL_UpDate);
							State seq = new State(hmessage,Config.SEQ_UpDate);
							State que = new State(message,Config.Q_UpDate);

							Thread s1 = new Thread(mtlState);
							Thread s2 = new Thread(ddoState);
							Thread s3 = new Thread(lvlState);
							Thread sq1 = new Thread(seq);
							Thread sq2 = new Thread(que);
						
							s1.start();
							s2.start();
							s3.start();
							sq1.start();
							sq2.start();
						
							myport=myrequestMULTI.getPort();
						}
					
						//if the message is from RM (the case of updating the state, the replica is receiving a hashmap)
						else if(receivedarray[0].equals("UPDST")){
							
							updateHashMap(Config.MTL_UpDate);
							updateHashMap(Config.DDO_UpDate);
							updateHashMap(Config.LVL_UpDate);
							updateHashMap(Config.SEQ_UpDate);
							updateHashMap(Config.Q_UpDate);
							myport=myrequestMULTI.getPort();
						}
					
						//if the message is from another replica (the case of sending the record count for update)
						else if(receivedarray[0].equals("scount")){
						
							receivedmsg = "gcount,,"+MTLcount+",,"+DDOcount+",,"+LVLcount+",,";
							myport = myrequestMULTI.getPort();
						}
					
						//if the message is from another replica (the case of updating record count based on the received string from another replica)
						else if(receivedmsg.substring(0, 13).equals("ready,,gcount")){
						
							String[] c = receivedmsg.split(",,");
							System.out.println(c[2]+c[3]+c[4]);
							MTLcount = Integer.parseInt(c[2]);
							DDOcount = Integer.parseInt(c[3]);
							LVLcount = Integer.parseInt(c[4]);
							myport = myrequestMULTI.getPort();
						}
						
						//the message is sent to the chosen destination.
						DatagramPacket myreply = new DatagramPacket(receivedmsg.getBytes(),
								receivedmsg.length(),InetAddress.getByName(Config.IP),myport);
						mySocket.send(myreply);
		
					}
					
				}
				
				//if it is a server in either MTL, DDO or LVL.
				else{
					
					byte[] mybuffer = new byte[1000];
					DatagramPacket myrequest = new DatagramPacket(mybuffer, mybuffer.length);
					mySocket.receive(myrequest);
					String receivedmsg=(new String(myrequest.getData()));
					String[] receivedarray = receivedmsg.split(",,");
					String servereply = "";
					
					//we decide which server is called and we call the action function based on that.
					if(receivedarray[1].equals("MTL")){
						servereply = action(MTLServer,receivedmsg);
					}
					else if(receivedarray[1].equals("DDO")){
						servereply = action(DDOServer,receivedmsg);
					}
					else if(receivedarray[1].equals("LVL")){
						servereply = action(LVLServer,receivedmsg);
					}
					
					//we send back the result to Replica. it then redirects it to FE.
					DatagramPacket myreply = new DatagramPacket(servereply.getBytes(),
							servereply.length(),InetAddress.getByName(Config.IP),Config.PORT_NUMBER_ReplicaAnanta);
					mySocket.send(myreply);
					receivedmsg = null;
				}
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
