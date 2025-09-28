package com.bugtracker.service;

import com.bugtracker.dto.BugTableDTO;
import com.bugtracker.dto.DashboardDTO;
import com.bugtracker.dto.IssueHistoryDTO;

import java.util.List;

public interface DashboardService {

    DashboardDTO getDashboardSummary(Long projectId);

    List<BugTableDTO> getFilteredIssues(Long projectId, String status, Long assigneeId, String priority, String startDate, String endDate);

    BugTableDTO updateIssueStatus(Long issueId, String newStatus, Long userId);

    BugTableDTO assignIssue(Long issueId, Long assigneeId, Long userId);

    List<IssueHistoryDTO> getIssueHistory(Long issueId);
}
