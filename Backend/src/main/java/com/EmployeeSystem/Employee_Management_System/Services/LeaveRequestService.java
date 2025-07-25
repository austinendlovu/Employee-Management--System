package com.EmployeeSystem.Employee_Management_System.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.EmployeeSystem.Employee_Management_System.DTOs.ApplyLeaveRequest;
import com.EmployeeSystem.Employee_Management_System.DTOs.LeaveStatsDTO;
import com.EmployeeSystem.Employee_Management_System.Models.EmployeeProfile;
import com.EmployeeSystem.Employee_Management_System.Models.LeaveRequest;
import com.EmployeeSystem.Employee_Management_System.Models.LeaveStatus;
import com.EmployeeSystem.Employee_Management_System.Models.User;
import com.EmployeeSystem.Employee_Management_System.Repositories.EmployeeProfileRepository;
import com.EmployeeSystem.Employee_Management_System.Repositories.LeaveRequestRepository;
import com.EmployeeSystem.Employee_Management_System.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRepo;
    private final EmployeeProfileRepository profileRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    public void applyLeave(ApplyLeaveRequest request, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmployeeProfile profile = profileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Employee profile not found"));

        LeaveRequest leave = LeaveRequest.builder()
                .employee(profile)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .appliedAt(LocalDateTime.now())
                .build();

        leaveRepo.save(leave);

        emailService.sendLeaveAppliedEmail(user.getEmail(), leave);
    }

    public List<LeaveRequest> getMyLeaves(String username) {
        return leaveRepo.findByEmployee_User_Username(username);
    }

    public List<LeaveRequest> getAll() {
        return leaveRepo.findAll(Sort.by(Sort.Direction.DESC, "appliedAt"));
    }


public void approveLeave(Long id) {
    LeaveRequest leave = leaveRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Leave not found"));

    leave.setStatus(LeaveStatus.APPROVED);
    leaveRepo.save(leave);

    String email = leave.getEmployee().getUser().getEmail();
    emailService.sendLeaveApprovedEmail(email, leave);

    // Notify all other employees
    List<User> users = userRepo.findAll();
    String fullName = leave.getEmployee().getFullName();

    for (User u : users) {
        if (!u.getId().equals(leave.getEmployee().getUser().getId())) {
            String body = String.format(
                "%s will be on leave from %s to %s.",
                fullName, leave.getStartDate(), leave.getEndDate()
            );
            emailService.sendGeneralNotification(u.getEmail(), "Employee Leave Notification", body);
        }
    }
}

    public void rejectLeave(Long id) {
        LeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        leave.setStatus(LeaveStatus.REJECTED);
        leaveRepo.save(leave);

        String email = leave.getEmployee().getUser().getEmail();
        emailService.sendLeaveRejectedEmail(email, leave);
    }

    public void cancelLeave(Long id, String username) {
        LeaveRequest leave = leaveRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (!leave.getEmployee().getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Cannot cancel non-pending leave");
        }

        leaveRepo.delete(leave);
    }
    public LeaveStatsDTO getLeaveStats(String username) {
        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        EmployeeProfile profile = profileRepo.findByUserId(user.getId())
            .orElseThrow(() -> new RuntimeException("Profile not found"));

        List<LeaveRequest> approvedLeaves = leaveRepo.findApprovedByEmployee(profile.getId());

        long usedDays = approvedLeaves.stream()
            .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
            .sum();

        LocalDate today = LocalDate.now();

        // Filter upcoming leaves (start date > today)
        Optional<LeaveRequest> nextLeave = approvedLeaves.stream()
            .filter(l -> l.getStartDate().isAfter(today))
            .min(Comparator.comparing(LeaveRequest::getStartDate));

        // Filter ongoing leave (startDate <= today <= endDate)
        Optional<LeaveRequest> ongoingLeave = approvedLeaves.stream()
            .filter(l -> !l.getStartDate().isAfter(today) && !l.getEndDate().isBefore(today))
            .findFirst();

        Integer daysUntilNextLeave = nextLeave
            .map(l -> (int) ChronoUnit.DAYS.between(today, l.getStartDate()))
            .orElse(null);

        Integer daysLeftInOngoingLeave = ongoingLeave
            .map(l -> (int) ChronoUnit.DAYS.between(today, l.getEndDate()) + 1)
            .orElse(null);

        return new LeaveStatsDTO(
            profile.getAnnualLeaveDays(),
            usedDays,
            profile.getAnnualLeaveDays() - usedDays,
            daysUntilNextLeave,
            daysLeftInOngoingLeave
        );
    }

}