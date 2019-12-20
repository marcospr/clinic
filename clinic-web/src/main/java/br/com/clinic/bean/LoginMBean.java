package br.com.clinic.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import br.com.clinic.model.security.UserSystem;
import br.com.clinic.service.UserServiceRemote;

@Named
@ViewScoped
public class LoginMBean implements Serializable {

	private static final long serialVersionUID = 535939470091051741L;
	private static final String PAGINA_INDEX = "/main/Main.xhtml?faces-redirect=true";
	private static final String SESSION_USER_VARIABLE_NAME = "user";

	private String forwardUrl;

	private String usuario;
	private String senha;

	@EJB
	private UserServiceRemote userService;

	/** Logger. */
	private Logger log = Logger.getLogger(LoginMBean.class.getCanonicalName());

	@PostConstruct
	public void init() {
		this.forwardUrl = extractRequestedUrlBeforeLogin();
	}

	private String extractRequestedUrlBeforeLogin() {
		ExternalContext externalContext = externalContext();
		String requestedUrl = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
		if (requestedUrl == null) {
			return externalContext.getRequestContextPath() + "/Login.xhtml";
		}
		String queryString = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);
		return requestedUrl + (queryString == null ? "" : "?" + queryString);
	}

	private ExternalContext externalContext() {
		return facesContext().getExternalContext();
	}

	public String getRequestContextPath() {
		return externalContext().getRequestContextPath();
	}

	private FacesContext facesContext() {
		return FacesContext.getCurrentInstance();
	}

	public void onClickForgot() {
		// redireciona para a pagina de Forgot.xhtml
		// Recupero o usuario pelo e-mail digitado
		// pego o email
		// gero link de troca de senha : ver como é montado esse link
		// envia email com o link
		// criar pagina de troca de senha do link clicado
	}

	public String onClickLogar() throws IOException {
		String loginErrorMessage = null;
		try {

			ExternalContext externalContext = externalContext();
			HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
			request.getSession().invalidate();
			externalContext().invalidateSession();

			request.login(this.getUsuario(), this.getSenha()); // Realm

			UserSystem user = userService.findByEmail(getUsuario());

			setUserSession(user);
//			externalContext.redirect(forwardUrl);
			return PAGINA_INDEX;
		} catch (ServletException e) {
			// e.printStackTrace();
			loginErrorMessage = e.getLocalizedMessage();
		} finally {
			// tratar aqui mensagens de segurança que possam ter vindo
			// do Login Module exibindo-as na forma de FacesMessage
			if (loginErrorMessage != null) {
				facesContext().addMessage(null, new FacesMessage(loginErrorMessage));
			}
		}

		return null;
	}

	private void setUserSession(UserSystem user) {
		externalContext().getSessionMap().put(SESSION_USER_VARIABLE_NAME, user);
	}

	public void logout() throws IOException {
		externalContext().invalidateSession();
		externalContext().redirect(getRequestContextPath() + "/Login.xhtml"); // <=> return
																				// "/Login.xhtml?faces-redirect=true";
	}

	public UserSystem getUserSession() {
		FacesContext context = facesContext();
		ExternalContext externalContext = context.getExternalContext();
		return (UserSystem) externalContext.getSessionMap().get("user");
	}

	public boolean isUserLoggedIn() {
		return getUserSession() != null;
	}

	public boolean isUserInRole(String role) {
		FacesContext context = facesContext();
		ExternalContext externalContext = context.getExternalContext();
		return externalContext.isUserInRole(role);
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
