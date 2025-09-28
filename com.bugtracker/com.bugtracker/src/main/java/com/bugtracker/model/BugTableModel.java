package com.bugtracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bug_table")
public class BugTableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mapping to ProjectTable, no new projectId column
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectTableModel project;

    private String bugDescription;

    private Long reporterId;

    private Long assigneeId;

    @Enumerated(EnumType.STRING)
    private BugStatus status;

    @Enumerated(EnumType.STRING)
    private BugPriority priority;

    private String remarks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String bugReportedName;

    private String bugSolvedName;
    
    //for email purpose created 
    private String reporterEmail;

    public String getReporterEmail() {
		return reporterEmail;
	}
	public void setReporterEmail(String reporterEmail) {
		this.reporterEmail = reporterEmail;
	}
	// Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProjectTableModel getProject() { return project; }
    public void setProject(ProjectTableModel project) { this.project = project; }

    public String getBugDescription() { return bugDescription; }
    public void setBugDescription(String bugDescription) { this.bugDescription = bugDescription; }

    public Long getReporterId() { return reporterId; }
    public void setReporterId(Long reporterId) { this.reporterId = reporterId; }

    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }

    public BugStatus getStatus() { return status; }
    public void setStatus(BugStatus status) { this.status = status; }

    public BugPriority getPriority() { return priority; }
    public void setPriority(BugPriority priority) { this.priority = priority; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getBugReportedName() { return bugReportedName; }
    public void setBugReportedName(String bugReportedName) { this.bugReportedName = bugReportedName; }

    public String getBugSolvedName() { return bugSolvedName; }
    public void setBugSolvedName(String bugSolvedName) { this.bugSolvedName = bugSolvedName; }

}
