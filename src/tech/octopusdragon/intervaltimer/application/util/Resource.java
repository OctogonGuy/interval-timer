package tech.octopusdragon.intervaltimer.application.util;

import java.io.InputStream;
import java.net.URL;

/**
 * Contains resource information
 * @author Alex Gill
 *
 */
public enum Resource {
	ICON("images/icon.png");
	
	// --- Global constants ---
	// The directory to the resources
	private static final String DIRECTORY = "resources/";
	
	// --- Instance variables ---
	private String path;	// The path to the resource file
	
	/**
	 * Initializes resource information
	 * @param path The path to the resource file
	 */
	private Resource(String path) {
		this.path = DIRECTORY + path;
	}
	
	/**
	 * @return The path of the resource
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * @return The resource as a URL
	 */
	public URL getResource() {
		return Resource.class.getClassLoader().getResource(path);
	}
	
	/**
	 * @return The resource as an input stream
	 */
	public InputStream getResourceAsStream() {
		return Resource.class.getClassLoader().getResourceAsStream(path);
	}
}