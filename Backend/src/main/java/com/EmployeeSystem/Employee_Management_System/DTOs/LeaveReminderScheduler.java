package com.EmployeeSystem.Employee_Management_System.DTOs;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.EmployeeSystem.Employee_Management_System.Models.LeaveRequest;
import com.EmployeeSystem.Employee_Management_System.Models.LeaveStatus;
import com.EmployeeSystem.Employee_Management_System.Repositories.LeaveRequestRepository;
import com.EmployeeSystem.Employee_Management_System.Services.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LeaveReminderScheduler {

    private final LeaveRequestRepository leaveRepo;
    private final EmailService emailService;

    @Scheduled(cron = "0 8 * * *") // every day at 8AM
    public void remindEmployeesAboutLeave() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<LeaveRequest> upcoming = leaveRepo.findAll().stream()
            .filter(l -> l.getStartDate().equals(tomorrow) && l.getStatus() == LeaveStatus.APPROVED)
            .toList();

        for (LeaveRequest leave : upcoming) {
            String msg = String.format("Reminder: Your leave starts tomorrow (%s to %s).",
                leave.getStartDate(), leave.getEndDate());

            emailService.sendGeneralNotification(
                leave.getEmployee().getUser().getEmail(),
                "Leave Starts Tomorrow",
                msg
            );
        }
    }
}

