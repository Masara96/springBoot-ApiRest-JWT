package com.springboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.apirest.models.entity.Usuario;
import com.springboot.backend.apirest.service.IServiceUsuario;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = {"http://localhost:4200","*"})
public class RestUsuariosControllers {
    
	@Autowired
	private IServiceUsuario serviceUser;
	
	@GetMapping
	@Secured("ROLE_ADMIN")
	public List<Usuario> getUsuarios (){
		return serviceUser.findAll();
	}
	

}
