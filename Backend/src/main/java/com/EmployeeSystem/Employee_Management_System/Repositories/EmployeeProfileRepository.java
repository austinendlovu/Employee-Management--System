package com.EmployeeSystem.Employee_Management_System.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EmployeeSystem.Employee_Management_System.Models.EmployeeProfile;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
	Optional<EmployeeProfile> findByUserId(Long userId);

}
