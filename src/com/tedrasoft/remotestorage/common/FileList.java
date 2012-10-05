package com.tedrasoft.remotestorage.common;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;
/**
 * Keeps the list of files from a storage folder
 * @author Dragos
 *
 */
@XmlRootElement(name="fileList")
public class FileList implements Serializable{

	private static final long serialVersionUID = -1769082543087410142L;
	private String relativePath;
    private List<String> fileList;

    public FileList() {
    }

    public FileList(List<String> fileList) {
        this.fileList = fileList;
    }

    @XmlElements(
            @XmlElement(name="file", type=String.class)
    )
    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }
    @XmlElement(name="relativePath")
	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

}
