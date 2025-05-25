package com.microservices.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.microservices.UserDto.UserDto;
import com.microservices.UserRepo.APIResponse;
import com.microservices.UserRepo.UserRepository;
import com.microservices.entity.User;

@Service
public class AuthService {

	     @Autowired
	     private UserRepository userRepository;
	
	     @Autowired
	     private PasswordEncoder passwordEncoder;
	
	     APIResponse<String> response =new APIResponse<>();
	
    public APIResponse<String> register(UserDto dto) {
		
		if(userRepository.existsByUsername(dto.getUsername())) {
			response.setMessage("Registration Failed");
			response.setStatus(500);
			response.setData("User with username exists");
			return response;
		}
		if(userRepository.existsByEmail(dto.getEmail())) {
			response.setMessage("Registration Failed");
			response.setStatus(500);
			response.setData("User with Email Id exists");
			return response;
		}
		
		User user =new User();
		BeanUtils.copyProperties(dto, user);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		//user.setRole("ADMIN");
		//user.setRole("MIKE");
	//manually setting up here 	or else not required by default
		userRepository.save(user);
		
		response.setMessage("Registration Done");
		response.setStatus(201);
		response.setData("User is registered");
		
		return response;
}

}
