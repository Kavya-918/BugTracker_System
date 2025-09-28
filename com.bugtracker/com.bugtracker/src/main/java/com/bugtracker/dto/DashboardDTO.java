package com.bugtracker.dto;

import java.util.List;
import java.util.Map;

public class DashboardDTO {

    // Counts of issues grouped by status (OPEN, IN_PROGRESS, RESOLVED, CLOSED)
    private Map<String, Long> issueCountByStatus;

    // List of recent issues
    private List<BugTableDTO> recentIssues;

    // Getters and Setters
    public Map<String, Long> getIssueCountByStatus() {
        return issueCountByStatus;
    }

    public void setIssueCountByStatus(Map<String, Long> issueCountByStatus) {
        this.issueCountByStatus = issueCountByStatus;
    }

    public List<BugTableDTO> getRecentIssues() {
        return recentIssues;
    }

    public void setRecentIssues(List<BugTableDTO> recentIssues) {
        this.recentIssues = recentIssues;
    }
}
