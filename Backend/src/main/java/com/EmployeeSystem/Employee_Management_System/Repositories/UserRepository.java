package com.EmployeeSystem.Employee_Management_System.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EmployeeSystem.Employee_Management_System.Models.User;

public interface UserRepository extends JpaRepository<User, Long>{
	 Optional<User> findByUsername(String username);

	    boolean existsByUsername(String username);
	    Optional<User> findByEmail(String email);
	    
	    Optional<User> findByResetToken(String resetToken);
}
