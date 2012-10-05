package com.tedrasoft.remotestorage.common;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.List;
/**
 * Keeps list of files retrieved at search
 * @author Dragos
 *
 */
@XmlRootElement(name="searchList")
public class SearchList implements Serializable{

	private static final long serialVersionUID = -790677396090230443L;
	private List<String> fullPathList;

    public SearchList() {
    }

    public SearchList(List<String> fullPathList) {
        this.fullPathList = fullPathList;
    }

    @XmlElement(name="file")
    public List<String> getFullPathList() {
        return fullPathList;
    }

    public void setFullPathList(List<String> fileList) {
        this.fullPathList = fileList;
    }

}
