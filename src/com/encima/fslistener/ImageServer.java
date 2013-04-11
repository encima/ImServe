package com.encima.fslistener;
/**
 * @author christophergwilliams
 */
import java.io.*;
import java.net.*;

public class ImageServer extends Thread{

	ServerSocket ss;
	Socket s;
	ObjectOutputStream oos;
	int port = 4440;
	
	public ImageServer(int rPort) {
		port  = rPort;
	}

	
	public void run() {
		try {
			ss  = new ServerSocket(port);
			System.out.println("Server started on Port: " + port);
		} catch(IOException e) {
			System.out.println("Server: Port-" + port  + " not available, exiting.");
			System.exit(0);
		}
		
		System.out.println("Server: Waiting for Client Connection...");
		
		while(true) {
			try {
				s = ss.accept();
				new ImageReceiver(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
