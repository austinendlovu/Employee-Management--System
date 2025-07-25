package com.EmployeeSystem.Employee_Management_System.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeSystem.Employee_Management_System.DTOs.EmployeeWorkReport;
import com.EmployeeSystem.Employee_Management_System.DTOs.SubmitWorkLogRequest;
import com.EmployeeSystem.Employee_Management_System.Models.WorkLog;
import com.EmployeeSystem.Employee_Management_System.Services.WorkLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee/worklogs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeWorkLogController {

    private final WorkLogService workLogService;

    @PostMapping
    public ResponseEntity<?> submit(@RequestBody SubmitWorkLogRequest request,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        workLogService.submitLog(request, userDetails.getUsername());
        return ResponseEntity.ok("Work log submitted");
    }

    @GetMapping
    public ResponseEntity<List<WorkLog>> getMyLogs(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(workLogService.getMyLogs(userDetails.getUsername()));
    }



}
