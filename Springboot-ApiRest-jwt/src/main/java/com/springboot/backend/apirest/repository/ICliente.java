package com.springboot.backend.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.backend.apirest.models.entity.Cliente;

@Repository
public interface ICliente extends JpaRepository<Cliente, Long> {
        
	public Cliente findClienteByNombre(String nombre);
	
}
