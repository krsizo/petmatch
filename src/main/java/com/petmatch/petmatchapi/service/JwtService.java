package com.petmatch.petmatchapi.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service

public class JwtService
{

	@Value("${app.jwt.secret}")
	private String secret;

	@Value("${app.jwt.expiration-ms}")
	private long expirationMs;

	private Key getSecretKey()
	{
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String generateToken(String email, String role) {
		return Jwts.builder()
			.setSubject(email)
			.claim("role", role)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expirationMs))
			.signWith(SignatureAlgorithm.HS256, getSecretKey())
			.compact();
	}

	public String extractEmail(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSecretKey())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	public String extractRole(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSecretKey())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("role", String.class);
	}

	public boolean isTokenValid(String token, String email) {
		String extractedEmail = extractEmail(token);
		return extractedEmail.equals(email) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		Date expiration = Jwts.parserBuilder()
			.setSigningKey(getSecretKey())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getExpiration();
		return expiration.before(new Date());
	}
}
