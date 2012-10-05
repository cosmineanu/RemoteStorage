package com.tedrasoft.remotestorage.common;

import java.io.File;
/**
 * Utility class for storage operations
 * @author Dragos
 *
 */
public class StorageUtils {
	private String relativePathSeparator;
	
	
	public StorageUtils() {
		super();
	}
	
	/**
	 * Converts a relative path to file system format
	 * @param relativePath
	 * @return converted path
	 */
	public String convertToFileSystemPath(String relativePath){
		if(relativePathSeparator.equalsIgnoreCase(File.separator)) {
			return relativePath;
		}else{
			return relativePath.replaceAll(relativePathSeparator, File.separator);
		}
	}
	
	/**
	 * Converts a file system path to storage format
	 * @param relativePath
	 * @return converted path
	 */
	public String convertToRelativePath(String relativePath){
		if(relativePathSeparator.equalsIgnoreCase(File.separator)) {
			return relativePath;
		}else{
			return relativePath.replaceAll(File.separator,relativePathSeparator);
		}
	}
	public String getRelativePathSeparator() {
		return relativePathSeparator;
	}
	public void setRelativePathSeparator(String relativePathSeparator) {
		this.relativePathSeparator = relativePathSeparator;
	}

}
