package com.EmployeeSystem.Employee_Management_System.DTOs;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ApplyLeaveRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}