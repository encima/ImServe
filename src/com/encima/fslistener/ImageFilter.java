package com.encima.fslistener;

import java.io.*;

public class ImageFilter implements FileFilter {

	String fileExt = "jpg";
	
	@Override
	public boolean accept(File file) {
		
		if(file.getName().toLowerCase().endsWith(fileExt) && file.length() != 0) {
			return true;
		}
		
		return false;
	}

}
