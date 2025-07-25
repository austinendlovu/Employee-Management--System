package com.EmployeeSystem.Employee_Management_System.Services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.EmployeeSystem.Employee_Management_System.DTOs.EmployeeWorkReport;
import com.EmployeeSystem.Employee_Management_System.DTOs.SubmitWorkLogRequest;
import com.EmployeeSystem.Employee_Management_System.Models.EmployeeProfile;
import com.EmployeeSystem.Employee_Management_System.Models.User;
import com.EmployeeSystem.Employee_Management_System.Models.WorkLog;
import com.EmployeeSystem.Employee_Management_System.Repositories.EmployeeProfileRepository;
import com.EmployeeSystem.Employee_Management_System.Repositories.UserRepository;
import com.EmployeeSystem.Employee_Management_System.Repositories.WorkLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkLogService {

    private final WorkLogRepository workLogRepo;
    private final UserRepository userRepo;
    private final EmployeeProfileRepository profileRepo;

    public void submitLog(SubmitWorkLogRequest request, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmployeeProfile profile = profileRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Employee profile not found"));

        WorkLog log = WorkLog.builder()
                .employee(profile)
                .workDate(request.getWorkDate())
                .taskSummary(request.getTaskSummary())
                .hoursWorked(request.getHoursWorked())
                .comments(request.getComments())
                .submittedAt(LocalDateTime.now())
                .build();

        workLogRepo.save(log);
    }

    public List<WorkLog> getMyLogs(String username) {
        return workLogRepo.findByEmployee_User_UsernameOrderByWorkDateDesc(username);
    }

    public List<WorkLog> getLogsByEmployee(Long employeeId) {
        return workLogRepo.findByEmployee_IdOrderByWorkDateDesc(employeeId);
    }

    public List<WorkLog> getAllLogs() {
        return workLogRepo.findAll(Sort.by(Sort.Direction.DESC, "workDate"));
    }
    public EmployeeWorkReport generateReport(Long employeeId, LocalDate fromDate, LocalDate toDate) {

        List<WorkLog> logs = workLogRepo.findByEmployee_IdAndWorkDateBetweenOrderByWorkDateAsc(employeeId, fromDate, toDate);

        if (logs.isEmpty()) {
            throw new RuntimeException("No work logs found for the selected period");
        }

        EmployeeProfile profile = logs.get(0).getEmployee();

        double totalHours = logs.stream()
                .mapToDouble(WorkLog::getHoursWorked)
                .sum();

        long totalLogs = logs.size();

        Map<LocalDate, Double> hoursPerDay = logs.stream()
                .collect(Collectors.groupingBy(
                        WorkLog::getWorkDate,
                        LinkedHashMap::new,
                        Collectors.summingDouble(WorkLog::getHoursWorked)
                ));

        double avgHours = totalHours / totalLogs;

        return EmployeeWorkReport.builder()
                .employeeName(profile.getFullName())
                .employeeId(profile.getId())
                .totalHoursWorked(totalHours)
                .totalLogsSubmitted(totalLogs)
                .firstLogDate(logs.get(0).getWorkDate())
                .lastLogDate(logs.get(logs.size() - 1).getWorkDate())
                .hoursPerDay(hoursPerDay)
                .averageHoursPerDay(avgHours)
                .build();
    }
    public EmployeeWorkReport generateFullReport(Long employeeId) {
        List<WorkLog> logs = workLogRepo.findByEmployee_IdOrderByWorkDateDesc(employeeId);

        if (logs.isEmpty()) {
            throw new RuntimeException("No work logs found for this employee");
        }

        EmployeeProfile profile = logs.get(0).getEmployee();

        double totalHours = logs.stream()
                .mapToDouble(WorkLog::getHoursWorked)
                .sum();

        long totalLogs = logs.size();

        Map<LocalDate, Double> hoursPerDay = logs.stream()
                .collect(Collectors.groupingBy(
                        WorkLog::getWorkDate,
                        LinkedHashMap::new,
                        Collectors.summingDouble(WorkLog::getHoursWorked)
                ));

        double avgHours = totalLogs > 0 ? totalHours / totalLogs : 0;

        return EmployeeWorkReport.builder()
                .employeeName(profile.getFullName())
                .employeeId(profile.getId())
                .totalHoursWorked(totalHours)
                .totalLogsSubmitted(totalLogs)
                .firstLogDate(logs.get(logs.size() - 1).getWorkDate())  // Last element = oldest date
                .lastLogDate(logs.get(0).getWorkDate())                 // First element = latest date
                .hoursPerDay(hoursPerDay)
                .averageHoursPerDay(avgHours)
                .build();
    }


}