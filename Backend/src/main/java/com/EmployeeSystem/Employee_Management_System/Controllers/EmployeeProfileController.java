package com.EmployeeSystem.Employee_Management_System.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeSystem.Employee_Management_System.DTOs.createProfileRequest;
import com.EmployeeSystem.Employee_Management_System.Models.EmployeeProfile;
import com.EmployeeSystem.Employee_Management_System.Services.EmployeeProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService profileService;

    @PostMapping("/setup")
    public ResponseEntity<?> createProfile(@RequestBody createProfileRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        profileService.createProfile(request, userDetails.getUsername());
        return ResponseEntity.ok("Profile created");
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeProfile> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(profileService.getMyProfile(userDetails.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody createProfileRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        profileService.updateProfile(request, userDetails.getUsername());
        return ResponseEntity.ok("Profile updated");
    }
}

