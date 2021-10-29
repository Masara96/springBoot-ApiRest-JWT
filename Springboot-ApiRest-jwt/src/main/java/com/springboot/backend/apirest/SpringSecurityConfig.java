package com.springboot.backend.apirest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springboot.backend.apirest.auth.filter.JWTAuthenticationFilter;
import com.springboot.backend.apirest.auth.filter.JWTAuthorizationFilter;
import com.springboot.backend.apirest.auth.service.IJWTservice;
import com.springboot.backend.apirest.service.JpaUserDetailsService;


@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter  {
    
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	private IJWTservice jwtService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
	  // http.authorizeRequests().antMatchers("/").permitAll();
       http.authorizeRequests().antMatchers("POST","/api/login").permitAll();
	   
	   http.authorizeRequests().anyRequest().authenticated();
	   http.csrf().disable();
	   http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	   http.addFilter(new JWTAuthenticationFilter(authenticationManager(),jwtService));
	   http.addFilter(new JWTAuthorizationFilter(authenticationManager(),jwtService));
	   
		
	}
     
	
	
}
