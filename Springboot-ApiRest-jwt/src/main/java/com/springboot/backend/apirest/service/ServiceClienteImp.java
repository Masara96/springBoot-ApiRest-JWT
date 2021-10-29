package com.springboot.backend.apirest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.apirest.models.entity.Cliente;
import com.springboot.backend.apirest.repository.ICliente;

@Service
public class ServiceClienteImp implements IServiceCliente {

	@Autowired
	private ICliente clienteDao;
	
	@Override
	@Transactional(readOnly = true)
	public Cliente findClienteById(Long id) {
		return (Cliente) clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findClienteByNombre(String nombre) {
		return clienteDao.findClienteByNombre(nombre);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return clienteDao.findAll();
	}

	@Override
	@Transactional
	public Cliente SaveCliente(Cliente cliente) {
		return (Cliente) clienteDao.save(cliente);
	}

	@Override
	@Transactional
	public void DeleteCliente(Long id) {
		clienteDao.deleteById(id);
		
	}

}
