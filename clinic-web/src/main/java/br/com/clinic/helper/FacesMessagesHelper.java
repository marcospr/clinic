package br.com.clinic.helper;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Stateless(name = FacesMessagesHelper.BEAN_NAME)
public class FacesMessagesHelper implements Serializable {
	public final static String BEAN_NAME = "facesMessagesHelper";
	private static final long serialVersionUID = 1L;

	private void add(String msg, FacesMessage.Severity severity) {
		FacesMessage facesMessage = new FacesMessage(msg);
		facesMessage.setSeverity(severity);

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	public void info(String msg) {
		add(msg, FacesMessage.SEVERITY_INFO);
	}

}
