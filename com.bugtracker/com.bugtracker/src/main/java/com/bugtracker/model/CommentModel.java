package com.bugtracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comment_table")
public class CommentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One comment belongs to one issue
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private BugTableModel issue;

    // Map to user who added the comment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private RegisterModel author;

    // Map to Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectTableModel project;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    // Relations to attachments and history
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttachmentModel> attachments = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssueHistoryModel> histories = new ArrayList<>();

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BugTableModel getIssue() { return issue; }
    public void setIssue(BugTableModel issue) { this.issue = issue; }

    public RegisterModel getAuthor() { return author; }
    public void setAuthor(RegisterModel author) { this.author = author; }

    public ProjectTableModel getProject() { return project; }
    public void setProject(ProjectTableModel project) { this.project = project; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<AttachmentModel> getAttachments() { return attachments; }
    public void setAttachments(List<AttachmentModel> attachments) { this.attachments = attachments; }

    public List<IssueHistoryModel> getHistories() { return histories; }
    public void setHistories(List<IssueHistoryModel> histories) { this.histories = histories; }
}
