package br.com.clinic.ws;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

import br.com.clinic.model.security.UserSystem;
import br.com.clinic.service.UserServiceRemote;

@Stateless
@WebService(serviceName = "UserService")
public class UserWS implements UserWSRemote {

	@EJB
	private UserServiceRemote userService;

	@WebMethod(operationName = "getUsers")
	@WebResult(name = "users")
	@Override
	public List<UserSystem> getUsers() {
		return userService.findAll();
	}

}
