package com.hsbc.jwt.Service;

import com.hsbc.jwt.entity.User;

public interface IUserService {

	Integer saveUser(User user);
	
	User findByUsername(String username);
}
