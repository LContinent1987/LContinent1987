package com.spring.security.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.MimeTypeUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	
	@Bean
	public UserDetailsService userDetailsService(BCryptPasswordEncoder encoder) {

		UserDetails user = User.withUsername("zhou")
				           .password(encoder.encode("1"))
				           .roles("ADMIN")
				           .build();
		return new InMemoryUserDetailsManager(user);
		
	}	 	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
		.authorizeHttpRequests()
		.requestMatchers("/login.html","/login","/logout","/indexNonAuthenticated.html").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login.html")		
		.loginProcessingUrl("/login")
		.usernameParameter("inputUsername")  // input  username
		.passwordParameter("inputPassword")  // input  password		
		.successHandler(new AuthenticationSuccessHandler() {
			
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				// TODO Auto-generated method stub
				ObjectMapper objectMapper = new ObjectMapper();
				response.setStatus(HttpStatus.OK.value());
				response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
				response.getOutputStream().write(objectMapper.writeValueAsBytes(authentication));
				
			}
		})
	    .failureHandler(new AuthenticationFailureHandler() {
			
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				// TODO Auto-generated method stub
				ObjectMapper objectMapper = new ObjectMapper();
				response.setStatus(HttpStatus.OK.value());
				response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
				response.getOutputStream().write(objectMapper.writeValueAsBytes(exception.toString()));
			}
		})		
		.and()
		.sessionManagement()
		.maximumSessions(1)
		.maxSessionsPreventsLogin(true);
	     
		
		return http.build();
		
	}
	
}
