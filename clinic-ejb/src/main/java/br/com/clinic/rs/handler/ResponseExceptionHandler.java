package br.com.clinic.rs.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Classe usada para interceptar as exceptions da API rest, podendo ser
 * devolvido um json com as informações da exception.
 * 
 * @author marcospr
 *
 */
@Provider
public class ResponseExceptionHandler implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		return Response.status(500).entity("Falha no Sistema, tente novamente").type("text/plain").build();
	}

}
