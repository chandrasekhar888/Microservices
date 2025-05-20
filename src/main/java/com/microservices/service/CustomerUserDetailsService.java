package com.microservices.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.microservices.UserRepo.UserRepository;
import com.microservices.entity.User;

@Service
public class CustomerUserDetailsService implements UserDetailsService{ //we are implementing an interface so it loads the incomplete
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
		    throw new UsernameNotFoundException("User not found with username: " + username);
		}
		
		//return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),Collections.emptyList());
	//modified for role 
		return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),Collections.singleton(new SimpleGrantedAuthority("ROLE_" +user.getRole())));//This should be modified

	}

}