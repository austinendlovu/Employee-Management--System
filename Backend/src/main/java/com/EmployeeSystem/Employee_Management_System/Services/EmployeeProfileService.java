package com.EmployeeSystem.Employee_Management_System.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.EmployeeSystem.Employee_Management_System.DTOs.createProfileRequest;
import com.EmployeeSystem.Employee_Management_System.Models.EmployeeProfile;
import com.EmployeeSystem.Employee_Management_System.Models.User;
import com.EmployeeSystem.Employee_Management_System.Repositories.EmployeeProfileRepository;
import com.EmployeeSystem.Employee_Management_System.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeProfileService {

    private final EmployeeProfileRepository profileRepo;
    private final UserRepository userRepo;

    public void createProfile(createProfileRequest request, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (profileRepo.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException("Profile already exists");
        }

        EmployeeProfile profile = EmployeeProfile.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .position(request.getPosition())
                .annualLeaveDays(21)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        profileRepo.save(profile);

        // Flip the setProfileSetup flag to true
        user.setSetProfileSetup(true);
        userRepo.save(user);
    }

    public EmployeeProfile getMyProfile(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return profileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public void updateProfile(createProfileRequest request, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        EmployeeProfile profile = profileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setFullName(request.getFullName());
        profile.setPhone(request.getPhone());
        profile.setDepartment(request.getDepartment());
        profile.setPosition(request.getPosition());

        profileRepo.save(profile);
    }
    public List<EmployeeProfile> getAllProfiles() {
        return profileRepo.findAll();
    }

    public EmployeeProfile getProfileById(Long id) {
        return profileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public void updateProfileById(createProfileRequest request, Long id) {
        EmployeeProfile profile = profileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setFullName(request.getFullName());
        profile.setPhone(request.getPhone());
        profile.setDepartment(request.getDepartment());
        profile.setPosition(request.getPosition());

        profileRepo.save(profile);
    }

    public void deleteProfile(Long id) {
        EmployeeProfile profile = profileRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profileRepo.delete(profile);
    }

}