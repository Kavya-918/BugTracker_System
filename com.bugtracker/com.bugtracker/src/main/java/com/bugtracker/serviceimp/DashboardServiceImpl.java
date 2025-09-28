package com.bugtracker.serviceimp;

import com.bugtracker.dto.BugTableDTO;
import com.bugtracker.dto.DashboardDTO;
import com.bugtracker.dto.IssueHistoryDTO;
import com.bugtracker.model.BugTableModel;
import com.bugtracker.model.IssueHistoryModel;
import com.bugtracker.model.RegisterModel;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.IssueHistoryRepo;
import com.bugtracker.repository.LoginRepo;
import com.bugtracker.service.DashboardService;
import com.bugtracker.model.BugStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private BugRepository bugRepository;

    @Autowired
    private LoginRepo loginRepo; // for users
    
    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private IssueHistoryRepo historyRepo; // for audit history

    // -------------------------------
    // Dashboard summary
    // -------------------------------
    @Override
    public DashboardDTO getDashboardSummary(Long projectId) {
        System.out.println("DashboardServiceImpl.getDashboardSummary called with projectId: " + projectId);
        DashboardDTO dashboard = new DashboardDTO();
        List<BugTableModel> issues = projectId != null ?
                bugRepository.findByProject_ProjectId(projectId) : bugRepository.findAll();
        System.out.println("Fetched issues count: " + issues.size());

        // Status counts
        Map<String, Long> statusCount = issues.stream()
                .collect(Collectors.groupingBy(i -> i.getStatus().name(), Collectors.counting()));
        dashboard.setIssueCountByStatus(statusCount);
        System.out.println("Status counts calculated: " + statusCount);

        // Recent issues (last 5 updated)
        List<BugTableDTO> recentIssues = issues.stream()
                .sorted(Comparator.comparing(BugTableModel::getUpdatedAt).reversed())
                .limit(5)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        dashboard.setRecentIssues(recentIssues);
        System.out.println("Recent issues set: " + recentIssues.size());

        return dashboard;
    }

    // -------------------------------
    // Advanced filtering
    // -------------------------------
    @Override
    public List<BugTableDTO> getFilteredIssues(Long projectId, String status, Long assigneeId, String priority,
                                                String startDate, String endDate) {
        System.out.println("DashboardServiceImpl.getFilteredIssues called for projectId: " + projectId);
        List<BugTableModel> issues = bugRepository.findByProject_ProjectId(projectId);
        System.out.println("Fetched issues count: " + issues.size());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        List<BugTableDTO> filtered = issues.stream()
                .filter(i -> status == null || i.getStatus().name().equalsIgnoreCase(status))
                .filter(i -> assigneeId == null || (i.getAssigneeId() != null && i.getAssigneeId().equals(assigneeId)))
                .filter(i -> priority == null || i.getPriority().name().equalsIgnoreCase(priority))
                .filter(i -> {
                    if (startDate != null && endDate != null) {
                        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
                        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
                        return i.getCreatedAt().isAfter(start) && i.getCreatedAt().isBefore(end);
                    }
                    return true;
                })
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        System.out.println("Filtered issues count: " + filtered.size());

        return filtered;
    }
 // -------------------------------
 // Update issue status with role-based workflow
 // -------------------------------
 @Override
 public BugTableDTO updateIssueStatus(Long issueId, String newStatus, Long userId) {
     System.out.println("DashboardServiceImpl.updateIssueStatus called with issueId: " + issueId
             + ", newStatus: " + newStatus + ", userId: " + userId);

     // Fetch the issue
     BugTableModel issue = bugRepository.findById(issueId)
             .orElseThrow(() -> new RuntimeException("Issue not found"));
     // Fetch the user
     RegisterModel user = loginRepo.findById(userId)
             .orElseThrow(() -> new RuntimeException("User not found"));

     System.out.println("Fetched issue and user for status update");
     System.out.println("Current issue status: " + issue.getStatus());
     System.out.println("User role: " + user.getRole());

     String role = user.getRole();

     // Role-based workflow rules with debug prints
     switch (newStatus.toUpperCase()) {
         case "IN_PROGRESS":
             System.out.println("Attempting to move issue to IN_PROGRESS...");
             if (!role.equalsIgnoreCase("DEVELOPER")) {
                 System.out.println("Unauthorized: User is not a DEVELOPER");
                 throw new RuntimeException("Not authorized to move issue to IN_PROGRESS");
             }
             if (!issue.getStatus().name().equals("OPEN")) {
                 System.out.println("Unauthorized: Issue status is not OPEN");
                 throw new RuntimeException("Not authorized to move issue to IN_PROGRESS");
             }
             break;

         case "CLOSED":
             System.out.println("Attempting to move issue to CLOSED...");
             if (!role.equalsIgnoreCase("QA")) {
                 System.out.println("Unauthorized: User is not QA");
                 throw new RuntimeException("Not authorized to move issue to CLOSED");
             }
             if (!issue.getStatus().name().equals("RESOLVED")) {
                 System.out.println("Unauthorized: Issue status is not RESOLVED");
                 throw new RuntimeException("Not authorized to move issue to CLOSED");
             }
             break;

         case "RESOLVED":
             System.out.println("Attempting to move issue to RESOLVED...");
             // Optional: allow only DEVELOPER to resolve
             if (!role.equalsIgnoreCase("DEVELOPER") && !role.equalsIgnoreCase("PM")) {
                 System.out.println("Unauthorized: User is not allowed to RESOLVE");
                 throw new RuntimeException("Not authorized to move issue to RESOLVED");
             }
             break;

         default:
             System.out.println("Attempting to move issue to " + newStatus + " (no specific role check)");
             break;
     }

     // Save old status
     String oldStatus = issue.getStatus().name();
     issue.setStatus(BugStatus.valueOf(newStatus.toUpperCase()));
     issue.setUpdatedAt(LocalDateTime.now());
     bugRepository.save(issue);

     System.out.println("Issue status updated from " + oldStatus + " to " + newStatus);

     // ✅ Send email notification
     emailService.sendStatusChangeEmail(issue, oldStatus, newStatus, user);

     // Save history
     saveHistory(issue, user, "status", oldStatus, newStatus);

     return mapToDTO(issue);
 }


    // -------------------------------
    // Assign issue (only ADMIN/PM)
    // -------------------------------
    @Override
    public BugTableDTO assignIssue(Long issueId, Long assigneeId, Long userId) {
        System.out.println("DashboardServiceImpl.assignIssue called with issueId: " + issueId + ", assigneeId: " + assigneeId + ", userId: " + userId);
        BugTableModel issue = bugRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        RegisterModel user = loginRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().equalsIgnoreCase("ADMIN") && !user.getRole().equalsIgnoreCase("PM"))
            throw new RuntimeException("Not authorized to assign issues");

        Long oldAssignee = issue.getAssigneeId();
        issue.setAssigneeId(assigneeId);
        issue.setUpdatedAt(LocalDateTime.now());
        bugRepository.save(issue);
        System.out.println("Issue assigned from " + oldAssignee + " to " + assigneeId);

        // ✅ Send email notification
        RegisterModel assignee = loginRepo.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
        emailService.sendAssignmentEmail(issue, assignee, user);

        saveHistory(issue, user, "assigneeId", oldAssignee != null ? oldAssignee.toString() : null, assigneeId.toString());
        return mapToDTO(issue);

    }

    // -------------------------------
    // Get issue history
    // -------------------------------
    @Override
    public List<IssueHistoryDTO> getIssueHistory(Long issueId) {
        System.out.println("DashboardServiceImpl.getIssueHistory called for issueId: " + issueId);
        List<IssueHistoryDTO> history = historyRepo.findByIssueId(issueId).stream()
                .map(h -> {
                    IssueHistoryDTO dto = new IssueHistoryDTO();
                    dto.setId(h.getId());
                    dto.setIssueId(h.getIssue().getId());
                    dto.setChangedById(h.getChangedBy().getCandidateId());
                    dto.setCommentId(h.getComment() != null ? h.getComment().getId() : null);
                    dto.setFieldChanged(h.getFieldChanged());
                    dto.setOldValue(h.getOldValue());
                    dto.setNewValue(h.getNewValue());
                    dto.setChangedAt(h.getChangedAt());
                    return dto;
                })
                .collect(Collectors.toList());
        System.out.println("History records fetched: " + history.size());
        return history;
    }

    // -------------------------------
    // Helper: Save history
    // -------------------------------
    private void saveHistory(BugTableModel issue, RegisterModel user, String field, String oldValue, String newValue) {
        System.out.println("Saving history for issueId: " + issue.getId() + ", field: " + field);
        IssueHistoryModel history = new IssueHistoryModel();
        history.setIssue(issue);
        history.setChangedBy(user);
        history.setFieldChanged(field);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        history.setChangedAt(LocalDateTime.now());
        historyRepo.save(history);
        System.out.println("History saved successfully");
    }

    // -------------------------------
    // Helper: Map BugTableModel to DTO
    // -------------------------------
    private BugTableDTO mapToDTO(BugTableModel issue) {
        BugTableDTO dto = new BugTableDTO();
        dto.setProjectId(issue.getProject().getProjectId());
        dto.setProjectName(issue.getProject().getName());
        dto.setBugDescription(issue.getBugDescription());
        dto.setReporterId(issue.getReporterId());
        dto.setAssigneeId(issue.getAssigneeId());
        dto.setStatus(issue.getStatus());
        dto.setPriority(issue.getPriority());
        dto.setRemarks(issue.getRemarks());
        dto.setCreatedAt(issue.getCreatedAt());
        dto.setUpdatedAt(issue.getUpdatedAt());
        dto.setBugReportedName(issue.getBugReportedName());
        dto.setBugSolvedName(issue.getBugSolvedName());
        return dto;
    }

}
