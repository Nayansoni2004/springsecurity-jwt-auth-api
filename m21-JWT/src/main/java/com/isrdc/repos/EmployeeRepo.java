package com.isrdc.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isrdc.entities.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer>{
	public Employee findByEmail(String email);
}
