package com.EmployeeSystem.Employee_Management_System.DTOs;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterEmployeeRequest {
	private String email;
    private String username;
    private String role; // optional; can be null or "EMPLOYEE"
}

