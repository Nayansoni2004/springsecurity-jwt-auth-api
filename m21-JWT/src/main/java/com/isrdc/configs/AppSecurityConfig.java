package com.isrdc.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.isrdc.filters.AppFilter;
import com.isrdc.services.EmployeeService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
	@Autowired
	private AppFilter appFilter; 
	
	@Autowired
	private EmployeeService empServ;

	@Bean
	public PasswordEncoder passwEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProv = new DaoAuthenticationProvider(empServ);
		authProv.setPasswordEncoder(passwEncoder());
		return authProv;
	}

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain configSecurity(HttpSecurity httpSecurity) {
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(req->req
						.requestMatchers("/signin", "/signup").permitAll()
						.anyRequest().authenticated()
						)
				.sessionManagement(session->session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authProvider())
				.addFilterBefore(appFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

//	@Bean
//	public SecurityFilterChain configSecurity(HttpSecurity httpSecurity) {
//		httpSecurity.authorizeHttpRequests(req -> req
//				.requestMatchers("/signin", "/signup")
//				.permitAll()
//				.anyRequest()
//				.authenticated()
//				).httpBasic(Customizer.withDefaults())
//				.formLogin(Customizer.withDefaults());
//		
//		return httpSecurity.csrf(csrf -> csrf.disable()).build();
//		
//	}
}
