package br.com.clinic.bean;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.com.clinic.helper.FacesMessagesHelper;
import br.com.clinic.model.ConstantsView;
import br.com.clinic.model.security.UserSystem;
import br.com.clinic.service.UserServiceRemote;

@Named
@SessionScoped
public class UserMBean extends GenericCrudMBean<UserSystem, Long> {
	private static final long serialVersionUID = 1L;
	@EJB
	private UserServiceRemote userService;

	@EJB(beanName = FacesMessagesHelper.FACES_MESSAGES_HELPER)
	private FacesMessagesHelper messages;

	private UserSystem user;
	private List<UserSystem> users;
	private String termoPesquisa;

	public UserMBean() {
	}

	@PostConstruct
	public void init() {
		try {
			setUsers(userService.findAllByActive(Boolean.TRUE));
		} catch (Exception e) {
			messages.info(facesContext(), "Falha no servidor. " + e.getMessage(), true);
			try {
				externalContext().redirect(getRequestContextPath() + ConstantsView.PAGE_MAIN);
			} catch (IOException e1) {
				messages.info(facesContext(), "Falha no servidor. " + e.getMessage(), true);
			}
		}
	}

	public String prepararNovoUsuario() {
		user = new UserSystem();
		return ConstantsView.PAGE_USER_CREATE;
	}

	public String prepararEdicao() {
		return ConstantsView.PAGE_USER_UPDATE;
	}

	public String onClicksalvar() {
		save(user);
		return ConstantsView.PAGE_USER_LIST;
	}

	public String onClickexcluir() {
		delete(user);
		return ConstantsView.PAGE_USER_LIST;
	}

	public void onClickpesquisar() {
		users = userService.findByLikeName(termoPesquisa);

		if (users.isEmpty()) {
			messages.info(facesContext(), "Sua consulta não retornou registros.", false);
		}
	}

	private void atualizarRegistros() {
		if (jaHouvePesquisa()) {
			onClickpesquisar();
		} else {
			todosUsuarios();
		}
	}

	private boolean jaHouvePesquisa() {
		return termoPesquisa != null && !"".equals(termoPesquisa);
	}

	public void todosUsuarios() {
		users = findAll();
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

	@Override
	protected void save(UserSystem user) {
		userService.save(user);

		atualizarRegistros();

		messages.info(facesContext(), "Empresa salva com sucesso!", true);

//		RequestContext.getCurrentInstance().update(Arrays.asList("formUser:usersDataTable", "formUser:messages"));

	}

	@Override
	protected void delete(UserSystem user) {
		userService.delete(user);

		user = null;

		atualizarRegistros();

		messages.info(facesContext(), "Usuario excluído com sucesso!", true);

	}

	@Override
	protected List<UserSystem> findAll() {
		return userService.findAll();
	}

	@Override
	protected UserSystem findById(Long id) {
		return userService.findById(id);
	}

}
