package com.springboot.backend.apirest.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.apirest.models.entity.Cliente;
import com.springboot.backend.apirest.service.IServiceCliente;

@RestController
@RequestMapping("/api")
public class RestClienteController {

	@Autowired
	private IServiceCliente clienteService;
	
	@GetMapping("/clientes")
	@Secured("ROLE_USER")
	public ResponseEntity<?> listClients() {

		List<Cliente> clientes = new ArrayList<Cliente>();
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			clientes = clienteService.findAll();
		} catch (DataAccessException e) {
			response.put("mensaje", "No se realizo la accion por problemas en el servidor");
			response.put("error", "ERROR : " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (clientes == null) {
			response.put("mensaje", "No se encontro usuarios en la BD!");
			response.put("clientes", clientes);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		response.put("clientes", clientes);
		response.put("mensaje", "Se han encontrado resultados de clientes!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK) ;
	}
	
	
	@GetMapping("/clientes/{id}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> show(@PathVariable Long id) {

		Cliente cliente = null;
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			cliente = clienteService.findClienteById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "No se realizo la accion por problemas en el servidor");
			response.put("error", "ERROR : " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (cliente == null) {
			response.put("mensaje", "No se encontro al usuario en la BD!");
			response.put("clientes", cliente);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		response.put("clientes", cliente);
		response.put("mensaje", "Se han encontrado un cliente clientes!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK) ;
	}
	
	
	@PostMapping("/clientes/save")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> saveClient(@Valid @RequestBody Cliente cliente, BindingResult result) {

		Cliente saveCliente = new Cliente();
		
		Map<String, Object> response = new HashMap<String, Object>();

		if(result.hasErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(), "El campo -> ".concat(err.getField().concat(" ").concat(err.getDefaultMessage())));
			});
			
			return new ResponseEntity<Map<String, String>>(errores, HttpStatus.BAD_REQUEST);
		}
		
		try {
			saveCliente = clienteService.SaveCliente(cliente);
			saveCliente.setFecha(new Date());
		} catch (DataAccessException e) {
			response.put("mensaje", "No se realizo la accion por problemas en el servidor");
			response.put("error", "ERROR : " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (cliente == null) {
			response.put("mensaje", "No pudo guardar el cliente en la BD!");
			response.put("clientes",saveCliente);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		response.put("clientes", saveCliente);
		response.put("mensaje", "Se ha guardado el cliente : '" + saveCliente.getId());
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED) ;
	}

	 
	
	@PutMapping("/clientes/edit")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> updateClient(@Valid @RequestBody Cliente cliente, BindingResult result) {

		Cliente updateCliente = null;
		Map<String, Object> response = new HashMap<String, Object>();

		if(result.hasErrors()) {
			Map<String, String> errores = new HashMap<>();
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(), "El campo -> ".concat(err.getField().concat(" ").concat(err.getDefaultMessage())));
			});
			
			return new ResponseEntity<Map<String, String>>(errores, HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			updateCliente = clienteService.findClienteById(cliente.getId());
			updateCliente.setNombre(cliente.getNombre());
			updateCliente.setApellido(cliente.getApellido());
			updateCliente.setEmail(cliente.getEmail());
			
			cliente = clienteService.SaveCliente(updateCliente);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "No se realizo la accion por problemas en el servidor");
			response.put("error", "ERROR : " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (cliente == null) {
			response.put("mensaje", "No se encontro al usuario en la BD!");
			response.put("clientes", cliente);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		response.put("clientes", cliente);
		response.put("mensaje", "Se ha modificado el cliente : '" + cliente.getId());
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED) ;
	}
	
	
	@DeleteMapping("/clientes/delete/{id}")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<?> deleteClient(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<String, Object>();
		
		try {
		   clienteService.DeleteCliente(id);	
		} catch (DataAccessException e) {
			response.put("mensaje", "No se realizo la accion por problemas en el servidor");
			response.put("error", "ERROR : " + e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		

	    response.put("mensaje", "Se ha eliminado el cliente con exito!");
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK) ;
	}
	

}
