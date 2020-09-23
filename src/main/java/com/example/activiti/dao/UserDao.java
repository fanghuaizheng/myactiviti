package com.example.activiti.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.activiti.entity.User;

public interface UserDao extends JpaRepository<User, String> {
	
	public User  findById(String id);
	

}
