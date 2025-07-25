package com.EmployeeSystem.Employee_Management_System.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EmployeeSystem.Employee_Management_System.Models.LeaveRequest;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee_User_Username(String username);
    @Query("""
    	    SELECT l FROM LeaveRequest l
    	    WHERE l.employee.id = :employeeId AND l.status = 'APPROVED'
    	""")
    	List<LeaveRequest> findApprovedByEmployee(@Param("employeeId") Long employeeId);
}


