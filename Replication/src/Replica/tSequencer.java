package Replica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import shared.Config;

public class tSequencer implements Runnable{
	
	public static double s=0;
	public static int udpserverport;
	
	@Override
	public void run(){
		
		DatagramSocket mySocket = null;
		
		try
		{
			udpserverport = Config.PORT_NUMBER_seq;
			mySocket = new DatagramSocket(udpserverport);
			
			byte[] mybuffer = new byte[1000];
			
			while(true)
			{
				
				DatagramPacket myrequest = new DatagramPacket(mybuffer, mybuffer.length);
				
				mySocket.receive(myrequest);
				increase();
				DatagramPacket myreply = new DatagramPacket("sequencer has been increased,,m,,m,,m,,m,,m,,m,,m,,".getBytes(),
						"sequencer has been increased,,m,,m,,m,,m,,m,,m,,m,,".length(),myrequest.getAddress(),myrequest.getPort());
				mySocket.send(myreply);
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
	public static Double dSequence() {
		
		System.out.println("dsequence ncrease: "+s);
		return s;
	}

	public void increase() {
		s++;
		System.out.println("increase: "+s);
		
	}
	public static void cincrease()
	{
		s++;
	}
	public static void decrease(){
		s--;
	}
}