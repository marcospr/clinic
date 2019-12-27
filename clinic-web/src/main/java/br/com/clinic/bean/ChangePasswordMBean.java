package br.com.clinic.bean;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.clinic.helper.FacesMessagesHelper;
import br.com.clinic.model.ConstantsView;
import br.com.clinic.model.security.UserSystem;
import br.com.clinic.service.UserServiceRemote;
import br.com.clinic.service.exception.FailureToken;
import br.com.clinic.service.impl.TokenService;

@Named
@ViewScoped
public class ChangePasswordMBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String novaSenha;
	private String confirmarNovaSenha;
	private String token;
	@EJB
	private UserServiceRemote userService;

	@EJB
	private TokenService tokenService;

	@EJB(beanName = FacesMessagesHelper.FACES_MESSAGES_HELPER)
	private FacesMessagesHelper messages;

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmarNovaSenha() {
		return confirmarNovaSenha;
	}

	public void setConfirmarNovaSenha(String confirmarNovaSenha) {
		this.confirmarNovaSenha = confirmarNovaSenha;
	}

	public String salvar() {
		// Validar senha
		if (!isValidNewPassword()) {
			messages.warn("senha invalida", false);
			return "";
		}

		// Recuperar usuario
		UserSystem user = userService.findByToken(token);

		// Token/User inexistente
		if (user == null) {
			messages.warn("token inexistente", false);
			return "";
		}

		// Parse Token
		try {
			tokenService.parse(token, user.getPasswordToken());
		} catch (FailureToken e) {
			messages.warn("token invalido", false);
			return "";
		}

		// Persistir nova senha
		userService.changePassword(user.getId(), this.novaSenha);

		messages.info("efetue o login com a nova senha", true);
		return ConstantsView.PAGE_LOGIN;
	}

	private boolean isValidNewPassword() {
		return novaSenha != null && confirmarNovaSenha != null && novaSenha.equals(confirmarNovaSenha);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
