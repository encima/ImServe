package com.encima.fslistener;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;

public class ConnectionHandler {

static Runtime rt;
static Process proc;
static String wirelessInterface = null;
static String board;

	public ConnectionHandler() throws Exception{
		board = getBoardName();
		rt = Runtime.getRuntime();
		System.out.println("Running Connection Handler...");
		System.out.println("---Stopping Network Manager, if running...");
		System.out.println("---Board Name is: " + board);
		proc = rt.exec("sudo stop network-manager");
		System.out.println("---Getting name of Wireless interface");
		wirelessInterface = getWirelessInterface();
		wait(2);
			if(wirelessInterface == null) {
				System.out.println("---No Wireless Interfaces, labelled wlan, available. Connection can't be managed");
				System.exit(0);
			}else{
				System.out.println("---Wireless interface is: " + wirelessInterface);
				System.out.println("---Bringing " + wirelessInterface + " up, if it is not already...");
				proc = rt.exec("sudo ifconfig " + wirelessInterface + " up");
			}
	}
	
	public String changeNetworkState(Boolean host) throws Exception {
		String ssid = null;
			if(host) {
				System.out.println("---Creating Ad-Hoc Connection...");
				proc = rt.exec("iwconfig " + wirelessInterface + " mode ad-hoc essid " + board);
				proc = rt.exec("ifconfig " + wirelessInterface + " " + PropertiesUtils.getProperty(board));
					if(!adHocCheck()) {
						System.out.println("---Mode not changed Successfully, retrying");
						changeNetworkState(true);
					}
			}else{
				System.out.println("---Searching for viable connections...");
				Vector<String> ssids = getHighestSignalNetwork();
				int attempts = 0;
				boolean connected =false;
					while(attempts < 3 && ssids.isEmpty()) {
						System.out.println("No IGEP Nodes in range, retrying...");
						ssids = getHighestSignalNetwork();
						attempts++;
					}
				
						if(!ssids.isEmpty()) {
							System.out.println("---Connecting to " + ssids.get(0));
							proc = rt.exec("iwconfig " + wirelessInterface + " mode ad-hoc essid " + ssids.get(0));
								if(!checkConnection(ssids.get(0))) {
									changeNetworkState(false);
								}
							ssid = ssids.get(0);
						}else{
							System.out.println("No IGEP Connections in range");
						}
			}
		return ssid;
	}
	
	public static String getWirelessInterface() throws Exception {
		String wlan = null;
		/*Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			for(NetworkInterface netint : Collections.list(interfaces)) {
				System.out.println(netint.getName());
				if(netint.getName().contains("wlan")) {
					wlan = netint.getName();
				}
			}*/
		//if(wlan == null) {
			Vector<String> inter = getOutput("ifconfig -a");
				for(int i = 0; i < inter.size(); i++) {
					if(inter.get(i).contains("wlan")) {
						String face = inter.get(i);
							if(face.contains("wlan")) {
								String[] lans = face.split(" ");
								wlan = lans[0];
							}
					}
				}
		//}
		return wlan;
	}
		
	public static Vector<String> getHighestSignalNetwork() throws Exception {
		Vector<String> out = getOutput("iwlist " + wirelessInterface + " scanning");
		Vector<String> ssids = new Vector<String>();
		System.out.println("---Getting Available Networks...");
			for(int i = 0; i < out.size(); i++) {
				if(out.get(i).contains("ESSID")) {
					String ssid = out.get(i).replace("ESSID:", "");
					ssid = ssid.replace("\"", "");
					ssid = ssid.trim();
						if (ssid.contains("IGEP") && !ssid.contains(board)) {
							ssids.add(ssid);
							System.out.println("******" + ssid);
						}
				}
			}
		return ssids;
	}
	
	public static boolean adHocCheck() throws Exception {
		wait(5);
		boolean adHoc = false;
		Vector<String> out = getOutput("iwconfig " + wirelessInterface);
			for(int i = 0; i < out.size(); i++) {
				if(out.get(i).contains("Ad-Hoc")) {
					adHoc = true;							
				}
			}	
		return adHoc;
	}
	
	public static boolean checkConnection(String ssid) throws Exception {
		boolean connected = false;
		Vector<String> out = getOutput("iwconfig " + wirelessInterface);
			for(int i = 0; i < out.size(); i++) {
				if(out.get(i).contains(ssid)) {
					connected = true;							
				}
			}	
		return connected;
	}
	
	public static Vector<String> getOutput(String cmd) throws Exception {
		Process pr = rt.exec(cmd);
		String line = null;
		Vector<String> out = new Vector<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String str = null;	
			while((str = in.readLine()) != null) {
				out.add(str);
			}
		in.close();
		return out;
	}
	
	public static void wait(int period) {
		long time0, time1;
		
		time0 = System.currentTimeMillis();
		
		do{
			time1 = System.currentTimeMillis();
		}while((time1-time0) < (period*1000));
	}
	
	public static String getBoardName() {
		String board;
			try {
				board = InetAddress.getLocalHost().getHostName();
				System.out.println(board);
			}catch(Exception e) {
				e.printStackTrace();
				board = null;
			}
		return board;
	}
	
	public static void main(String[] args) {		
			try {
				ConnectionHandler ch = new ConnectionHandler();
				ch.changeNetworkState(false);
			}catch(Exception e) {
				e.printStackTrace();
			}		
	}
}

		/*if(adHocCheck()) {
			System.out.println("---Current Connection is Ad-Hoc");
			System.out.println("---Changing connection to Managed...");
			proc = rt.exec("connectManaged");
			wait(5);
				if(!adHocCheck()) {
					System.out.println("---State Successfully Changed to Managed...");
					wait(5);
					getHighestSignalNetwork(proc);
				}else{
					System.out.println("---State Change Failed, Retrying...");
					changeNetworkState();
				}
		}else{*/
			//System.out.println("---Current Connection is Managed");
			
			
			

	
	
				/*if(netint.getName().equals("wlan0")) {
					Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
						for(InetAddress address : Collections.list(inetAddresses)) {
							if(address instanceof Inet4Address) {
								ip = address.toString().substring(1);
								System.out.println(ip);
							}
						}
				}*/
