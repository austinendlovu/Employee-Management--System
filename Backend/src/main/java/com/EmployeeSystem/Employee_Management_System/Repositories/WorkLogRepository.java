package com.EmployeeSystem.Employee_Management_System.Repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EmployeeSystem.Employee_Management_System.Models.WorkLog;

@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {

    List<WorkLog> findByEmployee_User_UsernameOrderByWorkDateDesc(String username);

    List<WorkLog> findByEmployee_IdOrderByWorkDateDesc(Long employeeId);

List<WorkLog> findByEmployee_IdAndWorkDateBetweenOrderByWorkDateAsc(Long employeeId, LocalDate from, LocalDate to);
}
