package com.cyb.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyb.entity.User;
import com.cyb.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	UserServiceImpl userServiceImpl;
	
	@Override
	public Set<User> findAllUsers() {
		
		return userServiceImpl.setUsers;
		
	}

}
