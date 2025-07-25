package com.EmployeeSystem.Employee_Management_System.DTOs;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SubmitWorkLogRequest {
    private LocalDate workDate;
    private String taskSummary;
    private Double hoursWorked;
    private String comments;
}