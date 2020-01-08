package br.com.clinic.bean;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.clinic.model.EntityDefault;

@Named
@ViewScoped
public abstract class GenericMBean<T extends EntityDefault> implements Serializable {
	private static final long serialVersionUID = 1L;

	protected ExternalContext externalContext() {
		return facesContext().getExternalContext();
	}

	/**
	 * Esse metodo é acessado tanto pelas classes filhas como pela pagina xhtml
	 * 
	 * @return String
	 */
	public String getRequestContextPath() {
		return externalContext().getRequestContextPath();
	}

	protected FacesContext facesContext() {
		return FacesContext.getCurrentInstance();
	}

}
