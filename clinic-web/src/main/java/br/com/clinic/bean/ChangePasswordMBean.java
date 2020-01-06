package br.com.clinic.bean;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.clinic.exception.FailureToken;
import br.com.clinic.helper.FacesMessagesHelper;
import br.com.clinic.model.ConstantsView;
import br.com.clinic.model.security.UserSystem;
import br.com.clinic.security.service.TokenServiceRemote;
import br.com.clinic.service.UserServiceRemote;

@Named
@ViewScoped
public class ChangePasswordMBean extends GenericMBean<UserSystem> {
	private static final long serialVersionUID = 1L;
	private String novaSenha;
	private String confirmarNovaSenha;
	private String token;
	@EJB
	private UserServiceRemote userService;

	@EJB
	private TokenServiceRemote tokenService;

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
			messages.warn(facesContext(), "senha invalida", false);
			return "";
		}

		// Recuperar usuario
		UserSystem user = userService.findByToken(token);

		// Token/User inexistente
		if (user == null) {
			messages.warn(facesContext(), "token inexistente", false);
			return "";
		}

		// Parse Token
		try {
			tokenService.parse(token, user.getPasswordToken());
		} catch (FailureToken e) {
			messages.warn(facesContext(), "token invalido", false);
			return "";
		}

		// Persistir nova senha
		userService.changePassword(user.getId(), this.novaSenha);

		// Mensagem
		messages.info(facesContext(), "efetue o login com a nova senha", true);
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
