package com.bugtracker.service;

import com.bugtracker.dto.CommentDTO;
import com.bugtracker.dto.AttachmentDTO;
import com.bugtracker.dto.IssueHistoryDTO;
import java.util.List;

public interface HistoryService {
    CommentDTO addComment(CommentDTO commentDTO);
    List<CommentDTO> getCommentsByIssue(Long issueId);

    AttachmentDTO addAttachment(AttachmentDTO attachmentDTO);
    List<AttachmentDTO> getAttachmentsByComment(Long commentId);

    IssueHistoryDTO logHistory(IssueHistoryDTO historyDTO);
     List<IssueHistoryDTO> getHistoryByIssue(Long issueId);

   
}
