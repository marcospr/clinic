package br.com.clinic.service;

import br.com.clinic.model.security.UserSystem;

public interface SecurityServiceRemote {

	void rememberPassword(UserSystem user, String url);

}
