package com.springboot.backend.apirest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.apirest.models.entity.Usuario;
import com.springboot.backend.apirest.repository.IUsuario;



@Service
public class ServiceUsuario implements IServiceUsuario {

	@Autowired
	private IUsuario usuarioDao;
	
	@Override
	@Transactional
	public void usuarioSave(Usuario usuario) {
		usuarioDao.save(usuario);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findUsuarioById(Long id) {
		return usuarioDao.findById(id).orElse(null);
	}
    
	@Override
	@Transactional
	public void deleteUsuario(Long id) {
		usuarioDao.delete(findUsuarioById(id));
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findUsuarioByUsername(String username) {
		return usuarioDao.findUsuarioByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findUsuariobyEmail(String email) {
		return usuarioDao.findUsuarioByEmail(email);
	}

	@Override
	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioDao.findAll();
	}

	
	
}
