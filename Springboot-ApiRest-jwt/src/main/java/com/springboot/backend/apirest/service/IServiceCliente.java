package com.springboot.backend.apirest.service;

import java.util.List;

import com.springboot.backend.apirest.models.entity.Cliente;

public interface IServiceCliente {
         
	public Cliente findClienteById(Long id);
	 
	public Cliente findClienteByNombre(String nombre); 
	
	public List<Cliente> findAll();
	
	public Cliente SaveCliente(Cliente cliente);
	
	public void DeleteCliente(Long id);
	
	
}
