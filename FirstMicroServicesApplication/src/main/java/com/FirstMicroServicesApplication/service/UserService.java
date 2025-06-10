package com.FirstMicroServicesApplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.FirstMicroServicesApplication.entities.User;
import com.FirstMicroServicesApplication.exception.UserNotFoundException;


public interface UserService {
	public User saveUser(User user) throws UserNotFoundException;
	
	public List<User> getAllUser();
	
	public User updateUser(User user, String userId) throws UserNotFoundException;
	
	public void deleteUser(String userId) throws UserNotFoundException;
	

	User getUserById(String userId) throws UserNotFoundException;
}
