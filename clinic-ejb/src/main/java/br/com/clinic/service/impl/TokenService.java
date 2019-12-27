package br.com.clinic.service.impl;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Singleton;
import javax.xml.bind.DatatypeConverter;

import br.com.clinic.service.exception.FailureToken;
import br.com.clinic.service.model.ConstantsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Singleton
public class TokenService {

	public String generate(String userId, String secret) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

//		long nowMillis = System.currentTimeMillis();
//		Date now = new Date(nowMillis);
		Date now = new Date();

		// We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId(userId).setIssuedAt(now).setSubject("limpar senha")
				.setIssuer("Sistema Clinic").signWith(signatureAlgorithm, signingKey);

		// if it has been specified, let's add the expiration
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.MINUTE, ConstantsService.TOKEN_EXPIRATION_TIME);
		builder.setExpiration(c.getTime());

		// Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	public void parse(String token, String secret) throws FailureToken {
		Claims claims = null;
		try {
			// This line will throw an exception if it is not a signed JWS (as expected)
			claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
			throw new FailureToken();
		}

		System.out.println("ID: " + claims.getId());
		System.out.println("Subject: " + claims.getSubject());
		System.out.println("Issuer: " + claims.getIssuer());
		System.out.println("Expiration: " + claims.getExpiration());
	}
}
