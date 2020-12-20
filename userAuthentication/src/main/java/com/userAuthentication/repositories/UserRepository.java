package com.userAuthentication.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.userAuthentication.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
	
	// retrieves all users from database
	List<User> findAll();
	// retrieves a User by email from database
	User findByEmail(String email);
}