package br.com.clinic.rs;

import java.net.URI;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.clinic.model.security.Profile;
import br.com.clinic.model.security.UserSystem;
import br.com.clinic.service.ProfileServiceRemote;
import br.com.clinic.service.UserServiceRemote;

@WebService
@Path("user")
@RequestScoped
public class UserResource {
	@EJB
	private UserServiceRemote userService;

	@EJB
	private ProfileServiceRemote profileService;

	@GET
	@Path("{id}")
	@Produces({ "application/json" })
	public UserSystem busca(@PathParam("id") long id) {
		UserSystem user = userService.findById(id);
		return user;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response adiciona(UserSystem user) {
		userService.save(user);
		URI uri = URI.create("/" + user.getId());
		return Response.created(uri).build();
	}

	@DELETE
	@Path("{id}/profile/{profileId}")
	public Response removeProfile(@PathParam("id") long id, @PathParam("profileId") long profile) {
		Profile profileToDel = profileService.findById(id);
		UserSystem user = userService.findById(id);
		user.getProfile().remove(profileToDel);
		userService.save(user);
		return Response.ok().build();
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response altera(UserSystem user) {
		userService.save(user);
		return Response.ok().build();
	}
}
