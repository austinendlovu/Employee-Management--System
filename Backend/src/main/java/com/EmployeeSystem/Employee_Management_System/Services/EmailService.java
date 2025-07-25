package com.EmployeeSystem.Employee_Management_System.Services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.EmployeeSystem.Employee_Management_System.Models.LeaveRequest;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetPasswordEmail(String to, String resetLink) {
        String subject = "Reset Your Password - Employee Management Sys";
        String htmlContent = buildResetEmailTemplate(resetLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            helper.setFrom("mukomiaustine8@gmail.com", "Employee Managment Systsem");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Enable HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email to " + to, e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during email sending", e);
        }
    }

    private String buildResetEmailTemplate(String resetLink) {
        return """
            <div style="font-family:Arial,sans-serif; padding:20px; background:#f9f9f9; color:#333;">
                <div style="max-width:600px; margin:auto; background:#fff; border-radius:8px; overflow:hidden; box-shadow:0 0 10px rgba(0,0,0,0.1);">
                    <div style="padding:20px; text-align:center; background:#4A90E2; color:#fff;">
                        <h2>Attendance System</h2>
                        <p>Password Reset Request</p>
                    </div>
                    <div style="padding:20px;">
                        <p>Hello,</p>
                        <p>We received a request to reset your password. Click the button below to set a new one:</p>
                        <div style="text-align:center; margin:30px 0;">
                            <a href="%s" style="background:#4A90E2; color:#fff; padding:12px 25px; border-radius:5px; text-decoration:none;">Reset Password</a>
                        </div>
                        <p>If you didn’t request this, you can safely ignore this email.</p>
                        <p style="margin-top:40px;">Thank you,<br>The Attendance System Team</p>
                    </div>
                </div>
            </div>
        """.formatted(resetLink);
    }
    public void sendEmployeeWelcomeEmail(String to, String username, String password, String role) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress("mukomiaustine8@gmail.com", "Employee Management System"));
            helper.setTo(to);
            helper.setSubject("Welcome to the Employee Management System");

            String htmlContent = """
                <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; padding: 30px; background-color: #f5f7fa; color: #2c3e50;">
                    <div style="max-width: 600px; margin: 0 auto; background-color: white; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); padding: 30px;">
                        <h1 style="color: #2980b9; font-size: 26px;">Welcome to the Employee Management System</h1>
                        <p style="font-size: 16px; line-height: 1.6;">Hello <strong>%s</strong>,</p>
                        <p style="font-size: 16px; line-height: 1.6;">You have been successfully registered as a <strong>%s</strong> in our system.</p>

                        <div style="background-color: #ecf0f1; padding: 20px; border-radius: 8px; margin: 20px 0;">
                            <p style="font-size: 16px;"><strong>Here are your login credentials:</strong></p>
                            <p style="font-size: 15px; color: #34495e;">Username: <code style="background-color: #dfe6e9; padding: 4px 8px; border-radius: 4px;">%s</code></p>
                            <p style="font-size: 15px; color: #34495e;">Password: <code style="background-color: #dfe6e9; padding: 4px 8px; border-radius: 4px;">%s</code></p>
                        </div>

                        <p style="font-size: 15px;">Click the button below to log in and get started:</p>
                        <a href="http://localhost:8081/login" 
                           style="display: inline-block; background-color: #2980b9; color: white; text-decoration: none; padding: 12px 20px; border-radius: 5px; font-weight: bold;">
                            Log In to EMS
                        </a>

                        <p style="margin-top: 30px; font-size: 14px; color: #7f8c8d;">
                            If you have any issues logging in, please contact the administrator.
                        </p>

                        <p style="margin-top: 40px; font-size: 14px;">
                            Regards,<br>
                            <strong>EMS Team</strong>
                        </p>
                    </div>
                </div>
            """.formatted(username, role, username, password);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendLeaveAppliedEmail(String to, LeaveRequest leave) {
        String subject = "Leave Request Submitted";
        String html = """
            <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#f9fafb; padding:30px;">
                <div style="max-width:600px; margin:auto; background:#fff; border-radius:8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding:30px;">
                    <h2 style="color:#0052cc; border-bottom: 2px solid #0052cc; padding-bottom: 10px;">Leave Request Submitted</h2>
                    <p style="font-size:16px; color:#333;">Your leave request has been submitted successfully.</p>
                    <p><strong>Dates:</strong> %s to %s</p>
                    <p><strong>Reason:</strong> %s</p>
                    <p><strong>Status:</strong> %s</p>
                    <p style="margin-top:30px; color:#555;">Thanks,<br>EMS Team</p>
                </div>
            </div>
            """.formatted(leave.getStartDate(), leave.getEndDate(), leave.getReason(), leave.getStatus());

        send(to, subject, html);
    }

    public void sendLeaveApprovedEmail(String to, LeaveRequest leave) {
        String subject = "Leave Request Approved";
        String html = """
            <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#f9fafb; padding:30px;">
                <div style="max-width:600px; margin:auto; background:#fff; border-radius:8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding:30px;">
                    <h2 style="color:#007a00; border-bottom: 2px solid #007a00; padding-bottom: 10px;">Leave Request Approved</h2>
                    <p style="font-size:16px; color:#333;">Good news! Your leave request has been approved.</p>
                    <p><strong>Dates:</strong> %s to %s</p>
                    <p style="margin-top:30px; color:#555;">Enjoy your leave!<br>EMS Team</p>
                </div>
            </div>
            """.formatted(leave.getStartDate(), leave.getEndDate());

        send(to, subject, html);
    }

    public void sendLeaveRejectedEmail(String to, LeaveRequest leave) {
        String subject = "Leave Request Rejected";
        String html = """
            <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background:#f9fafb; padding:30px;">
                <div style="max-width:600px; margin:auto; background:#fff; border-radius:8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding:30px;">
                    <h2 style="color:#cc0000; border-bottom: 2px solid #cc0000; padding-bottom: 10px;">Leave Request Rejected</h2>
                    <p style="font-size:16px; color:#333;">Your leave request has been rejected.</p>
                    <p><strong>Dates:</strong> %s to %s</p>
                    <p><strong>Reason:</strong> %s</p>
                    <p style="margin-top:30px; color:#555;">Please contact HR for more information.<br>EMS Team</p>
                </div>
            </div>
            """.formatted(leave.getStartDate(), leave.getEndDate(), leave.getReason());

        send(to, subject, html);
    }


    private void send(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);  // true = HTML enabled
            helper.setFrom("mukomiaustine8@gmail.com", "Employee Management System");

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    
    public void sendGeneralNotification(String to, String subject, String htmlBody) {
        send(to, subject, htmlBody);
    }


}
