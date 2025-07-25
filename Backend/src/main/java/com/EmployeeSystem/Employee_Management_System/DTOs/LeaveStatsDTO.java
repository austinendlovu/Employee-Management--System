package com.EmployeeSystem.Employee_Management_System.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveStatsDTO {
    private int entitledDays;
    private long usedDays;
    private long remainingDays;

    private Integer daysUntilNextLeave;         // days before next approved leave starts
    private Integer daysLeftInOngoingLeave;     // days left if leave is currently ongoing
}
