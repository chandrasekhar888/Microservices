package com.microservices.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.microservices.service.CustomerUserDetailsService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
	
	@Autowired
	private JwtFilter filter;
	String[] openUrl = {
		    "/api/v1/auth/register/**",
		    "/api/v1/auth/login",
		    
		    "/v3/api-docs/**",
		    "/swagger-ui/**",
		    "/swagger-ui.html",
		    "/swagger-resources/**",
		    "/webjars/**"
		};
	@Autowired
	private CustomerUserDetailsService customerUserDetailsService;

	@Bean
	public PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

	 @Bean
		public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
			return config.getAuthenticationManager();
		}
	    
	    @Bean
		public AuthenticationProvider authProvider() {

			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();//from authenticate it comes here

			authProvider.setUserDetailsService(customerUserDetailsService);//here object has details coming from db
			authProvider.setPasswordEncoder(getEncoder());

			return authProvider;//final info is in this it will return to authenticate(token)
		}
	
//	@Bean
//	public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {
//	    http
//	        .csrf(csrf -> csrf.disable())  // Disable CSRF
//	        .authorizeHttpRequests(auth -> 
//	            auth.requestMatchers(openUrl).permitAll()
//	            .requestMatchers("/api/v1/admin/hello").hasRole("ADMIN")  ////has any role we can use to give access for both .hasAnyRole("ADMIN", "MANAGER")
//	            .anyRequest()
//	           //.authenticated()//	anyRequest().authenticated(), which just checks if the user is authenticated,but not authorized, hence the 403 Forbidden.*/
//
//	        );
//	        //).httpBasic(); //in order to test the auth to do form related testing to enable form via user
//	    //when i uncomment this it is used for service normal forms but i am using this for jwt so no longer 
//    //return http.build();
//	    //when we add role the return statement should disable csrf
//	    return http.csrf().disable().build();
//}
//	
	    @Bean
		public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception{
			
			http.authorizeHttpRequests( req -> {
				req.requestMatchers(openUrl)
				   .permitAll()
				   .requestMatchers("/api/v1/admin/welcome").hasRole("ADMIN")
				   .anyRequest()
				   .authenticated();			
			}) .authenticationProvider(authProvider())
	        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
			
			return http.csrf().disable().build();
		}


	}
