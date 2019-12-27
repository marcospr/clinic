package br.com.clinic.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import br.com.clinic.helper.FacesMessagesHelper;
import br.com.clinic.model.ConstantsView;
import br.com.clinic.model.security.UserSystem;
import br.com.clinic.service.UserServiceRemote;
import br.com.clinic.service.impl.GeneratorPasswordService;
import br.com.clinic.service.impl.TokenService;

@Named
@ViewScoped
public class LoginMBean implements Serializable {

	private static final long serialVersionUID = 535939470091051741L;

	private String usuario;
	private String senha;
	private String email = "marcos@gmail.com";

	@EJB
	private UserServiceRemote userService;

	@EJB
	private TokenService tokenService;

	@EJB
	private GeneratorPasswordService generatorPasswordService;

	@EJB(beanName = FacesMessagesHelper.FACES_MESSAGES_HELPER)
	private FacesMessagesHelper messages;

	/** Logger. */
	private Logger log = Logger.getLogger(LoginMBean.class.getCanonicalName());

	@PostConstruct
	public void init() {
//		this.forwardUrl = extractRequestedUrlBeforeLogin();
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

	public String solicitarNovaSenha() {
		UserSystem user = userService.findByEmail(email);
		if (user == null) {
			messages.warn("e-mail nao cadastrado", true);
			return ConstantsView.PAGE_LOGIN;
		}

		// Gera Senha
		String senhaToken = generatorPasswordService.gerarNovaSenha();

		// Gerar Token
		String token = tokenService.generate(user.getId().toString(), senhaToken);

		// Monta URL
		String url = externalContext().getRequestContextPath() + ConstantsView.PAGE_FORGOT + "&token=" + token;

		System.out.println(url);

		// Persistir o Token e sua senha
		user.setToken(token);
		user.setPasswordToken(senhaToken);
		userService.save(user);

		// TODO: mandar e-mail ( url ) JMS
		messages.info("e-mail enviado com sucesso", true);

		return ConstantsView.PAGE_LOGIN;
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
			return ConstantsView.PAGE_MAIN;
		} catch (ServletException e) {
			// e.printStackTrace();
			loginErrorMessage = e.getLocalizedMessage();
		} finally {
			// tratar aqui mensagens de segurança que possam ter vindo
			// do Login Module exibindo-as na forma de FacesMessage
			if (loginErrorMessage != null) {
				messages.error(loginErrorMessage, false);
			}
		}

		return null;
	}

	private void setUserSession(UserSystem user) {
		externalContext().getSessionMap().put(ConstantsView.SESSION_USER_VARIABLE_NAME, user);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
