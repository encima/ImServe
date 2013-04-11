package com.encima.fslistener;

import java.io.File;
import java.util.Vector;

public class FileSystemListener extends Thread {
	
	File dirAtStart; 
	ImageFilter imf = new ImageFilter();
	File[] initialFiles;
	File dir;
	File[] currentFileNames;
	Vector<File> removedFiles = new Vector<File>();
	Vector<File> addedFiles  = new Vector<File>();
	int fileDiff;
	Boolean run = true;
	String path = null;
	
	public FileSystemListener(String path) {
		this.path = path;
		dirAtStart = new File(path);
		File[] sendFirst = dirAtStart.listFiles(imf);
		System.out.println("+++++" + sendFirst.length); 
                	try {
						if(sendFirst.length != 0) {
							ImageClient ic = new ImageClient(sendFirst);
						}
	                } catch (Exception e) {
        	                e.printStackTrace();
                	}
	}
	
	public void run() {
		System.out.println("Starting File Listener....");
		initialFiles = dirAtStart.listFiles(imf);

		System.out.println("Snapshot of Images Directory: ");	
		
			for(int i = 0; i < initialFiles.length; i++) {
				System.out.println(initialFiles[i]);
			}
		
		getSubDirectories(initialFiles);
		
		while(true) {
			try {
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//System.out.println("Checking for changes...");
			getFiles(path);
			if(initialFiles.length != currentFileNames.length) {
				compareFiles();
			}
		}
	}
	
	public void getFiles(String dirString) {
		dir = new File(dirString);
		currentFileNames = dir.listFiles(imf);
	}
	
	public void compareFiles() {	
		if(initialFiles.length > currentFileNames.length) {
			filesRemoved();
		}else if(initialFiles.length < currentFileNames.length) {
			filesAdded();
		}
	}
	
	public void filesRemoved() {
		removedFiles.clear();
		System.out.println("Files Removed");
		fileDiff = initialFiles.length - currentFileNames.length;
			for(int i = currentFileNames.length; i < initialFiles.length; i++) {
				removedFiles.add(initialFiles[i]);
				System.out.println(initialFiles[i]);
			}
		initialFiles = dirAtStart.listFiles(imf);
	}
	
	public void filesAdded() {
		addedFiles.clear();
		System.out.println("Files Added");
		fileDiff = currentFileNames.length - initialFiles.length;
			for(int i = initialFiles.length; i < currentFileNames.length; i++) {
					addedFiles.add(currentFileNames[i]);
					System.out.println(i);
			}
		initialFiles = dirAtStart.listFiles(imf);
		try {
			ImageClient ic = new ImageClient(addedFiles);
			System.out.println("No. of Changed Files: " + addedFiles.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getSubDirectories(File[] fileArray) {
	
		Vector<File> subDirVector = new Vector<File>();
	
			for(int i=0; i<fileArray.length;i++) {
				if(fileArray[i].isDirectory()) {
					subDirVector.add(fileArray[i]);
					System.out.println("SubDirectory "+ i +" is: " + fileArray[i]);
				}
			}
				if(subDirVector.isEmpty()) {
					System.out.println("No Subdirectories");
				}
	}
}
