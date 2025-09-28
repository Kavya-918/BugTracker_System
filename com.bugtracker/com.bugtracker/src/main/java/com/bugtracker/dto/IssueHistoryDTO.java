//package com.bugtracker.dto;
//
//import java.time.LocalDateTime;
//
//public class IssueHistoryDTO {
//    private Long id;
//    private Long commentId;
//    private Long changedById;
//    private String fieldChanged;
//    private String oldValue;
//    private String newValue;
//    private LocalDateTime changedAt;
//
//    // Getters & Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public Long getCommentId() { return commentId; }
//    public void setCommentId(Long commentId) { this.commentId = commentId; }
//
//    public Long getChangedById() { return changedById; }
//    public void setChangedById(Long changedById) { this.changedById = changedById; }
//
//    public String getFieldChanged() { return fieldChanged; }
//    public void setFieldChanged(String fieldChanged) { this.fieldChanged = fieldChanged; }
//
//    public String getOldValue() { return oldValue; }
//    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
//
//    public String getNewValue() { return newValue; }
//    public void setNewValue(String newValue) { this.newValue = newValue; }
//
//    public LocalDateTime getChangedAt() { return changedAt; }
//    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
//}



package com.bugtracker.dto;

import java.time.LocalDateTime;

public class IssueHistoryDTO {
    private Long id;
    private Long issueId;
    private Long changedById;
    private Long commentId;   // ✅ Added for comment support
    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private LocalDateTime changedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIssueId() { return issueId; }
    public void setIssueId(Long issueId) { this.issueId = issueId; }

    public Long getChangedById() { return changedById; }
    public void setChangedById(Long changedById) { this.changedById = changedById; }

    public Long getCommentId() { return commentId; }
    public void setCommentId(Long commentId) { this.commentId = commentId; }

    public String getFieldChanged() { return fieldChanged; }
    public void setFieldChanged(String fieldChanged) { this.fieldChanged = fieldChanged; }

    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }

    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}

