package com.isrdc.filters;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.isrdc.jwts.JwtService;
import com.isrdc.services.EmployeeService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AppFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtService jwtServ;
	
	@Autowired
	private EmployeeService empServ;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authHdr = request.getHeader("Authorization");
		String token = null;		
		String username = null;
		
		if(authHdr != null && authHdr.startsWith("Bearer ")) {
			token = authHdr.substring(7);
			username = jwtServ.extractUsername(token);
		}
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = empServ.loadUserByUsername(username);
			if(jwtServ.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authToken 
						= new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
