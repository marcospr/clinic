package br.com.clinic.helper;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

@Stateless(name = FacesMessagesHelper.FACES_MESSAGES_HELPER)
public class FacesMessagesHelper implements Serializable {

	public final static String FACES_MESSAGES_HELPER = "facesMessagesHelper";
	private static final long serialVersionUID = 1L;

	private void add(FacesContext facesContext, String message, FacesMessage.Severity severity, boolean isFlash) {
		if (isFlash) {
			Flash flash = facesContext.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			flash.setRedirect(true);
			facesContext.addMessage(null, new FacesMessage(severity, message, ""));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
		}
	}

	public void info(FacesContext facesContext, String msg, boolean isFlash) {
		add(facesContext, msg, FacesMessage.SEVERITY_INFO, isFlash);
	}

	public void error(FacesContext facesContext, String msg, boolean isFlash) {
		add(facesContext, msg, FacesMessage.SEVERITY_ERROR, isFlash);
	}

	public void warn(FacesContext facesContext, String msg, boolean isFlash) {
		add(facesContext, msg, FacesMessage.SEVERITY_WARN, isFlash);
	}

}
