package com.hsbc.jwt.Service.ServiceImpl;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hsbc.jwt.Service.IUserService;
import com.hsbc.jwt.entity.User;
import com.hsbc.jwt.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService{

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Override
	public Integer saveUser(User user) {
		user.setPassword(encoder.encode(user.getPassword()));
		User savedUser = repo.save(user);
		return savedUser.getId();
	}

	@Override
	public User findByUsername(String username) {
		
		Optional<User> user = repo.findByUserName(username);
		if(!user.isEmpty()) {
			return user.get();
		}else {
			throw new RuntimeException("No Such User found!");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repo.findByUserName(username).get();
		org.springframework.security.core.userdetails.User mappedUser= new org.springframework.security.core.userdetails.User(username, user.getPassword()
				, user.getRoles().stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toSet()));
		
		return mappedUser;
	}

}
