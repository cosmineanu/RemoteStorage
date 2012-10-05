package com.tedrasoft.remotestorage.client;

import java.io.File;
import java.io.FileOutputStream;
import com.tedrasoft.remotestorage.common.FileList;
import com.tedrasoft.remotestorage.common.SearchList;

import org.apache.commons.httpclient.HttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Client for storage service
 * 
 * @author Dragos
 * 
 */
@Controller
@SessionAttributes({ "currentPath" })
public class ClientController {
	private RestTemplate restTemplate;
	private UsernamePasswordCredentials credentials;
	private String host;
	private String localStorage;
	private String relativePathSeparator;
	private Logger log = Logger.getLogger(this.getClass());
	
	public String getLocalStorage() {
		return localStorage;
	}

	public void setLocalStorage(String localStorage) {
		this.localStorage = localStorage;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setCredentials(UsernamePasswordCredentials credentials) {
		this.credentials = credentials;
	}
	/**
	 * Prepares a get request entity for media type
	 * @param type
	 * @return
	 */
	private static HttpEntity<String> prepareGet(MediaType type) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(type);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return entity;
	}
	/**
	 * Sets the currentPath as session attribute if not set 
	 * @param model
	 * @return
	 */
	@ModelAttribute
	public String getCurrentPath(ModelMap model) {
		String currentPath = (String) model.get("currentPath");
		if (currentPath == null) {
			currentPath = relativePathSeparator;
			model.addAttribute("currentPath", currentPath);
		}
		return currentPath;
	}
	
	/**
	 * Processes a request for files in currentFolder. If receives up=true goes one step up in folder hierarchy.If receives nextFolder then retrieves nextFolder
	 * file list. 
	 * @param currentPath
	 * @param map
	 * @param up - command to go up one level
	 * @param nextFolder -  navigate from current folder to nextFolder 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/client")
	public ModelAndView renderListPage(
			@ModelAttribute String currentPath,
			ModelMap map,
			@RequestParam(value = "up", required = false) String up,
			@RequestParam(value = "nextFolder", required = false) String nextFolder) {
		
		ModelAndView modelAndView = new ModelAndView("fileList");
		Boolean error = false;
		String errorMessage = "";
		String urlBase = "http://" + host + "/RemoteStorage";
		//
		if (currentPath == null) {
			//root
			currentPath = relativePathSeparator;
			map.addAttribute("currentPath", currentPath);
		}
		//
		if (up != null && up.equalsIgnoreCase("true")) {
			//if already at root get list for root
			currentPath = (currentPath.lastIndexOf(relativePathSeparator) == 0) ? relativePathSeparator : currentPath
					.substring(0, currentPath.lastIndexOf(relativePathSeparator));
			map.addAttribute("currentPath", currentPath);
		}
		
		if (nextFolder != null) {
			log.info("next folder is:"+nextFolder);
			currentPath = (currentPath.equalsIgnoreCase(relativePathSeparator)) ? (currentPath + nextFolder)
					: (currentPath + relativePathSeparator + nextFolder);
			map.addAttribute("currentPath", currentPath);
		}
		try {

			CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) restTemplate.getRequestFactory();
			HttpClient client = factory.getHttpClient();
			client.getState().setCredentials(AuthScope.ANY, credentials);
			HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_XML);

			ResponseEntity<FileList> fileListResponse = restTemplate.exchange(
					urlBase + "/service/files?path=" + currentPath,
					HttpMethod.GET, entity, FileList.class);
			FileList fileList = fileListResponse.getBody();
			modelAndView.addObject("fileList", fileList);

		} catch (Exception e) {
			log.error(e);	
			error = true;
			errorMessage = e.getMessage();
			
		}

		modelAndView.addObject("error", error);
		modelAndView.addObject("errorMessage", errorMessage);

		return modelAndView;
	}
	/**
	 * Go to upload file form
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/prepareFileForm")
	public ModelAndView uploadFilePage() {
		ModelAndView modelAndView = new ModelAndView("fileForm");

		return modelAndView;
	}
	/**
	 * Go to create folder form
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/prepareFolderForm")
	public ModelAndView newFolderForm() {
		ModelAndView modelAndView = new ModelAndView("folderForm");

		return modelAndView;
	}
	
	/**
	 * Upload file 
	 * @param currentPath
	 * @param file
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/saveFile")
	public ModelAndView saveFilePage(@ModelAttribute String currentPath,
			@RequestParam(value = "file", required = true) MultipartFile file) {
		ModelAndView modelAndView = new ModelAndView("addFileResult");
		Boolean error = false;
		String errorMessage = "";
		String urlBase = "http://" + host + "/RemoteStorage";

		try {

			CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) restTemplate
					.getRequestFactory();
			HttpClient client = factory.getHttpClient();
			client.getState().setCredentials(AuthScope.ANY, credentials);

			MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
			
			
			File f = new File(localStorage+File.separator+file.getOriginalFilename());
			file.transferTo(f);
			
			parts.add("file", new FileSystemResource(f));
			parts.add("folder", currentPath);
			restTemplate.postForLocation(urlBase + "/service/saveFile", parts);

			modelAndView.addObject("result", "File saved");

		} catch (Exception e) {
			log.error(e);
			error = true;
			errorMessage = e.getMessage();
		}

		modelAndView.addObject("error", error);
		modelAndView.addObject("errorMessage", errorMessage);
		return modelAndView;
	}
	/**
	 * Create a new Folder
	 * @param currentPath
	 * @param name
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/newFolder")
	public ModelAndView newFolderPage(@ModelAttribute String currentPath,
			@RequestParam(value = "name", required = true) String name) {
		ModelAndView modelAndView = new ModelAndView("addFolderResult");
		Boolean error = false;
		String errorMessage = "";
		String urlBase = "http://" + host + "/RemoteStorage";

		try {

			CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) restTemplate
					.getRequestFactory();
			HttpClient client = factory.getHttpClient();
			client.getState().setCredentials(AuthScope.ANY, credentials);
			HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_XML);

			ResponseEntity<String> stringResponse = restTemplate
					.exchange(urlBase + "/service/newFolder?name=" + name
							+ "&&parent=" + currentPath, HttpMethod.GET,
							entity, String.class);
			String response = stringResponse.getBody();
			modelAndView.addObject("result", response.equalsIgnoreCase("true")?"Folder created":"No folder created");

		} catch (Exception e) {
			e.printStackTrace();
			error = true;
			errorMessage = e.getMessage();
		}

		modelAndView.addObject("error", error);
		modelAndView.addObject("errorMessage", errorMessage);

		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/delete")
	public ModelAndView deletePage(@ModelAttribute String currentPath,
			@RequestParam(value = "name", required = true) String name) {
		ModelAndView modelAndView = new ModelAndView("deleteResult");
		Boolean error = false;
		String errorMessage = "";
		String urlBase = "http://" + host + "/RemoteStorage";

		try {

			CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) restTemplate
					.getRequestFactory();
			HttpClient client = factory.getHttpClient();
			client.getState().setCredentials(AuthScope.ANY, credentials);
			HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_XML);

			ResponseEntity<String> stringResponse = restTemplate
					.exchange(urlBase + "/service/deleteFile?relativePath="
							+ currentPath +relativePathSeparator + name, HttpMethod.GET,
							entity, String.class);
			String response = stringResponse.getBody();
			modelAndView.addObject("result", response.equalsIgnoreCase("true")?"Deleted Successfully":"Delete failed");

		} catch (Exception e) {
			log.error(e);
			error = true;
			errorMessage = e.getMessage();
		}

		modelAndView.addObject("error", error);
		modelAndView.addObject("errorMessage", errorMessage);

		return modelAndView;
	}

	/**
	 * Search files in storage
	 * @param searchTerm
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public ModelAndView searchPage(
			@RequestParam(value = "searchTerm", required = true) String searchTerm) {
		ModelAndView modelAndView = new ModelAndView("searchResult");
		Boolean error = false;
		String errorMessage = "";
		String urlBase = "http://" + host + "/RemoteStorage";

		try {

			CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) restTemplate
					.getRequestFactory();
			HttpClient client = factory.getHttpClient();
			client.getState().setCredentials(AuthScope.ANY, credentials);
			HttpEntity<String> entity = prepareGet(MediaType.APPLICATION_XML);

			ResponseEntity<SearchList> fileListResponse = restTemplate
					.exchange(urlBase + "/service/searchFile?searchTerm="
							+ searchTerm, HttpMethod.GET, entity,
							SearchList.class);
			SearchList fileList = fileListResponse.getBody();
			modelAndView.addObject("searchList", fileList);

		} catch (Exception e) {
			log.error(e);
			error = true;
			errorMessage = e.getMessage();
		}

		modelAndView.addObject("error", error);
		modelAndView.addObject("errorMessage", errorMessage);

		return modelAndView;

	}
	/**
	 * Download File
	 * @param currentPath
	 * @param name
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/download")
	public ModelAndView getFilePage(@ModelAttribute String currentPath,
			@RequestParam(value = "name", required = true) String name) {
		ModelAndView modelAndView = new ModelAndView("downloadResult");
		Boolean error = false;
		String errorMessage = "";
		String urlBase = "http://" + host + "/RemoteStorage";

		try {

			CommonsClientHttpRequestFactory factory = (CommonsClientHttpRequestFactory) restTemplate
					.getRequestFactory();
			HttpClient client = factory.getHttpClient();
			client.getState().setCredentials(AuthScope.ANY, credentials);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_HTML);

			ResponseEntity<byte[]> stringResponse = restTemplate.exchange(
					urlBase + "/service/download?relativePath=" + currentPath
							+ relativePathSeparator + name, HttpMethod.GET,
					new HttpEntity<String>(headers), byte[].class);
			byte[] response = stringResponse.getBody();
			FileOutputStream fos = new FileOutputStream(new File(localStorage
					+ File.separator + name));
			fos.write(response);
			fos.flush();
			fos.close();
			modelAndView.addObject("result", "download finished");

		} catch (Exception e) {
			log.error(e);
			error = true;
			errorMessage = e.getMessage();
		}

		modelAndView.addObject("error", error);
		modelAndView.addObject("errorMessage", errorMessage);
		
		return modelAndView;

	}

	public String getRelativePathSeparator() {
		return relativePathSeparator;
	}

	public void setRelativePathSeparator(String relativePathSeparator) {
		this.relativePathSeparator = relativePathSeparator;
	}
}
