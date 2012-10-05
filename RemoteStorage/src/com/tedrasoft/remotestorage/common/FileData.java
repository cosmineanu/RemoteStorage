package com.tedrasoft.remotestorage.common;

import java.io.InputStream;

/**
 * Keeps data of file requested for download
 * @author Dragos
 *
 */
public class FileData{

	
	InputStream is;
	long fileLength;
	String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public InputStream getIs() {
		return is;
	}
	public void setIs(InputStream is) {
		this.is = is;
	}
	public long getFileLength() {
		return fileLength;
	}
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	
	
}
