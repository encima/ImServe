package com.encima.fslistener;

import java.util.*;
import java.io.*;
import java.net.*;

public class PropertiesUtils {

	public static String getProperty(String property) {
		String value;
			try{
				Properties props = new Properties();
				FileInputStream fis = new FileInputStream("/home/linaro/projects/FileSystemListener/network.properties");
				props.load(fis);
				fis.close();
				value = props.getProperty(property);
			}catch(Exception e){
				value = null;
				e.printStackTrace();
			}
		return value;
	}

}	
