package com.EmployeeSystem.Employee_Management_System.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkLogResponse {
    private Long id;
    private String employeeName;
    private LocalDate workDate;
    private String taskSummary;
    private Double hoursWorked;
    private String comments;
    private LocalDateTime submittedAt;
}