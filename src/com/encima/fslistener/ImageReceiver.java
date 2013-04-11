package com.encima.fslistener;
/**
 * @author christophergwilliams
 */
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.Socket;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;

public class ImageReceiver implements Runnable {

	Socket s;
	int count = 0;
	
	public ImageReceiver(Socket socket) {
		s = socket;
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		
		try {
			BufferedInputStream ois = new BufferedInputStream(s.getInputStream());
			BufferedImage in = ImageIO.read(ois);

			Long time = System.currentTimeMillis();
			DateFormat df = new SimpleDateFormat("HHmmss");
			Date date = new Date(time);
			String now = df.format(date);

			FileOutputStream fos = new FileOutputStream("/home/linaro/projects/FileSystemListener/Received/image" + now + ".jpg");
			System.out.println("Image received from IGEP at: " + time);
		
			ImageIO.write(in, "jpg", fos);		
			fos.flush();
			
			System.out.println("Image Receieved from an IGEP board at: " + now);
			/*JFrame imgFrame = new JFrame("Image Received From IGEP at: " + now);
			ImageIcon ii = new ImageIcon("/home/linaro/projects/FileSystemListener/Received/image" + now + ".jpg");
			JLabel imgLabel = new JLabel(ii);
			imgFrame.add(imgLabel);
			imgFrame.setSize(400,400);
			imgFrame.setVisible(true);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
	


