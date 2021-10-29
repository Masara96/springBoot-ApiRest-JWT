package com.springboot.backend.apirest.auth.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.backend.apirest.auth.SimpleGrantedAuthorityMixin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService implements IJWTservice {

	public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	public static final Long EXPIRATION_DATE = 14000000L;

	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String HEADER_STRING = "Authorization";

	private static final Logger log = LoggerFactory.getLogger(JWTService.class);

	@Override
	public String create(Authentication authentication) throws JsonProcessingException {

		String username = ((User) authentication.getPrincipal()).getUsername();

		Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

		Claims claims = Jwts.claims();

		// Lo convierte en formato JSON
		claims.put("authorities", new ObjectMapper().writeValueAsString(roles));

		String token = Jwts.builder().setSubject(username).signWith(SECRET_KEY).setClaims(claims)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
				.compact();

		return token;
	}

	@Override
	public boolean validate(String token) {

		try {
			getClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			log.error("ERROR : " + e.getMessage());
			return false;
		}

	}

	@Override
	public Claims getClaims(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(resolver(token))
				.getBody();

		return claims;
	}

	@Override
	public String getUsername(String token) {
		return getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token)
			throws JsonParseException, JsonMappingException, IOException {

		Object roles = getClaims(token).get("authorities");

		Collection<? extends GrantedAuthority> authorities = Arrays
				.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
						.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));

		return authorities;
	}

	@Override
	public String resolver(String token) {
		if (token != null && token.startsWith(TOKEN_PREFIX)) {
			return token.replace(TOKEN_PREFIX, "");
		} else {
			return null;
		}
	}

}
