package com.bugtracker.controller;

import com.bugtracker.dto.BugTableDTO;
import com.bugtracker.dto.DashboardDTO;
import com.bugtracker.dto.IssueHistoryDTO;
import com.bugtracker.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
    

    //http://localhost:9090/api/dashboard?projectId=1
    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard(@RequestParam(required = false) Long projectId) {
        return ResponseEntity.ok(dashboardService.getDashboardSummary(projectId));
    }

    //http://localhost:9090/api/dashboard/projects/1/issues?status=OPEN&assigneeId=3&priority=HIGH&startDate=2025-09-21T00:00:00&endDate=2025-09-22T23:59:59
    @GetMapping("/projects/{projectId}/issues")
    public ResponseEntity<List<BugTableDTO>> getFilteredIssues(
            @PathVariable Long projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        return ResponseEntity.ok(dashboardService.getFilteredIssues(projectId, status, assigneeId, priority, startDate, endDate));
    }

    
    
    //http://localhost:9090/api/dashboard/issues/1/status?newStatus=IN_PROGRESS&userId=2
    @PutMapping("/issues/{issueId}/status")
    public ResponseEntity<BugTableDTO> updateStatus(
            @PathVariable Long issueId,
            @RequestParam String newStatus,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(dashboardService.updateIssueStatus(issueId, newStatus, userId));
    }

    
    //http://localhost:9090/api/dashboard/issues/1/assign?assigneeId=3&userId=1
    //set role as admin here
    //final testing -- error through
    @PutMapping("/issues/{issueId}/assign")
    public ResponseEntity<BugTableDTO> assignIssue(
            @PathVariable Long issueId,
            @RequestParam Long assigneeId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(dashboardService.assignIssue(issueId, assigneeId, userId));
    }

    
    //http://localhost:9090/api/dashboard/issues/1/history
    @GetMapping("/issues/{issueId}/history")
    public ResponseEntity<List<IssueHistoryDTO>> getIssueHistory(@PathVariable Long issueId) {
        return ResponseEntity.ok(dashboardService.getIssueHistory(issueId));
    }
}
