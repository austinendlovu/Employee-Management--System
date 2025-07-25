package com.EmployeeSystem.Employee_Management_System.Controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeSystem.Employee_Management_System.DTOs.AuthenticationResponse;
import com.EmployeeSystem.Employee_Management_System.DTOs.ResetPasswordRequest;
import com.EmployeeSystem.Employee_Management_System.Models.User;
import com.EmployeeSystem.Employee_Management_System.Repositories.UserRepository;
import com.EmployeeSystem.Employee_Management_System.Services.AuthenticationService;
import com.EmployeeSystem.Employee_Management_System.Services.JwtService;
import com.EmployeeSystem.Employee_Management_System.Services.PasswordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@Data
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordService passwordService;

   
    @Operation(summary = "Register a new staff member",
            description = "Creates a new staff account (e.g. teacher, admin, etc.) and returns a JWT token with user details")
 @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Staff member registered successfully"),
     @ApiResponse(responseCode = "400", description = "Invalid input or staff already exists")
 })
 @PostMapping("/register")
 public ResponseEntity<Map<String, Object>> register(@RequestBody User request) {
     AuthenticationResponse response = authenticationService.register(request);

     Map<String, Object> body = new HashMap<>();
     body.put("message", "You have successfully registered!");
     body.put("authenticationResponse", response);

     return new ResponseEntity<>(body, HttpStatus.OK);
 }

 @Operation(summary = "Login as staff",
            description = "Authenticates a staff member and returns a JWT token with user details")
 @ApiResponses(value = {
     @ApiResponse(responseCode = "200", description = "Staff logged in successfully"),
     @ApiResponse(responseCode = "401", description = "Invalid credentials"),
     @ApiResponse(responseCode = "400", description = "Missing or invalid request body")
 })
 @PostMapping("/login")
 public ResponseEntity<Map<String, Object>> login(@RequestBody User request) {
     AuthenticationResponse response = authenticationService.authenticate(request);

     Map<String, Object> body = new HashMap<>();
     body.put("message", "You have successfully logged in!");
     body.put("authenticationResponse", response);

     return new ResponseEntity<>(body, HttpStatus.OK);
 }

 @PostMapping("/forgot-password")
 @Operation(summary = "Send password reset link", description = "Sends a reset token to user's email")
 public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
     String email = request.get("email");

     if (email == null || email.isBlank()) {
         return ResponseEntity.badRequest().body("Email is required");
     }

     passwordService.sendResetToken(email);
     return ResponseEntity.ok("Reset link sent to email");
 }
 @PostMapping("/reset-password")
 @Operation(summary = "Reset password", description = "Resets user password with valid token")
 public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
     passwordService.resetPassword(request.getToken(), request.getNewPassword());
     return ResponseEntity.ok("Password reset successful");
 }

}

