package com.springboot.backend.apirest.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboot.backend.apirest.models.entity.Usuario;


@Repository
public interface IUsuario extends CrudRepository<Usuario, Long> {
          
	public Usuario findUsuarioByUsername(String username);
	
	public Usuario findUsuarioByEmail(String email);
	
}
