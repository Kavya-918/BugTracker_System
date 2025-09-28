//package com.bugtracker.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "issue_table")
//public class IssueHistoryModel {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "comment_id", nullable = false)
//    private CommentModel comment;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "changed_by", nullable = false)
//    private RegisterModel changedBy;
//
//    private String fieldChanged;
//    private String oldValue;
//    private String newValue;
//    private LocalDateTime changedAt;
//
//    // Getters & Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public CommentModel getComment() { return comment; }
//    public void setComment(CommentModel comment) { this.comment = comment; }
//
//    public RegisterModel getChangedBy() { return changedBy; }
//    public void setChangedBy(RegisterModel changedBy) { this.changedBy = changedBy; }
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



package com.bugtracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issue_history")
public class IssueHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The issue this history belongs to
    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    private BugTableModel issue;

    // Who made the change
    @ManyToOne
    @JoinColumn(name = "changed_by", nullable = false)
    private RegisterModel changedBy;
    
    
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentModel comment;


    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private LocalDateTime changedAt;

    @PrePersist
    protected void onCreate() {
        this.changedAt = LocalDateTime.now();
    }

    // Getters and Setters
 // Add getter/setter for comment
    public CommentModel getComment() { return comment; }
    public void setComment(CommentModel comment) { this.comment = comment; }

    public Long getId() { return id; }
    public BugTableModel getIssue() { return issue; }
    public void setIssue(BugTableModel issue) { this.issue = issue; }
    public RegisterModel getChangedBy() { return changedBy; }
    public void setChangedBy(RegisterModel changedBy) { this.changedBy = changedBy; }
    public String getFieldChanged() { return fieldChanged; }
    public void setFieldChanged(String fieldChanged) { this.fieldChanged = fieldChanged; }
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}

