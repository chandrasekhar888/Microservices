package com.microservices.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.UserDto.LoginDto;
import com.microservices.UserDto.UserDto;
import com.microservices.UserRepo.APIResponse;
import com.microservices.service.AuthService;
import com.microservices.service.JwtService;


@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authManager; //go to usernamepassword
	
	@Autowired
	private JwtService jwtService;
	
	 @PostMapping("/register")
	    public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto dto) {
	        APIResponse<String> response = authService.register(dto);
	        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
	    }
	 @PostMapping("/login")
	 public ResponseEntity<APIResponse<String>> loginCheck(@RequestBody LoginDto loginDto){ 
		 //goes to DAO Authentication manager but not directly
		 
		 APIResponse<String> response = new APIResponse<>();
		 
		 UsernamePasswordAuthenticationToken token = 
				 new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
		//we cant give directly so we give via the  UsernamePasswordAuthenticationToken token to manager
		try {
			 Authentication authenticate = authManager.authenticate(token); //go to dao authentication provider
			 
			 if(authenticate.isAuthenticated()) {
				 String jwtToken = jwtService.generateToken(loginDto.getUsername(),
			                authenticate.getAuthorities().iterator().next().getAuthority());
				 response.setMessage("Login Sucessful");
				 response.setStatus(200);
				 response.setData(jwtToken);
				 return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 response.setMessage("Failed");
		 response.setStatus(401);
		 response.setData("Un-Authorized Access");
		 return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
	 }
}
