package com.microservices.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.microservices.service.CustomerUserDetailsService;
import com.microservices.service.JwtService;

import java.io.IOException;
//from the url it will extract the token the bearer token

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService; //we should add because validation part is in that

    @Autowired
    private CustomerUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization"); //spelling mainly required
        if (authHeader != null && authHeader.startsWith("Bearer ")) { //true
            String jwt = authHeader.substring(7);//this goes to ur jwtservice to validate by using autowired
            String username = jwtService.validateTokenAndRetrieveSubject(jwt); 

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { //second verification
            	//the valid things we keep in security holder thats where spring security will give u access
                var userDetails = userDetailsService.loadUserByUsername(username);
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());//java 10 feature

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}