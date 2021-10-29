package com.springboot.backend.apirest.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.apirest.models.entity.Role;
import com.springboot.backend.apirest.models.entity.Usuario;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {
      
	@Autowired
	private IServiceUsuario usuarioDao;

	
	private static final Logger log = LoggerFactory.getLogger(JpaUserDetailsService.class);

	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    
		Usuario usuario = usuarioDao.findUsuarioByUsername(username);
		
		if(usuario == null) {
			log.error("ERROR, No existe el usuario -> " + username);
			throw new UsernameNotFoundException("Username " + username + ", No existe!");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for (Role role : usuario.getRoles()) {
			log.info("ROLE : " + role.getAuthority());
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		
		if(authorities.isEmpty()) {
			log.error("ERROR, No tiene roles asignados el usuarios '"+username+"'" );
			throw new UsernameNotFoundException("ERROR no tiene Roles asignados el usuario '"+ username +"'");
		}
		
		
		return new User(username, usuario.getPassword(), usuario.getEnabled(), true, true,true, authorities);
	}
	
	
	
}
