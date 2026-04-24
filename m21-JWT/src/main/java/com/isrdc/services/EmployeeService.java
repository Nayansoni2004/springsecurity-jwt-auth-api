package com.isrdc.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.isrdc.entities.Employee;
import com.isrdc.repos.EmployeeRepo;

@Service
public class EmployeeService implements UserDetailsService {
	@Autowired
	private EmployeeRepo empRepo;
	
	public void saveEmployee(Employee emp) {
		empRepo.save(emp);
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Employee employee = empRepo.findByEmail(email);
		
		return new User(employee.getEmail(), employee.getPassword(), Collections.emptyList());
	}
}
