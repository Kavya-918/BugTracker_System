package com.bugtracker.serviceimp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugtracker.dto.AttachmentDTO;
import com.bugtracker.dto.CommentDTO;
import com.bugtracker.dto.IssueHistoryDTO;
import com.bugtracker.model.AttachmentModel;
import com.bugtracker.model.BugTableModel;
import com.bugtracker.model.CommentModel;
import com.bugtracker.model.IssueHistoryModel;
import com.bugtracker.model.ProjectTableModel;
import com.bugtracker.model.RegisterModel;
import com.bugtracker.repository.AttachmentRepo;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.CommentRepo;
import com.bugtracker.repository.HistoryRepo;
import com.bugtracker.repository.IssueHistoryRepo;
import com.bugtracker.repository.LoginRepo;
import com.bugtracker.repository.ProjectTableRepository;
import com.bugtracker.service.HistoryService;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryRepo historyRepo;
    
    @Autowired
    private LoginRepo registerRepo;
    
    @Autowired
    private  BugRepository bugRepo;
    
    @Autowired
    private ProjectTableRepository projectrepo;
    
    @Autowired
    private IssueHistoryRepo issueHistoryRepo;
    
    @Autowired
    private AttachmentRepo attachmentRepo;

    @Autowired
    private CommentRepo commentRepo;


    @Override
    public CommentDTO addComment(CommentDTO dto) {
        CommentModel comment = new CommentModel();
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        // Fetch author from DB
        RegisterModel author = registerRepo.findById(dto.getAuthorId())
            .orElseThrow(() -> new RuntimeException("Author not found"));
        comment.setAuthor(author);

        // Fetch issue from DB
        BugTableModel issue = bugRepo.findById(dto.getIssueId())
            .orElseThrow(() -> new RuntimeException("Issue not found"));
        comment.setIssue(issue);

        // Fetch project from DB
        ProjectTableModel project = projectrepo.findById(dto.getProjectId())
            .orElseThrow(() -> new RuntimeException("Project not found"));
        comment.setProject(project);

        // Save comment
        historyRepo.save(comment);

        dto.setId(comment.getId());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }



    @Override
    public List<CommentDTO> getCommentsByIssue(Long issueId) {
        return historyRepo.findAll().stream()
                .filter(c -> c.getIssue().getId().equals(issueId))
                .map(c -> {
                    CommentDTO dto = new CommentDTO();
                    dto.setId(c.getId());
                    dto.setIssueId(c.getIssue().getId());
                    dto.setAuthorId(c.getAuthor().getCandidateId());
                    dto.setProjectId(c.getProject().getProjectId());
                    dto.setContent(c.getContent());
                    dto.setCreatedAt(c.getCreatedAt());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public AttachmentDTO addAttachment(AttachmentDTO dto) {
        CommentModel comment = commentRepo.findById(dto.getCommentId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        RegisterModel user = registerRepo.findById(dto.getUploadedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        AttachmentModel attachment = new AttachmentModel();
        attachment.setComment(comment);
        attachment.setUploadedBy(user);
        attachment.setFileName(dto.getFileName());
        attachment.setFileUrl(dto.getFileUrl());
        attachment.setUploadedAt(LocalDateTime.now());

        attachmentRepo.save(attachment);

        dto.setId(attachment.getId());
        dto.setUploadedAt(attachment.getUploadedAt());
        dto.setUploadedById(user.getCandidateId());
        dto.setFileUrl(attachment.getFileUrl());

        return dto;
    }

    @Override
    public List<AttachmentDTO> getAttachmentsByComment(Long commentId) {
        System.out.println("inside getAttachmentsServiceimp");

        // 1. Fetch attachments from DB
        List<AttachmentModel> attachments = attachmentRepo.findByCommentId(commentId);

        // 2. Convert to DTO
        return attachments.stream()
                .map(att -> {
                    AttachmentDTO dto = new AttachmentDTO();
                    dto.setId(att.getId());
                    dto.setCommentId(att.getComment().getId());
                    dto.setUploadedById(att.getUploadedBy().getCandidateId());
                    dto.setFileName(att.getFileName());
                    dto.setFileUrl(att.getFileUrl());
                    dto.setUploadedAt(att.getUploadedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

   

    public IssueHistoryDTO logHistory(IssueHistoryDTO dto) {
        IssueHistoryModel history = new IssueHistoryModel();

        BugTableModel issue = bugRepo.findById(dto.getIssueId())
                .orElseThrow(() -> new RuntimeException("Issue not found"));
        RegisterModel user = registerRepo.findById(dto.getChangedById())
                .orElseThrow(() -> new RuntimeException("User not found"));

        history.setIssue(issue);
        history.setChangedBy(user);
        history.setFieldChanged(dto.getFieldChanged());
        history.setOldValue(dto.getOldValue());
        history.setNewValue(dto.getNewValue());

        IssueHistoryModel saved = issueHistoryRepo.save(history);

        IssueHistoryDTO response = new IssueHistoryDTO();
        response.setId(saved.getId());
        response.setIssueId(saved.getIssue().getId());
        response.setChangedById(saved.getChangedBy().getCandidateId());
        response.setFieldChanged(saved.getFieldChanged());
        response.setOldValue(saved.getOldValue());
        response.setNewValue(saved.getNewValue());
        response.setChangedAt(saved.getChangedAt());

        return response;
    }


    public List<IssueHistoryDTO> getHistoryByIssue(Long issueId) {
    	System.out.println("inside getHistoryByIssueserviceimp");
        return issueHistoryRepo.findByIssueId(issueId)
                .stream()
                .map(h -> {
                    IssueHistoryDTO dto = new IssueHistoryDTO();
                    dto.setId(h.getId());
                    dto.setIssueId(h.getIssue().getId());
                 // inside mapping loop
                    dto.setChangedById(h.getChangedBy().getCandidateId());

                    dto.setFieldChanged(h.getFieldChanged());
                    dto.setOldValue(h.getOldValue());
                    dto.setNewValue(h.getNewValue());
                    dto.setChangedAt(h.getChangedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }





	
}
