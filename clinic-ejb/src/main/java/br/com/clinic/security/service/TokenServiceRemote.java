package br.com.clinic.security.service;

import br.com.clinic.exception.FailureToken;

public interface TokenServiceRemote {

	void parse(String token, String secret) throws FailureToken;

	String generate(String userId, String secret);

}
