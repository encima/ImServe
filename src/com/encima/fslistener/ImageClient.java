package com.encima.fslistener;
/**
 * @author christophergwilliams
 */
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

public class ImageClient {

	Socket s;
	String ip, network;
	int port = 4440;
	OutputStream os;
	
	public ImageClient(Vector<File> files) throws IOException, ClassNotFoundException, InterruptedException, Exception {
		ConnectionHandler ch = new ConnectionHandler();
		network = ch.changeNetworkState(false);	
			if(network != null) {
				ip = PropertiesUtils.getProperty(network);
				ch.wait(2);
					for(int i = 0; i<files.size(); i++) {
						int attempt = 0;
						System.out.println("File Size: " + files.get(i).length()/1000 + "Kb");
						ImageSender is = new ImageSender(s, ip, port, files.get(i));
							while(!is.sendImage() && attempt < 3) {
								is.sendImage();
								attempt++;
							}
					}
				ch.changeNetworkState(true);
			}else{
				System.out.println("No connection available, delaying sending");
			}
	}
	
	public ImageClient(File[] files) throws IOException, ClassNotFoundException, InterruptedException, Exception {
		ConnectionHandler ch = new ConnectionHandler();
		network = ch.changeNetworkState(false);	
			if(network != null) {
				ip = PropertiesUtils.getProperty(network);
				ch.wait(2);
					for(int i = 0; i<files.length; i++) {
						int attempt = 0;
						System.out.println("File Size: " + files[i].length()/1000 + "Kb");
						ImageSender is = new ImageSender(s, ip, port, files[i]);
							while(!is.sendImage() && attempt < 3) {
								is.sendImage();
								attempt++;
							}
					}
				ch.changeNetworkState(true);
			}else{
				System.out.println("No connection available, delaying sending");
			}
	}
}
