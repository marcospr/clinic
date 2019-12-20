package br.com.clinic.service.impl;

import java.io.Serializable;
import java.util.List;

import br.com.clinic.model.security.Profile;
import br.com.clinic.model.security.Role;
import br.com.clinic.model.security.UserSystem;

public class SecurityService implements Serializable {
	private static final long serialVersionUID = 1L;

	public Boolean login(UserSystem user) {
		return Boolean.TRUE;
	}

	public Boolean logout(UserSystem user) {
		return Boolean.TRUE;
	}

	public List<Role> getRoles() {
		return null;
	}

	public List<Profile> getProfiles() {
		return null;
	}

	public Boolean isEnable(UserSystem user) {
		return Boolean.TRUE;
	}

}
