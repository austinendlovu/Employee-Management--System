package com.EmployeeSystem.Employee_Management_System.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeSystem.Employee_Management_System.DTOs.createProfileRequest;
import com.EmployeeSystem.Employee_Management_System.Models.EmployeeProfile;
import com.EmployeeSystem.Employee_Management_System.Services.EmployeeProfileService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/employees")
@RequiredArgsConstructor
@Tag(name = "Admin - Employee Management", description = "Endpoints for managing employee profiles")
public class EmployeeManagementController {

    private final EmployeeProfileService profileService;

    @GetMapping
    public ResponseEntity<List<EmployeeProfile>> getAllEmployees() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProfile> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProfileById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployeeProfile(@PathVariable Long id,
                                                   @RequestBody createProfileRequest request) {
        profileService.updateProfileById(request, id);
        return ResponseEntity.ok("Profile updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return ResponseEntity.ok("Employee deleted");
    }
}
