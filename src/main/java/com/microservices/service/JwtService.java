package com.microservices.service;

import java.util.Date;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtService {

    private static final String SECRET_KEY = "my-super-secret-key";//signature
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    //Part -1 Generates Token 
    public String generateToken(String username, String role) {
        return JWT.create()
            .withSubject(username) //username role expiry stores in the pay load
            .withClaim("role", role)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(Algorithm.HMAC256(SECRET_KEY));
    }//algo goes to header

    //Part 2 -validates Token
    public String validateTokenAndRetrieveSubject(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY)) //take alogo and apply secret key 
            .build()
            .verify(token)//after decrypt verify and it gets username 
            .getSubject(); //and that username we are taking in filter class 
    }
}