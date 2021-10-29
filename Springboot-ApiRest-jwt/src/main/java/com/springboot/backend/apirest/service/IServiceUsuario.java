package com.springboot.backend.apirest.service;

import java.util.List;

import com.springboot.backend.apirest.models.entity.Usuario;

public interface IServiceUsuario {
       
	public List<Usuario> findAll();
	
	public void usuarioSave(Usuario usuario);
	
	public void deleteUsuario(Long id);
	 
	public Usuario findUsuarioById(Long id);
	
	public Usuario findUsuarioByUsername(String username);
	
	public Usuario findUsuariobyEmail(String email);
}
