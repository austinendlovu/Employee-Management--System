package com.EmployeeSystem.Employee_Management_System.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="employee_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fullName;
	private String phone;
	private String department;
	private String position;
	
	
	
	@OneToOne
	@JoinColumn(name = "user_id",referencedColumnName = "id")
	private User user;
	
	private LocalDateTime createdAt;
	
	@Column(name = "annual_leave_days")
	private int annualLeaveDays = 21; // default entitlement

	@Column(name = "leave_days_due")
	private int leaveDaysDue = 30; // annual default
	
	@Column(name = "leave_days_taken")
	private int leaveDaysTaken = 0;
		
}
