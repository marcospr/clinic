package br.com.clinic.service;

import java.util.List;

import javax.ejb.Remote;

import br.com.clinic.model.security.UserSystem;

@Remote
public interface UserServiceRemote extends CrudOperation<UserSystem, Long> {

	UserSystem findByEmail(String email);

	List<UserSystem> findByLikeName(String likeName);

	List<UserSystem> findAllByActive(boolean actived);

	UserSystem findByToken(String string);

	void changePassword(Long id, String novaSenha);

	void clearToken(UserSystem user);

	List<UserSystem> findAllByTokenIsNotNull();

}
