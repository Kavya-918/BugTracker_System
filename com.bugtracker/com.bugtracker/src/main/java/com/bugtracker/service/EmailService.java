package com.bugtracker.service;

import com.bugtracker.model.BugTableModel;
import com.bugtracker.model.RegisterModel;

public interface EmailService {

    void sendCustomEmail(String to, String subject, String body);

    

    void sendAssignmentEmail(BugTableModel bug, RegisterModel assignee, RegisterModel assignedBy);

    void sendStatusChangeEmail(BugTableModel bug, String oldStatus, String newStatus, RegisterModel changedBy);

	void sendIssueCreatedEmail(BugTableModel bug, String reporterEmail, String assigneeEmail);
}
