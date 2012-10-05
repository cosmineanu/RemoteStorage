package com.tedrasoft.remotestorage.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tedrasoft.remotestorage.common.FileData;
/**
 * Interface for storage operations. Can be implemented for file system, database, document management system etc 
 * @author Dragos
 */
public interface StorageManager {
	/**
	 * Retrieves all files and folders from current folder and returns a list of file names
	 * @param currentFolder
	 * @return List of file names
	 */
	public List<String> getFileList(String currentFolder);
	
	/**
	 * Searches for files corresponding to search term. Serch Term can contain wildcards *,?
	 * @param searchTerm
	 * @return List of file paths relative to storage root
	 */
	public List<String> searchFiles(String searchTerm);
	/**
	 * Creates a file with the given file path
	 * @param filePath
	 * @param file
	 * @return true if operation was a success false if failed 
	 */
	public boolean addFile(String filePath,MultipartFile file);
	/**
	 * Creates a folder with the given path
	 * @param folderPath
	 * @return true if operation was a success false if failed 
	 */
	public boolean addFolder(String folderPath);
	/**
	 * Deletes a file or a folder with the given relative path
	 * @param filePath
	 * @return true if operation was a success false if failed 
	 */
	public boolean deleteFile(String filePath);
	/**
	 * Returns a file for downloading - input stream , length, name
	 * @param path
	 * @return
	 */
	public FileData getFileData(String path);
}
