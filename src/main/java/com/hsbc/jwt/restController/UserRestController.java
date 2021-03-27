package com.hsbc.jwt.restController;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hsbc.jwt.Service.IUserService;
import com.hsbc.jwt.entity.User;
import com.hsbc.jwt.entity.UserRequest;
import com.hsbc.jwt.entity.UserResponse;
import com.hsbc.jwt.util.JwtUtil;

@RestController
@RequestMapping("/user")
public class UserRestController {

	@Autowired
	private IUserService service;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@PostMapping("/save")
	public ResponseEntity<String> saveUser(@RequestBody User user){
		
		Integer userId = service.saveUser(user);
		return ResponseEntity.ok("User with id: '"+userId+"' saved successfully!");
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest userRequest){
		//Authenticate a user before generatin a Token
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword()));
		
		String token = jwtUtil.generateToken(userRequest.getUsername());
		
		return ResponseEntity.ok(new UserResponse(token, "Token Created Successfully!"));
		
	}
	
	@GetMapping("/welcome")
	public ResponseEntity<String> getLoginMessage(Principal principal) {
		System.out.println("loggedin username : '"+principal.getName()+"'");
		return new ResponseEntity<String>("loggedin username : '"+principal.getName()+"'", HttpStatus.CREATED);
	}
	
}
