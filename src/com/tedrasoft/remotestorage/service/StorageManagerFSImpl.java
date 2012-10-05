package com.tedrasoft.remotestorage.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.tedrasoft.remotestorage.common.FileData;
import com.tedrasoft.remotestorage.common.StorageUtils;
/**
 * Implementation of @StorageManager for files system storage
 * @author Dragos
 *
 */
public class StorageManagerFSImpl implements StorageManager {

	private String storageRootPath;
	private StorageUtils utils;

	private Logger log = Logger.getLogger(this.getClass());

	public String getStorageRootPath() {
		return storageRootPath;
	}

	public void setStorageRootPath(String storageRootPath) {
		this.storageRootPath = storageRootPath;
	}

	@Override
	public List<String> getFileList(String currentFolder) {
		String realPath = storageRootPath;
		if (currentFolder != null) {
			realPath = realPath + utils.convertToFileSystemPath(currentFolder);
		}
		ArrayList<String> fileList = new ArrayList<String>();
		File rootFile = new File(realPath);
		String[] fileNames = rootFile.list();
		if (fileNames != null && fileNames.length > 0) {
			for (String name : fileNames) {
				fileList.add(name);
			}
		}
		return fileList;
	}

	@Override
	public List<String> searchFiles(String searchTerm) {
		IOFileFilter fileFilterS = new WildcardFileFilter(searchTerm);
		Collection<File> res = FileUtils.listFiles(new File(storageRootPath),
				fileFilterS, TrueFileFilter.TRUE);
		Iterator<File> it = res.iterator();
		List<String> lst = new ArrayList<String>();

		File temp = null;
		while (it.hasNext()) {
			temp = (File) it.next();
			lst.add(utils.convertToRelativePath(temp.getAbsolutePath().substring(storageRootPath.length())));
		}
		return lst;
	}

	@Override
	public boolean addFile(String filePath, MultipartFile multipartFile) {
		File dest = new File(storageRootPath + utils.convertToFileSystemPath(filePath));
		try {
			multipartFile.transferTo(dest);
		} catch (IllegalStateException e) {
			log.error(e);
			//e.printStackTrace();
			return false;
		} catch (IOException e) {
			log.error(e);
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteFile(String filePath) {
		File f = new File(storageRootPath + utils.convertToFileSystemPath(filePath));
		return f.delete();
	}

	@Override
	public FileData getFileData(String path) {
		File file = new File(storageRootPath + utils.convertToFileSystemPath(path));
		FileData fd = new FileData();
		try {
			fd.setIs(new FileInputStream(file));
			fd.setFileLength(file.length());
			fd.setName(file.getName());
		} catch (FileNotFoundException e) {
			log.error(e);
			//e.printStackTrace();
			return null;
		}
		return fd;
	}

	@Override
	public boolean addFolder(String folderPath) {
		File f = new File(storageRootPath + folderPath);
		return f.mkdir();
	}

	public StorageUtils getUtils() {
		return utils;
	}

	public void setUtils(StorageUtils utils) {
		this.utils = utils;
	}
}
