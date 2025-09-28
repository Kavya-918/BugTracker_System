package com.bugtracker.serviceimp;


import com.bugtracker.model.BugTableModel;
import com.bugtracker.model.RegisterModel;
import com.bugtracker.repository.LoginRepo;
import com.bugtracker.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


/*
 * createBug() → notify reporter + assignee (if any).

assignIssue() → notify assignee (assigned by Admin/PM).

updateIssueStatus() → notify assignee and/or reporter when status changes.
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    
    @Autowired
    private LoginRepo loginRepo; // for users

    @Override
    public void sendCustomEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void sendIssueCreatedEmail(BugTableModel bug, String reporterEmail, String assigneeEmail) {

        // --------------------------
        // 1️⃣ Reporter Email Template
        // --------------------------
        String reporterSubject = "Bug Report Created Successfully: " + bug.getBugDescription();
        String reporterBody = "Dear Reporter,\n\n" +
                "Your reported issue has been successfully logged in our Bug Tracking System.\n\n" +
                "Bug Details:\n" +
                "Description: " + bug.getBugDescription() + "\n" +
                "Assigned To: " + (assigneeEmail != null ? assigneeEmail : "Unassigned") + "\n" +
                "Priority: " + bug.getPriority() + "\n\n" +
                "Best Regards,\n" +
                "Kavya Solutions\n" +
                "Mysore";

        // Send email to reporter
        sendCustomEmail(reporterEmail, reporterSubject, reporterBody);

        // --------------------------
        // 2️⃣ Assignee/Developer Email Template
        // --------------------------
        if (assigneeEmail != null) {
            String assigneeSubject = "New Bug Assigned: " + bug.getBugDescription();
            String assigneeBody = "Dear Developer,\n\n" +
                    "A new issue has been assigned to you. Please review and resolve the issue at the earliest.\n\n" +
                    "Bug Details:\n" +
                    "Description: " + bug.getBugDescription() + "\n" +
                    "Reporter Email: " + reporterEmail + "\n" +
                    "Priority: " + bug.getPriority() + "\n\n" +
                    "Please complete this task as soon as possible and report the status back to the reporter.\n\n" +
                    "Best Regards,\n" +
                    "Kavya Solutions\n" +
                    "Mysore";

            // Send email to assignee
            sendCustomEmail(assigneeEmail, assigneeSubject, assigneeBody);
        }
    }


    @Override
    public void sendAssignmentEmail(BugTableModel bug, RegisterModel assignee, RegisterModel assignedBy) {
        // --- Email to Assignee ---
        String assigneeSubject = "Issue Assigned: " + bug.getBugDescription();
        String assigneeBody = "Hello " + assignee.getName() + ",\n\n" +
                "You have been assigned a new issue.\n" +
                "Description: " + bug.getBugDescription() + "\n" +
                "Assigned By: " + assignedBy.getName() + "\n" +
                "Priority: " + bug.getPriority() + "\n\n" +
                "Please start working on it as soon as possible.\n\nThanks.";
        sendCustomEmail(assignee.getEmail(), assigneeSubject, assigneeBody);

        // --- Email to Reporter ---
        // Fetch reporter
        RegisterModel reporter = loginRepo.findById(bug.getReporterId())
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        String reporterSubject = "Issue Update: " + bug.getBugDescription();
        String reporterBody = "Hello " + reporter.getName() + ",\n\n" +
                "Your reported issue has been assigned to " + assignee.getName() + ".\n" +
                "Assigned By: " + assignedBy.getName() + "\n" +
                "Priority: " + bug.getPriority() + "\n\n" +
                "You will be notified of any updates.\n\nThanks.";
        sendCustomEmail(reporter.getEmail(), reporterSubject, reporterBody);
    }


    @Override
    public void sendStatusChangeEmail(BugTableModel bug, String oldStatus, String newStatus, RegisterModel changedBy) {
        // -------------------
        // Reporter email
        // -------------------
        if (bug.getReporterEmail() != null && !bug.getReporterEmail().isEmpty()) {
            String subjectReporter = "Your reported issue has a status update: " + bug.getBugDescription();
            String bodyReporter = "Hello Reporter,\n\n" +
                    "The issue you reported has changed status.\n\n" +
                    "Issue: " + bug.getBugDescription() + "\n" +
                    "Old Status: " + oldStatus + "\n" +
                    "New Status: " + newStatus + "\n" +
                    "Changed By: " + changedBy.getName() + "\n\n" +
                    "Regards,\nBug Tracker System";

            sendCustomEmail(bug.getReporterEmail(), subjectReporter, bodyReporter);
        }

        // -------------------
        // Assignee email
        // -------------------
        if (bug.getAssigneeId() != null) {
            RegisterModel assignee = loginRepo.findById(bug.getAssigneeId()).orElse(null);
            if (assignee != null && assignee.getEmail() != null && !assignee.getEmail().isEmpty()) {
                String subjectAssignee = "Assigned issue status updated: " + bug.getBugDescription();
                String bodyAssignee = "Hello Assignee,\n\n" +
                        "An issue assigned to you has changed status.\n\n" +
                        "Issue: " + bug.getBugDescription() + "\n" +
                        "Old Status: " + oldStatus + "\n" +
                        "New Status: " + newStatus + "\n" +
                        "Changed By: " + changedBy.getName() + "\n\n" +
                        "Regards,\nBug Tracker System";

                sendCustomEmail(assignee.getEmail(), subjectAssignee, bodyAssignee);
            }
        }
    }

}
