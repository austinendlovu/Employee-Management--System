package com.EmployeeSystem.Employee_Management_System.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeSystem.Employee_Management_System.DTOs.EmployeeWorkReport;
import com.EmployeeSystem.Employee_Management_System.Models.WorkLog;
import com.EmployeeSystem.Employee_Management_System.Services.WorkLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/worklogs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminWorkLogController {

    private final WorkLogService workLogService;

    @GetMapping
    public ResponseEntity<List<WorkLog>> getAllLogs() {
        return ResponseEntity.ok(workLogService.getAllLogs());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<WorkLog>> getLogsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(workLogService.getLogsByEmployee(employeeId));
    }
    @GetMapping("/report/{employeeId}")
    public ResponseEntity<EmployeeWorkReport> getReport(
            @PathVariable Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        EmployeeWorkReport report = workLogService.generateReport(employeeId, from, to);
        return ResponseEntity.ok(report);
    }
    // 🔥 New Endpoint: Get full report without date filtering
    @GetMapping("/report/all/{employeeId}")
    public ResponseEntity<EmployeeWorkReport> getFullReport(@PathVariable Long employeeId) {
        EmployeeWorkReport report = workLogService.generateFullReport(employeeId);
        return ResponseEntity.ok(report);
    }
}