package com.springboot.backend.apirest.auth.filter;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.backend.apirest.auth.service.IJWTservice;
import com.springboot.backend.apirest.auth.service.JWTService;
import com.springboot.backend.apirest.models.entity.Usuario;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private IJWTservice jwtService;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, IJWTservice jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username != null && password != null) {
			logger.info("Username desde el request parameter(form-data) : " + username);
			logger.info("Password desde el request parameter(form-data) : " + password);
		} else {
			Usuario user = null;
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

				username = user.getUsername();
				password = user.getPassword();

				logger.info("Username desde el request parameter(RAW) : " + username);
				logger.info("Password desde el request parameter(RAW) : " + password);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		username.trim();

		// Crear un token pero es parte del sistema, no es el mismo que el de
		// successFulAuthentication!
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		String token = jwtService.create(authResult);

		response.addHeader(JWTService.HEADER_STRING, JWTService.TOKEN_PREFIX + token);

		logger.warn("Password: " + authResult.getCredentials());
		
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("token", token);
		body.put("user", (User) authResult.getPrincipal());
		body.put("mensaje", String.format("Hola %s, has iniciado session con exito!",
				((User) authResult.getPrincipal()).getUsername()));

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(200);
		response.setContentType("application/json");

	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		// Si no se puede loggiar!
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("mensaje", "ERROR de Autenticacion, el username o password es incorrecto!");
		body.put("error", failed.getMessage());

		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setStatus(401);
		response.setContentType("application/json");
	}

}
