package br.com.clinic.resources;

import java.net.URL;

public class ResourceApplicationLoader {

	/**
	 * getResource
	 * 
	 * Recupera resource através do class Loader deste jar.
	 * 
	 * @param pName
	 * @return
	 */
	public URL getResource(String pName) {
		return this.getClass().getClassLoader().getResource(pName);
	}
}
