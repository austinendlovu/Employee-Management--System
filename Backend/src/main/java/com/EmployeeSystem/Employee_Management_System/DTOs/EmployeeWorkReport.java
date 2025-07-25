package com.EmployeeSystem.Employee_Management_System.DTOs;

import java.time.LocalDate;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeWorkReport {
    private String employeeName;
    private Long employeeId;

    private Double totalHoursWorked;
    private Long totalLogsSubmitted;

    private LocalDate firstLogDate;
    private LocalDate lastLogDate;

    private Map<LocalDate, Double> hoursPerDay; // e.g. {2025-07-20: 8.0, 2025-07-21: 6.5}
    private Double averageHoursPerDay;
}
