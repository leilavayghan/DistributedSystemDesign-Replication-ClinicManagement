package Replica;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * this class is for running threads of outputstream in order to send a hashmap to another replica
 * @author Leila
 *
 */
public class State implements Runnable{
	
	public Object obj;
	public int serverport;
	public State(Object o,int port){
		this.obj = o;
		this.serverport = port;
	}
	
	@Override
	public void run() {
		try {
			
			ServerSocket server = new ServerSocket(serverport);
			Socket servSocket = server.accept();
			
			OutputStream out = servSocket.getOutputStream();
			ObjectOutputStream output = new ObjectOutputStream(out);
			output.writeObject(obj);
			
			out.close();
			servSocket.close();
			server.close();
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
