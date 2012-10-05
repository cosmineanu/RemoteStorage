package com.tedrasoft.remotestorage.service;


import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tedrasoft.remotestorage.common.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for exposed services
 * 
 * @author Dragos
 * 
 */

@Controller
public class RestController {

	// storage operations
	private StorageManager storageManager;
	private String relativePathSeparator;
	
	
	public String getRelativePathSeparator() {
		return relativePathSeparator;
	}

	public void setRelativePathSeparator(String relativePathSeparator) {
		this.relativePathSeparator = relativePathSeparator;
	}

	public StorageManager getStorageManager() {
		return storageManager;
	}

	public void setStorageManager(StorageManager storageManager) {
		this.storageManager = storageManager;
	}
	
	
	/**
	 * Expose list of files and folders for a certain relative path in storage
	 * @param path - relative path in storage
	 * @return FileList
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/files")
	public @ResponseBody FileList getAllFiles(@RequestParam(value = "path", required = true) String path) {

		FileList fs = new FileList(storageManager.getFileList(path));
		if (path == null)
			fs.setRelativePath(relativePathSeparator);
		else
			fs.setRelativePath(path);
		return fs;
	}

	/**
	 * Upload File in storage folder. Folder is given in path relative to storage root
	 * @param httpServletRequest
	 * @param folder
	 * @param multipartFile
	 * @return String representing boolean value
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveFile")
	public @ResponseBody String handleUpload(
			HttpServletRequest httpServletRequest,
			@RequestParam(value = "folder", required = true) String folder,
			@RequestParam(value = "file", required = false) MultipartFile multipartFile) {

		String orgName = multipartFile.getOriginalFilename();
		String relativePath = folder + relativePathSeparator + orgName;
		boolean response = storageManager.addFile(relativePath, multipartFile);
		return Boolean.toString(response);
	}
	
	/**
	 * Downloads file from storage
	 * @param relativePath
	 * @param response
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/download")
	public void handleFileDownload(
			@RequestParam(value = "relativePath", required = true) String relativePath,
			HttpServletResponse response) {
		
		FileData fd = storageManager.getFileData(relativePath);
		response.setContentType("application/octet-stream"); // 
		response.setContentLength(new Long(fd.getFileLength()).intValue());
		response.setHeader("Content-Disposition",
				"attachment; filename=" + fd.getName());
		try {
			FileCopyUtils.copy(fd.getIs(), response.getOutputStream());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return;
	}
	
	/**
	 * Searches the storage for a file with name corresponding to searchTerm. Search Term can contain wildcards *,?
	 * @param searchTerm 
	 * @return SearchList
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/searchFile", headers = "Accept=application/xml")
	public @ResponseBody SearchList searchFile(
			@RequestParam(value = "searchTerm", required = true) String searchTerm) {
		return new SearchList(storageManager.searchFiles(searchTerm));
	}
	
	/**
	 * Deletes a file or a folder from storage with specified relative path
	 * @param relativePath
	 * @return String true or false depending on the result of operation 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/deleteFile")
	public @ResponseBody String deleteFile(
			@RequestParam(value = "relativePath", required = true) String relativePath) {
		return new Boolean(storageManager.deleteFile(relativePath)).toString();
	}
	
	/**
	 * Creates a folder in storage under the given parent with the specified name
	 * @param httpServletRequest
	 * @param parent
	 * @param name
	 * @return String representing true or false depending on operation result
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/newFolder")
	public @ResponseBody String handleUpload(HttpServletRequest httpServletRequest,
			@RequestParam(value = "parent", required = true) String parent,
			@RequestParam(value = "name", required = false) String name) {

		boolean response = storageManager.addFolder(parent + relativePathSeparator	+ name);
		return Boolean.toString(response);
	}
}
