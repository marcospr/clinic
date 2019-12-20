package br.com.clinic.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.com.clinic.helper.FacesMessagesHelper;
import br.com.clinic.model.security.UserSystem;
import br.com.clinic.service.UserServiceRemote;

@Named
@SessionScoped
public class UserMBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@EJB
	private UserServiceRemote userService;
	@EJB(beanName = FacesMessagesHelper.BEAN_NAME)
	private FacesMessagesHelper messages;
	private UserSystem user;
	private List<UserSystem> users;
	private String termoPesquisa;

	public UserMBean() {
	}

	@PostConstruct
	public void init() {
		setUsers(userService.findAllByActive(Boolean.TRUE));
	}

	public void prepararNovoUsuario() {
		user = new UserSystem();
	}

	public void prepararEdicao() {
	}

	public void salvar() {
		userService.save(user);

		atualizarRegistros();

		messages.info("Empresa salva com sucesso!");

		RequestContext.getCurrentInstance().update(Arrays.asList("formUser:usersDataTable", "formUser:messages"));
	}

	public void excluir() {
		userService.delete(user);

		user = null;

		atualizarRegistros();

		messages.info("Usuario excluído com sucesso!");
	}

	public void pesquisar() {
		users = userService.findByLikeName(termoPesquisa);

		if (users.isEmpty()) {
			messages.info("Sua consulta não retornou registros.");
		}
	}

	private void atualizarRegistros() {
		if (jaHouvePesquisa()) {
			pesquisar();
		} else {
			todosUsuarios();
		}
	}

	private boolean jaHouvePesquisa() {
		return termoPesquisa != null && !"".equals(termoPesquisa);
	}

	public void todosUsuarios() {
		users = userService.findAll();
	}

	public boolean isUsuarioSeleciona() {
		return user != null && user.getId() != null;
	}

	public UserSystem getUser() {
		return user;
	}

	public void setUser(UserSystem user) {
		this.user = user;
	}

	public List<UserSystem> getUsers() {
		return users;
	}

	public void setUsers(List<UserSystem> users) {
		this.users = users;
	}

	public String getTermoPesquisa() {
		return termoPesquisa;
	}

	public void setTermoPesquisa(String termoPesquisa) {
		this.termoPesquisa = termoPesquisa;
	}

}
