package com.encima.fslistener;

import java.io.*;
import java.util.*;
import java.text.*;

public class FSLRunner {
	
	public static void main(String[] args) {
			try{
				String dir = "/Users/encima/Dropbox/Projects/PhD/Java/FileSystemListener/Logs/";
				int numFiles = new File(dir).listFiles().length;

				long date = System.currentTimeMillis();
				DateFormat df = new SimpleDateFormat("ddMMyyyy");
				Date today = new Date(date);
				String now = df.format(today);

				System.setOut(new PrintStream(new FileOutputStream(dir + "FSLOG-" + now + "-" + numFiles++ + ".txt")));
				ConnectionHandler ch = new ConnectionHandler();
				ch.changeNetworkState(true);
			}catch(Exception e){
				e.printStackTrace();
			}
		String path = args[0];//"/Users/encima/Dropbox/Projects/PhD/Java/FileSystemListener/Images/";
		FileSystemListener fsl = new FileSystemListener(path);
		fsl.start();
		System.out.println("----------");
		ImageServer is = new ImageServer(4440);
		is.start();
	}

}
