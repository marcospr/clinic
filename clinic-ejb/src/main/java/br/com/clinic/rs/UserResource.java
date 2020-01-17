package br.com.clinic.rs;

import java.net.URI;
import java.util.List;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
	public Response findById(@PathParam("id") long id) {
		UserSystem user = userService.findById(id);
		if (user != null) {
			return Response.ok(user).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(UserSystem user) {
		userService.save(user);
		URI uri = URI.create("/" + user.getId());
		return Response.created(uri).build();
	}

	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") long id) {
		UserSystem user = userService.findById(id);
		userService.delete(user);
		return Response.ok().build();
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(UserSystem user) {
		userService.save(user);
		return Response.ok().build();
	}

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findAll() {
		List<UserSystem> users = userService.findAll();

		GenericEntity<List<UserSystem>> entity = new GenericEntity<List<UserSystem>>(users) {
		};
		return Response.status(Response.Status.OK).entity(entity).build();

	}
}
