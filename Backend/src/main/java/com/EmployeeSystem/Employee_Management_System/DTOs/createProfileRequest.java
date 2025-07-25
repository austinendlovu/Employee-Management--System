package com.EmployeeSystem.Employee_Management_System.DTOs;

import lombok.Data;

@Data
public class createProfileRequest {
    private String fullName;
    private String phone;
    private String department;
    private String position;
}
