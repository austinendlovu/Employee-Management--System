package com.EmployeeSystem.Employee_Management_System.Exceptions;

public class PasswordResetException extends RuntimeException {
    public PasswordResetException(String message) {
        super(message);
    }
}