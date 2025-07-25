package com.EmployeeSystem.Employee_Management_System.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeSystem.Employee_Management_System.DTOs.AuthenticationResponse;
import com.EmployeeSystem.Employee_Management_System.DTOs.RegisterEmployeeRequest;
import com.EmployeeSystem.Employee_Management_System.Models.Role;
import com.EmployeeSystem.Employee_Management_System.Models.User;
import com.EmployeeSystem.Employee_Management_System.Services.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/api/auth")
public class AddEmployeeController {

    private final AuthenticationService authenticationService;

    public AddEmployeeController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register a new employee",
               description = "This endpoint registers a new employee and sends a notification email.")
    @PostMapping("/register-employee")
    public ResponseEntity<?> registerEmployee(@org.springframework.web.bind.annotation.RequestBody RegisterEmployeeRequest request) {
        AuthenticationResponse response = authenticationService.registerEmployeeWithNotification(request);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Employee registered successfully and email sent.");
        body.put("authenticationResponse", response);

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }


}

