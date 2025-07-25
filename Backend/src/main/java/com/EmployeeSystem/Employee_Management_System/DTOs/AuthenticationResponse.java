package com.EmployeeSystem.Employee_Management_System.DTOs;


import com.EmployeeSystem.Employee_Management_System.Models.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {

    private String token;
    private String role;
    private User user;
    private boolean profileSetup;
   
}

