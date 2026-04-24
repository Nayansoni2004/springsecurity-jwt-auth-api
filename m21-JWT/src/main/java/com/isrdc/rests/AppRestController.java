package com.isrdc.rests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.isrdc.entities.Employee;
import com.isrdc.jwts.JwtService;
import com.isrdc.services.EmployeeService;

@RestController
public class AppRestController {
	
	@Autowired
	private JwtService jwtServ;
	
	@Autowired
	private EmployeeService empServ;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private PasswordEncoder passEnc;
	
//	Secured EndPoint
//	http://localhost:9090/emp_txn -> use postman->select Auth -> Auth Type -> Bearer Token-> paste token received on successful signin...
	@GetMapping("/emp_txn")
	public String empTxn() {
		return "Employee Transaction report generated...";
	}
	
	//Public EndPoint
//	http://localhost:9090/signin -> use postman -> body -> raw -> json -> enter string email, pass -> send -> copy access token.. for nxt req.
	@PostMapping("/signin")
	public ResponseEntity<String> signinEmployee(@RequestBody Employee emp) {
		UsernamePasswordAuthenticationToken token 
									= new UsernamePasswordAuthenticationToken(emp.getEmail(), emp.getPassword());
		
		Authentication auth = authManager.authenticate(token);
		
		if(auth.isAuthenticated()) {
			String jwtToken = jwtServ.generateToken(emp.getEmail());
			return new ResponseEntity<>("Signin Successful..." + jwtToken, HttpStatus.ACCEPTED);
		}
		
		return new ResponseEntity<>("Signin failed~~~~", HttpStatus.NOT_ACCEPTABLE);
	}
	
	//Public End Point
//	http://localhost:9090/signup -> use postman -> Body -> raw-> json -> enter json string name, email, pass -> send req.
	@PostMapping("/signup")
	public ResponseEntity<String> employeeSignup(@RequestBody Employee emp) {
		emp.setPassword(passEnc.encode(emp.getPassword()));
		empServ.saveEmployee(emp);
		
		return new ResponseEntity<>("New Account Created", HttpStatus.CREATED);
	}
}
