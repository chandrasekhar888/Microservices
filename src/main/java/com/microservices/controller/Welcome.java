package com.microservices.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.entity.User;

@RestController
@RequestMapping("/api/v1/admin")
public class Welcome {
	
	
	@GetMapping("/hello")
	public String hello() {
	    return "Welcome, " ;
	}

}
