package br.com.clinic.ws;

import java.util.List;

import javax.ejb.Remote;

import br.com.clinic.model.security.UserSystem;

@Remote
public interface UserWSRemote {
	List<UserSystem> getUsers();
}
