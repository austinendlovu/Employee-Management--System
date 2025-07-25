package com.EmployeeSystem.Employee_Management_System.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeSystem.Employee_Management_System.DTOs.ApplyLeaveRequest;
import com.EmployeeSystem.Employee_Management_System.DTOs.LeaveStatsDTO;
import com.EmployeeSystem.Employee_Management_System.Models.LeaveRequest;
import com.EmployeeSystem.Employee_Management_System.Services.LeaveRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee/leaves")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeLeaveController {

    private final LeaveRequestService leaveService;

    @PostMapping
    public ResponseEntity<?> apply(@RequestBody ApplyLeaveRequest request,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        leaveService.applyLeave(request, userDetails.getUsername());
        return ResponseEntity.ok("Leave applied");
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> myLeaves(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(leaveService.getMyLeaves(userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelLeave(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        leaveService.cancelLeave(id, userDetails.getUsername());
        return ResponseEntity.ok("Leave cancelled");
    }
    @GetMapping("/stats")
    public ResponseEntity<LeaveStatsDTO> leaveStats(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(leaveService.getLeaveStats(userDetails.getUsername()));
    }
}