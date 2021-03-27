package com.hsbc.jwt.util;

import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	@Value("${app.secret}")
	String secretKey;

	public String generateToken(String subject) {

		String token = Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15)))
				.signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(secretKey.getBytes())).compact();

		return token;
	}

	public Claims getClaims(String token) {

		Claims claim = Jwts.parser()
				.setSigningKey(Base64.getEncoder().encode(secretKey.getBytes()))
				.parseClaimsJws(token)
				.getBody();

		return claim;
	}

	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}

	public Date getTokenExpiration(String token) {

		return getClaims(token).getExpiration();
	}

	public Boolean isTokenExpired(String token) {
		return getTokenExpiration(token).after(new Date(System.currentTimeMillis()));
	}

	public Boolean isTokenValid(String token, String username) {

		String userNameInToken = getUsername(token);
		if (userNameInToken.equals(username) && isTokenExpired(token)) {
			return true;
		} else
			return false;
	}
}
