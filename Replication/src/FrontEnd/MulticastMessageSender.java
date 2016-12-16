package FrontEnd;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import shared.Config;

/**
 * This class is used to send and receive messages to replicas
 * Also it sends crash notifications
 * @author 
 *
 */
public class MulticastMessageSender {
	
	public static double sequence = 2;

	/**
	 * method for multicasting the message to all replicas
	 * @param ports 
	 * @param msg
	 * @return 
	 * @return
	 * @throws IOException 
	 * @throws SocketException 
	 * @throws ClassNotFoundException 
	 * @throws SocketTimeoutException 
	 */
	public List<String> multicastMessage(List<Integer> ports, String msg)
			throws SocketTimeoutException, ClassNotFoundException,
			SocketException, IOException {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(Config.PORT_NUMBER_FrontEnd);
			InetAddress host = InetAddress.getByName(Config.IP);
			
			final ByteArrayOutputStream baos=new ByteArrayOutputStream(); 
			final DataOutputStream daos=new DataOutputStream(baos);
			daos.writeDouble(sequence);
			daos.writeBytes(msg);
			daos.close();
			final byte[] serializedMsg=baos.toByteArray();

			for (Integer port : ports) {
				DatagramPacket request = new DatagramPacket(serializedMsg,
						serializedMsg.length, host, port);
				aSocket.send(request);

				//wait for 10ms to get the responses from replica 
				aSocket.setSoTimeout(100);
				System.out.println("message sent to port: "+port);
			}
			
			sequence++;
		} catch (SocketException e) {
			System.out.println("SOCKET:" + e.getMessage());
		} catch (Exception e) {
			System.out.println("IO:" + e.getMessage());
		}
		
		return waitForACKsAndResponses(aSocket);
	}

	/**
	 * This method wait for ACKs and actual responses
	 * @param aSocket
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private List<String> waitForACKsAndResponses(DatagramSocket aSocket) throws IOException, ClassNotFoundException {
		List<String> list = new ArrayList<String>();
		byte[] buffer = null;
		try {
			
			for (int i = 0; i < 2* Config.MAX_REPLICA; i++) {
				try {
					buffer = new byte[1024];
					DatagramPacket res = new DatagramPacket(buffer,
							buffer.length);
					aSocket.receive(res);
					System.out.println("Received:"+new String(res.getData()).trim());
					list.add(new String(res.getData()));
				} catch (SocketTimeoutException e) {
					//do nothing
				}
			}
		}finally {
			if (null != aSocket)
				aSocket.close();
		}
		return list;
		
	}
}