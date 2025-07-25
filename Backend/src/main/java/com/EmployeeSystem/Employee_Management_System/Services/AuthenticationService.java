package com.EmployeeSystem.Employee_Management_System.Services;

import java.security.SecureRandom;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.EmployeeSystem.Employee_Management_System.DTOs.AuthenticationResponse;
import com.EmployeeSystem.Employee_Management_System.DTOs.RegisterEmployeeRequest;
import com.EmployeeSystem.Employee_Management_System.Models.Role;
import com.EmployeeSystem.Employee_Management_System.Models.User;
import com.EmployeeSystem.Employee_Management_System.Repositories.UserRepository;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService mailService;

    public AuthenticationService(
        AuthenticationManager authenticationManager,
        UserRepository repository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        EmailService mailService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.mailService = mailService;
    }

    public AuthenticationResponse register(User request) {
        User user = new User();

        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (request.getCreatedAt() != null) {
            user.setCreatedAt(request.getCreatedAt());
        }

        user.setRole(request.getRole() == null ? Role.ADMIN : request.getRole());
        user.setSetProfileSetup(false); // profile not yet set

        user = repository.save(user);

        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .token(token)
            .role(user.getRole().name())
            .user(user)
            .build();
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        User user = repository.findByUsername(request.getUsername())
                              .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .token(token)
            .role(user.getRole().name())
            .user(user)
            .profileSetup(user.isSetProfileSetup()) // include the flag
            .build();
    }

    public AuthenticationResponse registerEmployeeWithNotification(RegisterEmployeeRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email must be provided");
        }

        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username must be provided");
        }

        Role role = Role.EMPLOYEE;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            role = Role.valueOf(request.getRole().toUpperCase());
        }

        String rawPassword = generateRandomPassword(8);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = User.builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .password(encodedPassword)
            .role(role)
            .setProfileSetup(false) // profile not yet set
            .build();

        user = repository.save(user);

        String token = jwtService.generateToken(user);

        try {
            mailService.sendEmployeeWelcomeEmail(
                user.getEmail(),
                user.getUsername(),
                rawPassword,
                role.name()
            );
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }

        return AuthenticationResponse.builder()
            .token(token)
            .role(role.name())
            .user(user)
            .build();
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
